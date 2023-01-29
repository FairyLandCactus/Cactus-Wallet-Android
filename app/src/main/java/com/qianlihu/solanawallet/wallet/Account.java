package com.qianlihu.solanawallet.wallet;

import android.os.Build;
import android.util.Log;

import com.google.common.base.Preconditions;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.application.MyApplication;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.wallet_utils.HDKeyDerivationSol;
import com.qianlihu.solanawallet.utils.wallet_utils.TweetNaclFast;
import com.qianlihu.solanawallet.utils.wallet_utils.crypto.HdUtil;
import com.qianlihu.solanawallet.utils.wallet_utils.wallet.DerivableType;
import com.qianlihu.solanawallet.utils.wallet_utils.wallet.SolanaBip44;

import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDDerivationException;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.crypto.MnemonicCode;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static com.qianlihu.solanawallet.utils.wallet_utils.wallet.DerivableType.BIP44;
import static org.bitcoinj.crypto.HDKeyDerivation.createMasterPrivKeyFromBytes;

public class Account {
    private TweetNaclFast.Signature.KeyPair keyPair;

    public Account() {
        this.keyPair = TweetNaclFast.Signature.keyPair();
    }

    public Account(byte[] secretKey) {
        this.keyPair = TweetNaclFast.Signature.keyPair_fromSecretKey(secretKey);
    }

