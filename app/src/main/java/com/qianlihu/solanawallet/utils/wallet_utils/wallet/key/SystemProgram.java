package com.qianlihu.solanawallet.utils.wallet_utils.wallet.key;

import com.qianlihu.solanawallet.wallet.PublicKey;

import java.util.ArrayList;

import static org.bitcoinj.core.Utils.uint32ToByteArrayLE;

public class SystemProgram extends Program {
    public static final PublicKey PROGRAM_ID = new PublicKey("11111111111111111111111111111111");

    public static final int PROGRAM_INDEX_CREATE_ACCOUNT = 0;
    public static final int PROGRAM_INDEX_TRANSFER = 2;

    public static TransactionInstruction transfer(PublicKey fromPublicKey, PublicKey toPublickKey, long lamports) {
        ArrayList<AccountMeta> keys = new ArrayList<AccountMeta>();
        keys.add(new AccountMeta(fromPublicKey, true, true));
        keys.add(new AccountMeta(toPublickKey, false, true));

        // 4 byte instruction index + 8 bytes lamports
        byte[] data = new byte[4 + 8];
        uint32ToByteArrayLE(PROGRAM_INDEX_TRANSFER, data, 0);
        uint64ToByteArrayLE(lamports, data, 4);

        return createTransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static TransactionInstruction createAccount(PublicKey fromPublicKey, PublicKey newAccountPublikkey,
                                                       long lamports, long space, PublicKey programId) {
        ArrayList<AccountMeta> keys = new ArrayList<AccountMeta>();
        keys.add(new AccountMeta(fromPublicKey, true, true));
        keys.add(new AccountMeta(newAccountPublikkey, true, true));

        byte[] data = new byte[4 + 8 + 8 + 32];
        uint32ToByteArrayLE(PROGRAM_INDEX_CREATE_ACCOUNT, data, 0);
        uint64ToByteArrayLE(lamports, data, 4);
        uint64ToByteArrayLE(space, data, 12);
        System.arraycopy(programId.toByteArray(), 0, data, 20, 32);

        return createTransactionInstruction(PROGRAM_ID, keys, data);
    }

    public static void uint64ToByteArrayLE(long val, byte[] out, int offset) {
        out[offset] = (byte) (0xFF & val);
        out[offset + 1] = (byte) (0xFF & (val >> 8));
        out[offset + 2] = (byte) (0xFF & (val >> 16));
        out[offset + 3] = (byte) (0xFF & (val >> 24));
        out[offset + 4] = (byte) (0xFF & (val >> 32));
        out[offset + 5] = (byte) (0xFF & (val >> 40));
        out[offset + 6] = (byte) (0xFF & (val >> 48));
        out[offset + 7] = (byte) (0xFF & (val >> 56));
    }
}
