package com.qianlihu.solanawallet.solanadapp;

import com.qianlihu.solanawallet.utils.wallet_utils.wallet.key.AccountMeta;
import com.qianlihu.solanawallet.utils.wallet_utils.wallet.key.Program;
import com.qianlihu.solanawallet.utils.wallet_utils.wallet.key.TransactionInstruction;
import com.qianlihu.solanawallet.wallet.PublicKey;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implements the "initialize" call from Anchor's basic-0 tutorial.
 */
public class AnchorBasicTutorialProgram extends Program {

    private static final PublicKey PROGRAM_ID = new PublicKey("F9wQScGsUk5UEmkHhUF4SqmvQgQ1e1VPrPN7h26NddSM"); //正式链合约地址
    private static final PublicKey PROGRAM_ID_TEST = new PublicKey("9AkxeVAEsJpicmbUqnM59gwZCBDK1qSLiXxuYRrnVFKg");//测试链合约地址

    //初始化流动池
    public static TransactionInstruction initializePool(PublicKey mint0,
                                                        PublicKey mint1,
                                                        PublicKey amm,
                                                        PublicKey ammFeeTo,
                                                        PublicKey pool,
                                                        PublicKey poolAuthority,
                                                        PublicKey vault0,
                                                        PublicKey vault1,
                                                        PublicKey poolMint,
                                                        PublicKey liquidityLocker,
                                                        PublicKey poolFeeTo,
                                                        PublicKey payer,
                                                        PublicKey systemProgram,
                                                        PublicKey tokenProgram,
                                                        PublicKey associatedTokenProgram,
                                                        PublicKey rent,
                                                        Long slip_molecule,
                                                        Long slip_denominator) {
        final List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(mint0, false, false));
        keys.add(new AccountMeta(mint1, false, false));
        keys.add(new AccountMeta(amm, false, false));
        keys.add(new AccountMeta(ammFeeTo, false, true));
        keys.add(new AccountMeta(pool, false, true));
        keys.add(new AccountMeta(poolAuthority, false, false));
        keys.add(new AccountMeta(vault0, false, true));
        keys.add(new AccountMeta(vault1, false, true));
        keys.add(new AccountMeta(poolMint, false, true));
        keys.add(new AccountMeta(liquidityLocker, false, true));
        keys.add(new AccountMeta(poolFeeTo, false, true));
        keys.add(new AccountMeta(payer, true, true));
        keys.add(new AccountMeta(systemProgram, false, false));
        keys.add(new AccountMeta(tokenProgram, false, false));
        keys.add(new AccountMeta(associatedTokenProgram, false, false));
        keys.add(new AccountMeta(rent, false, false));

