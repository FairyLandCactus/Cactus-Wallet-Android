package com.qianlihu.solanawallet.utils.wallet_utils.wallet.key;


import android.util.Log;

import com.qianlihu.solanawallet.utils.wallet_utils.AssociatedAccountUtil;
import com.qianlihu.solanawallet.wallet.PublicKey;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for creating Token Program {@link TransactionInstruction}s
 */
public class TokenProgram extends Program {

    public static final PublicKey PROGRAM_ID = new PublicKey("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA");
    public static final PublicKey systemProgramId = new PublicKey("11111111111111111111111111111111");
    public static final PublicKey SYSVAR_RENT_PUBKEY = new PublicKey("SysvarRent111111111111111111111111111111111");
    private static final PublicKey ASSOCIATED_TOKEN_PROGRAM_ID = new PublicKey("ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL");
    private static final PublicKey OWNER_VALIDATION_PROGRAM_ID = new PublicKey("4MNPdKu9wFMvEeZBMt3Eipfs5ovVWTJb31pEXDJAAxX5");

    private static final int INITIALIZE_METHOD_ID = 1;
    private static final int TRANSFER_METHOD_ID = 3;
    private static final int CLOSE_ACCOUNT_METHOD_ID = 9;
    private static final int TRANSFER_CHECKED_METHOD_ID = 12;

    /**
     * Transfers an SPL token from the owner's source account to destination account.
     * Destination pubkey must be the Token Account (created by token mint), and not the main SOL address.
     *
     * @param source      SPL token wallet funding this transaction
     * @param destination Destined SPL token wallet
     * @param amount      64 bit amount of tokens to send
     * @param owner       account/private key signing this transaction
     * @return transaction id for explorer
     */
    public static TransactionInstruction transfer(PublicKey source, PublicKey destination, long amount, PublicKey owner) {
        final List<AccountMeta> keys = new ArrayList<>();

        keys.add(new AccountMeta(source, false, true));
        keys.add(new AccountMeta(destination, false, true));
        keys.add(new AccountMeta(owner, true, false));
        byte[] transactionData = encodeTransferTokenInstructionData(amount);

        return createTransactionInstruction(PROGRAM_ID, keys, transactionData);
    }

    public static TransactionInstruction exchange(PublicKey takerAccount,
                                                  PublicKey takerYToken,
                                                  PublicKey takerXToken,
                                                  PublicKey XTokenTemp,
                                                  PublicKey initializer,
                                                  PublicKey initializerY,
                                                  PublicKey escrow,
                                                  PublicKey PDA,
                                                  PublicKey TOKEN_PROGRAM_ID,
                                                  PublicKey programId,
                                                  List<Integer> amount) {
        final List<AccountMeta> keys = new ArrayList<>();

        keys.add(new AccountMeta(takerAccount, true, false));
        keys.add(new AccountMeta(takerYToken, false, true));
        keys.add(new AccountMeta(takerXToken, false, true));
        keys.add(new AccountMeta(XTokenTemp, false, true));
        keys.add(new AccountMeta(initializer, false, true));
        keys.add(new AccountMeta(initializerY, false, true));
        keys.add(new AccountMeta(escrow, false, true));
        keys.add(new AccountMeta(TOKEN_PROGRAM_ID, false, false));
        keys.add(new AccountMeta(PDA, false, false));
        byte[] transactionData = encodeTransferTokenInstructionData2(amount);
        Log.i("duan==tran", "ttttttt   " + Arrays.toString(transactionData));
        return createTransactionInstruction(programId, keys, transactionData);
    }

    public static TransactionInstruction exchange2(PublicKey amm,
                                                   PublicKey pool,
                                                   PublicKey poolAuthority,
                                                   PublicKey vault0,
                                                   PublicKey vault1,
                                                   PublicKey poolMint,
                                                   PublicKey liquidityLocker,
                                                   PublicKey feeTo,
                                                   PublicKey user0,
                                                   PublicKey user1,
                                                   PublicKey userPoolAta,
                                                   PublicKey owner,
                                                   PublicKey tokenProgram,
                                                   PublicKey programId,
                                                   long amount1,
                                                   byte decimals1,
                                                   long amount2,
                                                   byte decimals2) {
        final List<AccountMeta> keys = new ArrayList<>();

        keys.add(new AccountMeta(amm, false, false));
        keys.add(new AccountMeta(pool, false, true));
        keys.add(new AccountMeta(poolAuthority, false, false));
        keys.add(new AccountMeta(vault0, false, true));
        keys.add(new AccountMeta(vault1, false, true));
        keys.add(new AccountMeta(poolMint, false, true));
        keys.add(new AccountMeta(liquidityLocker, false, true));
        keys.add(new AccountMeta(feeTo, false, true));
        keys.add(new AccountMeta(user0, false, true));
        keys.add(new AccountMeta(user1, false, true));
        keys.add(new AccountMeta(userPoolAta, false, true));
        keys.add(new AccountMeta(owner, true, false));
        keys.add(new AccountMeta(tokenProgram, false, false));
        byte[] transactionData = encodeTransferTokenInstructionData3(amount1, decimals1, amount2, decimals2);
        Log.i("duan==tran", "ttttttt   " + Arrays.toString(transactionData));
        return createTransactionInstruction(programId, keys, transactionData);
    }