    private Account(TweetNaclFast.Signature.KeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public static Account fromMnemonic(List<String> words, String passphrase) {
        byte[] seed = MnemonicCode.toSeed(words, passphrase);
        Log.i("duan==wallet", "种子==  " + MyUtils.bytesToHex(seed));
        DeterministicKey dk = HDKeyDerivationSol.createMasterPrivateKeySol(seed);
//        Log.i("duan==wallet", "==" + bytesToHex(dk.getChainCode()));
        List<ChildNumber> parentPath = HDUtils.parsePath("M/44H/501H/1H/0H");
        byte[] key = dk.getPrivKeyBytes();
        byte[] chainCode = dk.getChainCode();
        DeterministicKey mpKey = null;
        for (int i = 0; i < parentPath.size(); i++) {
            String path = parentPath.get(i).toString();
            String pathStr = path.substring(0, path.indexOf("H"));
            mpKey = createMasterPrivateKey(chainCode, key, Integer.parseInt(pathStr));
            chainCode = mpKey.getChainCode();
            key = mpKey.getSecretBytes();
        }
        if (mpKey == null) {
            return null;
        }
        String hexStr = mpKey.getPrivateKeyAsHex()+ MyUtils.bytesToHex(mpKey.getChainCode());
        TweetNaclFast.Signature.KeyPair keyPair = TweetNaclFast.Signature.keyPair_fromSeed(MyUtils.hexToByteArray(hexStr));
        return new Account(keyPair);
    }

    public static Account fromMnemonic2(List<String> words, String passphrase, int pathEnd) {
        byte[] seed = MnemonicCode.toSeed(words, passphrase);
        Log.i("duan==wallet", "种子==  " + MyUtils.bytesToHex(seed));
        DeterministicKey dk = HDKeyDerivationSol.createMasterPrivateKeySol(seed);
//        Log.i("duan==wallet", "==" + bytesToHex(dk.getChainCode()));
        List<ChildNumber> parentPath = HDUtils.parsePath("M/44H/501H/1H/"+pathEnd+"H");
        byte[] key = dk.getPrivKeyBytes();
        byte[] chainCode = dk.getChainCode();
        DeterministicKey mpKey = null;
        for (int i = 0; i < parentPath.size(); i++) {
            String path = parentPath.get(i).toString();
            String pathStr = path.substring(0, path.indexOf("H"));
            mpKey = createMasterPrivateKey(chainCode, key, Integer.parseInt(pathStr));
            chainCode = mpKey.getChainCode();
            key = mpKey.getSecretBytes();
        }
        if (mpKey == null) {
            return null;
        }
        String hexStr = mpKey.getPrivateKeyAsHex()+ MyUtils.bytesToHex(mpKey.getChainCode());
        TweetNaclFast.Signature.KeyPair keyPair = TweetNaclFast.Signature.keyPair_fromSeed(MyUtils.hexToByteArray(hexStr));
        return new Account(keyPair);
    }

    public static DeterministicKey createMasterPrivateKey(byte[] chainCode, byte[] key, long pathInt) throws HDDerivationException {
        Preconditions.checkArgument(chainCode.length > 8, MyApplication.getContexts().getString(R.string.seed_forced));
        if ((pathInt & 0x80000000) != 0) {
            return null;
        }
        byte[] data = HdUtil.append(new byte[]{0}, key);
//        Log.i("duan==wallet", "key==   " + bytesToHex(data));
//        Log.i("duan==wallet", "ser32==   " + bytesToHex(HdUtil.ser32(pathInt | 0x80000000)));
        data = HdUtil.append(data, HdUtil.ser32(pathInt | 0x80000000));
//        Log.i("duan==wallet", "data==   " + bytesToHex(data));
        byte[] i;
        i = HDUtilss.hmacSha512(HDUtilss.createHmacSha512Digest(chainCode), data);
//        Log.i("duan==wallet", "HmacSha512==seed   " + bytesToHex(i));
        Preconditions.checkState(i.length == 64, i.length);
        byte[] il = Arrays.copyOfRange(i, 0, 32);
        byte[] ir = Arrays.copyOfRange(i, 32, 64);
        Arrays.fill(i, (byte)0);
        DeterministicKey masterPrivKey = createMasterPrivKeyFromBytes(il, ir);
        Arrays.fill(il, (byte)0);
        Arrays.fill(ir, (byte)0);
        masterPrivKey.setCreationTimeSeconds(Utils.currentTimeSeconds());
        return masterPrivKey;
    }

    /**
     * Derive a Solana account from a Mnemonic generated by the Solana CLI using bip44 Mnemonic with deviation path of
     * m/55H/501H/0H 使用偏差路径为的bip44助记符，从Solana CLI生成的助记符派生Solana帐户
     * @param words seed words
     * @param passphrase seed passphrase
     * @return Solana account
     */
    public static Account fromBip44Mnemonic(List<String> words, String passphrase) {
        SolanaBip44 solanaBip44 = new SolanaBip44();
        byte[] seed = MnemonicCode.toSeed(words, passphrase);
        byte[] privateKey = solanaBip44.getPrivateKeyFromSeed(seed, BIP44);
        return new Account(TweetNaclFast.Signature.keyPair_fromSeed(privateKey));
    }

//    public static DeterministicKey createMasterPrivateKey(byte[] seed) throws HDDerivationException {
//        checkArgument(seed.length > 8, "Seed is too short and could be brute forced");
//        byte[] i = HDUtilss.hmacSha512(HDUtilss.createHmacSha512Digest("ed25519 seed".getBytes()), seed);
//        checkState(i.length == 64, i.length);
//        byte[] il = Arrays.copyOfRange(i, 0, 32);
//        byte[] ir = Arrays.copyOfRange(i, 32, 64);
//            Arrays.fill(i, (byte)0);
//        DeterministicKey masterPrivKey = createMasterPrivKeyFromBytes(il, ir);
//        Log.i("duan==wallet", "il=====" + bytesToHex(il));
//        Log.i("duan==wallet", "ir=====" + bytesToHex(ir));
//        Arrays.fill(il, (byte)0);
//        Arrays.fill(ir, (byte)0);
//        masterPrivKey.setCreationTimeSeconds(Utils.currentTimeSeconds());
//        return masterPrivKey;
//    }

    /**
     * Derive a Solana account from a Mnemonic generated by the Solana CLI using bip44 Mnemonic with deviation path of
     * m/55H/501H/0H/0H
     * @param words seed words
     * @param passphrase seed passphrase
     * @return Solana account
     */
    public static Account fromBip44MnemonicWithChange(List<String> words, String passphrase) {
        SolanaBip44 solanaBip44 = new SolanaBip44();
        byte[] seed = MnemonicCode.toSeed(words, passphrase);
        byte[] privateKey = solanaBip44.getPrivateKeyFromSeed(seed, DerivableType.BIP44CHANGE);
        return new Account(TweetNaclFast.Signature.keyPair_fromSeed(privateKey));
    }

    /**
     * Derive a Solana account from a Mnemonic generated by the Solana CLI
     * @param words seed words
     * @param passphrase seed passphrase
     * @return Solana account
     */
    public static Account fromBip39Mnemonic(List<String> words, String passphrase) {
        byte[] seed = MnemonicCode.toSeed(words, passphrase);
        TweetNaclFast.Signature.KeyPair keyPair = TweetNaclFast.Signature.keyPair_fromSeed(seed);

        return new Account(keyPair);
    }

    /**
     * Creates an {@link Account} object from a Sollet-exported JSON string (array)
     * @param json Sollet-exported JSON string (array)
     * @return {@link Account} built from Sollet-exported private key
     */
    public static Account fromJson(String json) {
        return new Account(convertJsonStringToByteArray(json));
    }

    public PublicKey getPublicKey() {
        return new PublicKey(keyPair.getPublicKey());
    }

    public byte[] getSecretKey() {
        return keyPair.getSecretKey();
    }

    /**
     * Convert's a Sollet-exported JSON string into a byte array usable for {@link Account} instantiation
     * @param characters Sollet-exported JSON string
     * @return byte array usable in {@link Account} instantiation
     */
    private static byte[] convertJsonStringToByteArray(String characters) {
        // Create resulting byte array
        final ByteBuffer buffer = ByteBuffer.allocate(64);

        // Convert json array into String array
        String sanitizedJson = characters.replaceAll("\\[", "").replaceAll("]", "");
        String[] chars = sanitizedJson.split(",");

        // Convert each String character into byte and put it in the buffer
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Arrays.stream(chars).forEach(new Consumer<String>() {
                @Override
                public void accept(String character) {
                    byte byteValue = (byte) Integer.parseInt(character);
                    buffer.put(byteValue);
                }
            });
        }

        return buffer.array();
    }
}