        List<Integer> methodList = Arrays.asList(95, 180, 10, 172, 84, 174, 232, 40);
        byte[] transactionData = encodeAddLiquidityData(methodList, slip_molecule, slip_denominator);
        return createTransactionInstruction(PROGRAM_ID, keys, transactionData);
    }

    //初始化流动池
    public static TransactionInstruction initializePoolTest(PublicKey mint0,
                                                        PublicKey mint1,
                                                        PublicKey amm,
                                                        PublicKey ammFeeTo,
                                                        PublicKey pool,
                                                        PublicKey poolAuthority,
                                                        PublicKey vault0,
                                                        PublicKey vault1,
                                                        PublicKey poolMint,
                                                        PublicKey liquidityLocker,
                                                        PublicKey poolFeeTo,
                                                        PublicKey payer,
                                                        PublicKey systemProgram,
                                                        PublicKey tokenProgram,
                                                        PublicKey associatedTokenProgram,
                                                        PublicKey rent,
                                                        Long slip_molecule,
                                                        Long slip_denominator) {
        final List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(mint0, false, false));
        keys.add(new AccountMeta(mint1, false, false));
        keys.add(new AccountMeta(amm, false, false));
        keys.add(new AccountMeta(ammFeeTo, false, true));
        keys.add(new AccountMeta(pool, false, true));
        keys.add(new AccountMeta(poolAuthority, false, false));
        keys.add(new AccountMeta(vault0, false, true));
        keys.add(new AccountMeta(vault1, false, true));
        keys.add(new AccountMeta(poolMint, false, true));
        keys.add(new AccountMeta(liquidityLocker, false, true));
        keys.add(new AccountMeta(poolFeeTo, false, true));
        keys.add(new AccountMeta(payer, true, true));
        keys.add(new AccountMeta(systemProgram, false, false));
        keys.add(new AccountMeta(tokenProgram, false, false));
        keys.add(new AccountMeta(associatedTokenProgram, false, false));
        keys.add(new AccountMeta(rent, false, false));

        List<Integer> methodList = Arrays.asList(95, 180, 10, 172, 84, 174, 232, 40);
        byte[] transactionData = encodeAddLiquidityData(methodList, slip_molecule, slip_denominator);
        return createTransactionInstruction(PROGRAM_ID, keys, transactionData);
    }

    //添加流动池
    public static TransactionInstruction addLiquidity(PublicKey amm,
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
                                                      Long amount1,
                                                      Long amount2) {
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

        List<Integer> methodList = Arrays.asList(181, 157, 89, 67, 143, 182, 52, 72);
        byte[] transactionData = encodeAddLiquidityData(methodList, amount1,  amount2);
        return createTransactionInstruction(PROGRAM_ID, keys, transactionData);
    }

    //swap交易
    public static TransactionInstruction swapTrasfer(PublicKey amm,
                                                     PublicKey pool,
                                                     PublicKey poolAuthority,
                                                     PublicKey vaultSrc,
                                                     PublicKey vaultDst,
                                                     PublicKey userSrc,
                                                     PublicKey userDst,
                                                     PublicKey tokenProgram,
                                                     PublicKey owner,
                                                     long amount,
                                                     long slip,
                                                     long time) {
        final List<AccountMeta> keys = new ArrayList<>();

        keys.add(new AccountMeta(amm, false, false));
        keys.add(new AccountMeta(pool, false, true));
        keys.add(new AccountMeta(poolAuthority, false, true));
        keys.add(new AccountMeta(vaultSrc, false, true));
        keys.add(new AccountMeta(vaultDst, false, true));
        keys.add(new AccountMeta(userSrc, false, true));
        keys.add(new AccountMeta(userDst, false, true));
        keys.add(new AccountMeta(owner, true, false));
        keys.add(new AccountMeta(tokenProgram, false, false));

        List<Integer> methodList = Arrays.asList(248, 198, 158, 145, 225, 117, 135, 200);
        byte[] transactionData = encodeSwapTransferData(methodList, amount,  slip,  time);
        return createTransactionInstruction(PROGRAM_ID, keys, transactionData);
    }

    // 移除流动性
    public static TransactionInstruction removeLiquidity(PublicKey amm,
                                                         PublicKey pool,
                                                         PublicKey poolAuthority,
                                                         PublicKey vault0,
                                                         PublicKey vault1,
                                                         PublicKey poolMint,
                                                         PublicKey feeTo,
                                                         PublicKey user0,
                                                         PublicKey user1,
                                                         PublicKey userPoolAta,
                                                         PublicKey owner,
                                                         PublicKey tokenProgram,
                                                         long number) {
        final List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta(amm, false, false));
        keys.add(new AccountMeta(pool, false, true));
        keys.add(new AccountMeta(poolAuthority, false, false));
        keys.add(new AccountMeta(vault0, false, true));
        keys.add(new AccountMeta(vault1, false, true));
        keys.add(new AccountMeta(poolMint, false, true));
        keys.add(new AccountMeta(feeTo, false, true));
        keys.add(new AccountMeta(user0, false, true));
        keys.add(new AccountMeta(user1, false, true));
        keys.add(new AccountMeta(userPoolAta, false, true));
        keys.add(new AccountMeta(owner, true, false));
        keys.add(new AccountMeta(tokenProgram, false, false));

        List<Integer> methodList = Arrays.asList(80, 85, 209, 72, 24, 206, 177, 108);
        byte[] transactionData = encodeRemoveLiquidityData(methodList, number);
        return createTransactionInstruction(PROGRAM_ID, keys, transactionData);
    }

    public static TransactionInstruction tranferSwap(PublicKey  fromTokenAccount, PublicKey TOKEN_PROGRAM_ID) {
        final List<AccountMeta> keys = new ArrayList<>();
        keys.add(new AccountMeta( fromTokenAccount, false, true));
        List<Integer> numArr = Arrays.asList(17);
        byte[] transactionData = encodeAmount(numArr);
        return createTransactionInstruction(TOKEN_PROGRAM_ID, keys, transactionData);
    }

    private static byte[] encodeAddLiquidity(long number1, long number2) {
        ByteBuffer result = ByteBuffer.allocate(16);
        result.order(ByteOrder.LITTLE_ENDIAN);
        result.putLong(number1);
        result.putLong(number2);
        return result.array();
    }

    private static byte[] encodeSwap(Long amount, Long slip, Long time) {
        ByteBuffer result = ByteBuffer.allocate(24);
        result.order(ByteOrder.LITTLE_ENDIAN);
        result.putLong(amount);
        result.putLong(slip);
        result.putLong(time);
        return result.array();
    }

    private static byte[] encodeRemove(long number) {
        ByteBuffer result = ByteBuffer.allocate(8);
        result.order(ByteOrder.LITTLE_ENDIAN);
        result.putLong(number);
        return result.array();
    }

    private static byte[] encodeAmount(List<Integer> amount) {
        ByteBuffer result = ByteBuffer.allocate(amount.size());
        result.order(ByteOrder.LITTLE_ENDIAN);
        for (int i = 0; i < amount.size(); i++) {
            final int am = amount.get(i);
            result.put((byte) am);
        }
        return result.array();
    }

    private static byte[] encodeAddLiquidityData(List<Integer> methodList, Long num1, Long num2) {
        byte[] methodsByte = encodeAmount(methodList);
        byte[] amountByte1 = encodeAddLiquidity(num1, num2);
        byte[] merge = new byte[methodsByte.length + amountByte1.length];
        int len = 0;
        System.arraycopy(methodsByte, 0, merge, 0, methodsByte.length);
        len += methodsByte.length;
        System.arraycopy(amountByte1, 0, merge, len, amountByte1.length);
        return merge;
    }

    private static byte[] encodeSwapTransferData(List<Integer> methodList, Long amount, Long slip, Long time) {
        byte[] methodsByte = encodeAmount(methodList);
        byte[] amountByte1 = encodeSwap(amount, slip, time);
        byte[] merge = new byte[methodsByte.length + amountByte1.length];
        int len = 0;
        System.arraycopy(methodsByte, 0, merge, 0, methodsByte.length);
        len += methodsByte.length;
        System.arraycopy(amountByte1, 0, merge, len, amountByte1.length);
        return merge;
    }

    private static byte[] encodeRemoveLiquidityData(List<Integer> methodList, Long num) {
        byte[] methodsByte = encodeAmount(methodList);
        byte[] amountByte1 = encodeRemove(num);
        byte[] merge = new byte[methodsByte.length + amountByte1.length];
        int len = 0;
        System.arraycopy(methodsByte, 0, merge, 0, methodsByte.length);
        len += methodsByte.length;
        System.arraycopy(amountByte1, 0, merge, len, amountByte1.length);
        return merge;
    }

}