    public static TransactionInstruction transferChecked(PublicKey source, PublicKey destination, PublicKey owner, PublicKey tokenMint, long amount, byte decimals) {
        final List<AccountMeta> keys = new ArrayList<>();

        keys.add(new AccountMeta(source, false, true));
        // index 1 = token mint (https://docs.rs/spl-token/3.1.0/spl_token/instruction/enum.TokenInstruction.html#variant.TransferChecked)
        keys.add(new AccountMeta(tokenMint, false, false));
        keys.add(new AccountMeta(destination, false, true));
        keys.add(new AccountMeta(owner, true, false));

        byte[] transactionData = encodeTransferCheckedTokenInstructionData(
                amount,
                decimals
        );

        return createTransactionInstruction(
                PROGRAM_ID,
                keys,
                transactionData
        );
    }

    public static TransactionInstruction initializeAccount(final PublicKey account, final PublicKey mint, final PublicKey owner) {
        final List<AccountMeta> keys = new ArrayList<>();

        keys.add(new AccountMeta(account, false, true));
        keys.add(new AccountMeta(mint, false, false));
        keys.add(new AccountMeta(owner, false, true));
        keys.add(new AccountMeta(SYSVAR_RENT_PUBKEY, false, false));

        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) INITIALIZE_METHOD_ID);

        return createTransactionInstruction(
                PROGRAM_ID,
                keys,
                buffer.array()
        );
    }

    public static TransactionInstruction closeAccount(final PublicKey account, final PublicKey dest, final PublicKey owner) {
        final List<AccountMeta> keys = new ArrayList<>();

        keys.add(new AccountMeta(account, false, true));
        keys.add(new AccountMeta(dest, false, true));
        keys.add(new AccountMeta(owner, true, false));

        ByteBuffer buffer = ByteBuffer.allocate(1);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put((byte) CLOSE_ACCOUNT_METHOD_ID);

        return createTransactionInstruction(
                PROGRAM_ID,
                keys,
                buffer.array()
        );
    }

    private static byte[] encodeTransferTokenInstructionData(long amount) {
        ByteBuffer result = ByteBuffer.allocate(9);
        result.order(ByteOrder.LITTLE_ENDIAN);

        result.put((byte) TRANSFER_METHOD_ID);
        result.putLong(amount);

        return result.array();
    }

    private static byte[] encodeTransferTokenInstructionData2(List<Integer> amount) {
        ByteBuffer result = ByteBuffer.allocate(9);
        result.order(ByteOrder.LITTLE_ENDIAN);

//        result.put((byte) TRANSFER_METHOD_ID);
        for (int i = 0; i < amount.size(); i++) {
            final int am = amount.get(i);
            result.put((byte) am);
        }

        return result.array();
    }

    private static byte[] encodeTransferTokenInstructionData3(long amount1, byte decimals1, long amount2, byte decimals2) {
        ByteBuffer result = ByteBuffer.allocate(18);
        result.order(ByteOrder.LITTLE_ENDIAN);

//        result.put((byte) TRANSFER_CHECKED_METHOD_ID);
        result.putLong(amount1);
        result.put(decimals1);
        result.putLong(amount2);
        result.put(decimals2);

        return result.array();
    }

    private static byte[] encodeTransferCheckedTokenInstructionData(long amount, byte decimals) {
        ByteBuffer result = ByteBuffer.allocate(10);
        result.order(ByteOrder.LITTLE_ENDIAN);

        result.put((byte) TRANSFER_CHECKED_METHOD_ID);
        result.putLong(amount);
        result.put(decimals);

        return result.array();
    }
}
