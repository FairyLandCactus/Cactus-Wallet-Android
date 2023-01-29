package com.qianlihu.solanawallet.wallet;

import com.alphawallet.token.entity.Signable;
import com.qianlihu.solanawallet.BuildConfig;
import com.qianlihu.solanawallet.binance.BinanceWalletUtil;
import com.qianlihu.solanawallet.binance.Web3jUtils;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.wc.C;
import com.qianlihu.solanawallet.wc.SignatureFromKey;
import com.qianlihu.solanawallet.wc.SignatureReturnType;
import com.qianlihu.solanawallet.wc.TransactionData;
import com.qianlihu.solanawallet.wc.Wallet;
import com.qianlihu.solanawallet.wc.send.Transaction;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.Sign;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthSign;
import org.web3j.rlp.RlpEncoder;
import org.web3j.rlp.RlpList;
import org.web3j.rlp.RlpType;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import wallet.core.jni.Hash;

import static com.qianlihu.solanawallet.wc.CryptoFunctions.sigFromByteArray;

/**
 * author : Duan
 * date   : 2022/11/1010:03
 * desc   :
 * version: 1.0.0
 */
public class TransferUtils {

    private static String mPik = "";
    private static String mChain = "";

    public static byte[] createTokenTransferData(String to, BigInteger tokenAmount) {
        List<Type> params = Arrays.asList(new Address(to), new Uint256(tokenAmount));
        List<TypeReference<?>> returnTypes = Collections.singletonList(new TypeReference<Bool>() {
        });
        Function function = new Function("transfer", params, returnTypes);
        String encodedFunction = FunctionEncoder.encode(function);
        return Numeric.hexStringToByteArray(Numeric.cleanHexPrefix(encodedFunction));
    }

    public static Single<TransactionData> createTransactionWithSig(Transfcation tf) {

        mPik = tf.getPik();
        mChain = tf.getChain();
        Web3j web3j = Web3jUtils.initWeb3j();
        if (mChain.equals(Constant.ETH_CHAIN)) {
            web3j = Web3jUtils.initWeb3jEth();
        }
        final BigInteger useGasPrice = tf.getGasPrice();

        TransactionData txData = new TransactionData();

        Web3j finalWeb3j = web3j;
        return getNonceForTransaction(web3j, tf.getFrom().address, tf.getNonce())
                .flatMap(txNonce -> {
                    txData.nonce = txNonce;
                    return signTransaction(tf.getFrom(), tf.getToAddress(), tf.getSubunitAmount(), useGasPrice, tf.getGasLimit(), txNonce.longValue(), tf.getData(), tf.getChainId());
                })
                .flatMap(signedMessage -> Single.fromCallable(() -> {
                    if (signedMessage.sigType != SignatureReturnType.SIGNATURE_GENERATED) {
                        throw new Exception(signedMessage.failMessage);
                    }
                    txData.signature = Numeric.toHexString(signedMessage.signature);
                    EthSendTransaction raw = finalWeb3j
                            .ethSendRawTransaction(Numeric.toHexString(signedMessage.signature))
                            .send();
                    if (raw.hasError()) {
                        throw new Exception(raw.getError().getMessage());
                    }
                    txData.txHash = raw.getTransactionHash();
                    return txData;
                }))
                .flatMap(tx -> storeUnconfirmedTransaction(tf.getFrom(), tx, tf.getToAddress(), tf.getSubunitAmount(), tx.nonce, useGasPrice, tf.getGasLimit(), tf.getChainId(), tf.getData() != null ? Numeric.toHexString(tf.getData()) : "0x", ""))
                .subscribeOn(Schedulers.io());
    }

    private static Single<TransactionData> storeUnconfirmedTransaction(Wallet from, TransactionData txData, String toAddress, BigInteger value, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, long chainId, String data, String contractAddr) {
        return Single.fromCallable(() -> {
            Transaction newTx = new Transaction(txData.txHash, "0", "0", System.currentTimeMillis() / 1000, nonce.intValue(), from.address, toAddress, value.toString(10), "0", gasPrice.toString(10), data,
                    gasLimit.toString(10), chainId, contractAddr);
            return txData;
        });
    }

