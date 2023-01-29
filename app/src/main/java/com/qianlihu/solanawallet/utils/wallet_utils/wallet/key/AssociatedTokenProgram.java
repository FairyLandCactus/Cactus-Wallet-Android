package com.qianlihu.solanawallet.utils.wallet_utils.wallet.key;

import com.qianlihu.solanawallet.wallet.PublicKey;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * author : Duan
 * date   : 2021/12/3111:56
 * desc   :
 * version: 1.0.0
 */
public class AssociatedTokenProgram extends Program {

    public static final PublicKey SPL_ASSOCIATED_TOKEN_ACCOUNT_PROGRAM_ID = new PublicKey("ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL");
    public static final PublicKey OWNER_VALIDATION_PROGRAM_ID = new PublicKey("4MNPdKu9wFMvEeZBMt3Eipfs5ovVWTJb31pEXDJAAxX5");

    public static TransactionInstruction createAssociatedTokenAccountInstruction(PublicKey payer, PublicKey associatedAccount, PublicKey topuk, PublicKey tokenMint) {
        final List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(payer,true, true));
        keys.add(new AccountMeta(associatedAccount, false, true));
        keys.add(new AccountMeta(topuk,false, false));
        keys.add(new AccountMeta(tokenMint,false, false));
        keys.add(new AccountMeta(SystemProgram.PROGRAM_ID,false, false));
        keys.add(new AccountMeta(TokenProgram.PROGRAM_ID,false, false));
        keys.add(new AccountMeta(TokenProgram.SYSVAR_RENT_PUBKEY,false, false));

        ByteBuffer buffer = ByteBuffer.allocate(0);


        TransactionInstruction ti = createTransactionInstruction(
                SPL_ASSOCIATED_TOKEN_ACCOUNT_PROGRAM_ID,
                keys,
                buffer.array());
        return ti;
    }

    public static TransactionInstruction createAssociatedTokenAccountInstruction2(PublicKey payer, PublicKey associatedAccount, PublicKey topuk, PublicKey tokenMint) {
        final List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(payer,false, false));
        keys.add(new AccountMeta(associatedAccount, true, true));
        keys.add(new AccountMeta(topuk,true, true));
        keys.add(new AccountMeta(tokenMint,false, false));
        keys.add(new AccountMeta(SystemProgram.PROGRAM_ID,false, false));
        keys.add(new AccountMeta(TokenProgram.PROGRAM_ID,false, false));
        keys.add(new AccountMeta(TokenProgram.SYSVAR_RENT_PUBKEY,false, false));

        ByteBuffer buffer = ByteBuffer.allocate(0);


        TransactionInstruction ti = createTransactionInstruction(
                SPL_ASSOCIATED_TOKEN_ACCOUNT_PROGRAM_ID,
                keys,
                buffer.array());
        return ti;
    }

    public static TransactionInstruction assertOwner(PublicKey toPuk) {
        final List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(toPuk,false, false));

        TransactionInstruction ti = createTransactionInstruction(
                OWNER_VALIDATION_PROGRAM_ID,
                keys,
                encodeTransferCheckedTokenInstructionData());
        return ti;
    }

    private static byte[] encodeTransferCheckedTokenInstructionData() {
        ByteBuffer result = ByteBuffer.allocate(32);
//        result.put((byte) OWNER_VALIDATION_PROGRAM_ID.toString().length());
        return result.array();
    }

}
