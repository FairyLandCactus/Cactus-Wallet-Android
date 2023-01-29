package com.qianlihu.solanawallet.utils;

import org.bouncycastle.util.encoders.Hex;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.*;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Assertions;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;

import static com.google.common.base.Predicates.equalTo;

/**
 * 以太坊签名消息校验工具
 */
public class MetaMaskUtil {
    public static final String PERSONAL_MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    static byte[] getEthereumMessagePrefix(int messageLength) {
        return PERSONAL_MESSAGE_PREFIX.concat(String.valueOf(messageLength)).getBytes();
    }

    static byte[] getEthereumMessageHash(byte[] message) {
        byte[] prefix = getEthereumMessagePrefix(message.length);
        byte[] result = new byte[prefix.length + message.length];
        System.arraycopy(prefix, 0, result, 0, prefix.length);
        System.arraycopy(message, 0, result, prefix.length, message.length);
        return Hash.sha3(result);
    }
    
    static BigInteger signedMessageHashToKey(byte[] messageHash, SignatureData signatureData) throws SignatureException {
        byte[] r = signatureData.getR();
        byte[] s = signatureData.getS();
        Assertions.verifyPrecondition(r != null && r.length == 32, "r must be 32 bytes");
        Assertions.verifyPrecondition(s != null && s.length == 32, "s must be 32 bytes");
        int header = signatureData.getV()[0] & 255;
        if (header >= 27 && header <= 34) {
            ECDSASignature sig = new ECDSASignature(new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS()));
            int recId = header - 27;
            BigInteger key = Sign.recoverFromSignature(recId, sig, messageHash);
            if (key == null) {
                throw new SignatureException("Could not recover public key from signature");
            } else {
                return key;
            }
        } else {
            throw new SignatureException("Header byte out of range: " + header);
        }
    }
    
    public static SignatureData getSignatureData(String signature){
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {v += 27;}
        return new SignatureData(v, Arrays.copyOfRange(signatureBytes, 0, 32), Arrays.copyOfRange(signatureBytes, 32, 64));
    }
    
    public static String signMessage(String message, String privateKey) {
        // 原文摘要字节数组
        byte[] contentHashBytes = Numeric.hexStringToByteArray(message);
//        byte[] contentHashBytes = Hash.sha3(message.getBytes());
        Credentials credentials = Credentials.create(privateKey);
        SignatureData signMessage = Sign.signMessage(getEthereumMessageHash(contentHashBytes), credentials.getEcKeyPair(),false);
        // 签名后的字符串
        return "0x" + Hex.toHexString(signMessage.getR()) + Hex.toHexString(signMessage.getS()) + Hex.toHexString(signMessage.getV());
    }

    public static boolean validateSign(String signature, String message, String address) {
        // 原文摘要字节数组
        byte[] contentHashBytes = Hash.sha3(message.getBytes());
        // 原文摘要16进制字符串
        String contentHashHex = Hex.toHexString(contentHashBytes);
        // 原文摘要 添加 ETH签名头信息后再生成摘要
        byte[] messageHash = getEthereumMessageHash(Hex.decode(contentHashHex));
        boolean flag = false;
        try {
            flag = validateSign(signature,messageHash,address);
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    public static boolean validateSign(String signature, byte[] messageHash, String address) throws SignatureException {
        SignatureData signMessage = getSignatureData(signature);
        //通过摘要和签名后的数据，还原公钥
        SignatureData signatureData = new SignatureData(signMessage.getV(), signMessage.getR(), signMessage.getS());
        BigInteger publicKey = signedMessageHashToKey(messageHash, signatureData);
        String parseAddress = "0x" + Keys.getAddress(publicKey);
        return address.equalsIgnoreCase(parseAddress);
    }
    

//    public static void main(String[] args) throws Exception{
//        String msg = "hello";
//        String address = "0xa2f4ed67b25a84c5567787256f8ca0a79351896e";
//        String privateKey = "bc2f87f348aa4a6f7f762ec8ad32b520ae4cc356c78aac4a75414f22619894f1";
//        String signatrue = sign(msg,privateKey);
////        String signatrue = "0xb769a85b2ebd0ca45f07dcfe5d1b5f98de63ea5eddcd8d028124d7c9e7796e5e129522849e43139da2dfbbcfca807443f4e8e37a0701c7460032817b79cbab3c1b";
//        System.out.println("noSha3   : "+validateNoSha3(signatrue,msg,address));
//        System.out.println("sha3Web  : "+validateSha3Web(signatrue,msg,address));
//        System.out.println("sha3Java : "+validateSha3Java(signatrue,msg,address));
//    }
    
}