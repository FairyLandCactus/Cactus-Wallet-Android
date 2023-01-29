package com.qianlihu.solanawallet.wallet;

import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.List;

/**
 * author : Duan
 * date   : 2021/12/217:45
 * desc   : 生成助记词
 * version: 1.0.0
 */
public class CreateMnemoics {
    public static List<String> generateMnemonics(int bitSize) {
        List<String> mnemoniclist = null;
        byte bytes2[] = new byte[bitSize];
        SecureRandom random = new SecureRandom();
        random.nextBytes(bytes2);
        MnemonicCode mnemonicCode = null;
        try {
            mnemonicCode = new MnemonicCode();
            mnemoniclist = mnemonicCode.toMnemonic(bytes2);
        } catch (IOException | MnemonicException.MnemonicLengthException e) {
            e.printStackTrace();
        }
        return mnemoniclist;
    }

}