    public static Single<String> getSign(String puk, String data) {
        return Single.fromCallable(() -> {
            Web3j web3j = Web3jUtils.initWeb3j();
            EthSign sign = null;
            try {
                sign = web3j
                        .ethSign(puk, data)
                        .send();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sign.getSignature();
        }).subscribeOn(Schedulers.io());
    }

    public static Single<TransactionData> createTransactionWithSig(Wallet from, BigInteger gasPrice, BigInteger gasLimit, String data, long chainId) {
        Web3j web3j = Web3jUtils.initWeb3j();
        if (mChain.equals(Constant.ETH_CHAIN)) {
            web3j = Web3jUtils.initWeb3jEth();
        }
        final BigInteger useGasPrice = gasPrice;
        TransactionData txData = new TransactionData();

        Web3j finalWeb3j = web3j;
        return getLastTransactionNonce(web3j, from.address)
                .flatMap(txNonce -> {
                    txData.nonce = txNonce;
                    return getRawTransaction(txNonce, useGasPrice, gasLimit, BigInteger.ZERO, data);
                })
                .flatMap(rawTx -> signEncodeRawTransaction(rawTx, from, chainId))
                .flatMap(signedMessage -> Single.fromCallable(() -> {
                    txData.signature = Numeric.toHexString(signedMessage);
                    EthSendTransaction raw = finalWeb3j
                            .ethSendRawTransaction(Numeric.toHexString(signedMessage))
                            .send();
                    if (raw.hasError()) {
                        throw new Exception(raw.getError().getMessage());
                    }
                    txData.txHash = raw.getTransactionHash();
                    return txData;
                }))
                .flatMap(tx -> storeUnconfirmedTransaction(from, tx, "", BigInteger.ZERO, txData.nonce, useGasPrice, gasLimit, chainId, data, C.BURN_ADDRESS))
                .subscribeOn(Schedulers.io());
    }

    private static Single<byte[]> signEncodeRawTransaction(RawTransaction rtx, Wallet wallet, long chainId) {
        return Single.fromCallable(() -> TransactionEncoder.encode(rtx))
                .flatMap(encoded -> signTransaction(wallet, encoded, chainId))
                .flatMap(signatureReturn -> {
                    if (signatureReturn.sigType != SignatureReturnType.SIGNATURE_GENERATED) {
                        throw new Exception(signatureReturn.failMessage);
                    }
                    return encodeTransaction(signatureReturn.signature, rtx);
                });
    }

    public static final String FAILED_SIGNATURE = "00000000000000000000000000000000000000000000000000000000000000000";

    private static Single<byte[]> encodeTransaction(byte[] signatureBytes, RawTransaction rtx) {
        return Single.fromCallable(() -> {
            Sign.SignatureData sigData = sigFromByteArray(signatureBytes);
            if (sigData == null) return FAILED_SIGNATURE.getBytes();
            return encode(rtx, sigData);
        });
    }

    public static Single<SignatureFromKey> signTransaction(Wallet signer, byte[] message, long chainId) {
        return Single.fromCallable(() -> {
            //byte[] messageHash = Hash.sha3(message);
            SignatureFromKey returnSig = new SignatureFromKey();
            Credentials credentials = BinanceWalletUtil.loadCredentials(mPik);
            Sign.SignatureData signatureData = Sign.signMessage(
                    message, credentials.getEcKeyPair());
            returnSig.signature = bytesFromSignature(signatureData);
            returnSig.sigType = SignatureReturnType.SIGNATURE_GENERATED;
//            SignatureFromKey returnSig = signData(signer, message);
            returnSig.signature = patchSignatureVComponent(returnSig.signature);
            return returnSig;
        }).subscribeOn(Schedulers.io());
    }

    private static Single<RawTransaction> getRawTransaction(BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, BigInteger value, String data) {
        return Single.fromCallable(() ->
                RawTransaction.createContractTransaction(
                        nonce,
                        gasPrice,
                        gasLimit,
                        value,
                        data));
    }

    private static Single<BigInteger> getNonceForTransaction(Web3j web3j, String wallet, long nonce) {
        if (nonce != -1) //use supplied nonce
        {
            return Single.fromCallable(() -> BigInteger.valueOf(nonce));
        } else {
            return getLastTransactionNonce(web3j, wallet);
        }
    }

    public static Single<BigInteger> getLastTransactionNonce(Web3j web3j, String walletAddress) {
        return Single.fromCallable(() -> {
            try {
                EthGetTransactionCount ethGetTransactionCount = web3j
                        .ethGetTransactionCount(walletAddress, DefaultBlockParameterName.PENDING)
                        .send();
                return ethGetTransactionCount.getTransactionCount();
            } catch (Exception e) {
                return BigInteger.ZERO;
            }
        });
    }

    public static Single<SignatureFromKey> signTransaction(Wallet signer, String toAddress, BigInteger amount, BigInteger gasPrice, BigInteger gasLimit, long nonce, byte[] data, long chainId) {
        return Single.fromCallable(() -> {
            SignatureFromKey returnSig = new SignatureFromKey();
            Sign.SignatureData sigData;
            String dataStr = data != null ? Numeric.toHexString(data) : "";
            RawTransaction rtx = RawTransaction.createTransaction(
                    BigInteger.valueOf(nonce),
                    gasPrice,
                    gasLimit,
                    toAddress,
                    amount,
                    dataStr
            );

            byte[] signData = TransactionEncoder.encode(rtx, chainId);
            Credentials credentials = BinanceWalletUtil.loadCredentials(mPik);
            Sign.SignatureData signatureData = Sign.signMessage(
                    signData, credentials.getEcKeyPair());
            returnSig.signature = bytesFromSignature(signatureData);
            returnSig.sigType = SignatureReturnType.SIGNATURE_GENERATED;
            sigData = sigFromByteArray(returnSig.signature);
            if (sigData == null) {
                returnSig.sigType = SignatureReturnType.KEY_CIPHER_ERROR;
                returnSig.failMessage = "Incorrect signature length"; //should never see this message
            } else sigData = TransactionEncoder.createEip155SignatureData(sigData, chainId);
            returnSig.signature = encode(rtx, sigData);
            return returnSig;
        }).subscribeOn(Schedulers.io());
    }

    public static byte[] bytesFromSignature(Sign.SignatureData signature) {
        byte[] sigBytes = new byte[65];
        Arrays.fill(sigBytes, (byte) 0);
        try {
            System.arraycopy(signature.getR(), 0, sigBytes, 0, 32);
            System.arraycopy(signature.getS(), 0, sigBytes, 32, 32);
            System.arraycopy(signature.getV(), 0, sigBytes, 64, 1);
        } catch (IndexOutOfBoundsException e) {
            if (BuildConfig.DEBUG) e.printStackTrace();
        }

        return sigBytes;
    }

    private static byte[] encode(RawTransaction rawTransaction, Sign.SignatureData signatureData) {
        List<RlpType> values = TransactionEncoder.asRlpValues(rawTransaction, signatureData);
        RlpList rlpList = new RlpList(values);
        return RlpEncoder.encode(rlpList);
    }

    public static Single<SignatureFromKey> signMessage(Signable message) {
        return Single.fromCallable(() -> {
            //byte[] messageHash = Hash.sha3(message);
            byte[] digest = Hash.keccak256(message.getPrehash());
            SignatureFromKey returnSig = new SignatureFromKey();
            Credentials credentials = BinanceWalletUtil.loadCredentials(mPik);
            Sign.SignatureData signatureData = Sign.signMessage(
                    digest, credentials.getEcKeyPair());
            returnSig.signature = bytesFromSignature(signatureData);
//            SignatureFromKey returnSig = signData(signer, message.getPrehash());
//            returnSig.signature = patchSignatureVComponent(returnSig.signature);
            returnSig.sigType = SignatureReturnType.SIGNATURE_GENERATED;
            return returnSig;
        });
    }

    public static byte[] patchSignatureVComponent(byte[] signature) {
        if (signature != null && signature.length == 65 && signature[64] < 27) {
            signature[64] = (byte) (signature[64] + (byte) 0x1b);
        }
        return signature;
    }

    public static String hexToUtf8(String hex) {
        hex = Numeric.cleanHexPrefix(hex);
        ByteBuffer buff = ByteBuffer.allocate(hex.length() / 2);
        for (int i = 0; i < hex.length(); i += 2) {
            buff.put((byte) Integer.parseInt(hex.substring(i, i + 2), 16));
        }
        buff.rewind();
        Charset cs = Charset.forName("UTF-8");
        CharBuffer cb = cs.decode(buff);
        return cb.toString();
    }
}
