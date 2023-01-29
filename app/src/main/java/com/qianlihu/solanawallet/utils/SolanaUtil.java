package com.qianlihu.solanawallet.utils;

import android.text.TextUtils;

import com.qianlihu.solanawallet.bean.DappSignDataBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.rpc.Cluster;
import com.qianlihu.solanawallet.rpc.RpcClient;
import com.qianlihu.solanawallet.rpc.RpcException;
import com.qianlihu.solanawallet.rpc.bean.AccountInfo;
import com.qianlihu.solanawallet.rpc.bean.SignaturesForAddressBean;
import com.qianlihu.solanawallet.rpc.bean.TokenAccountBalanceBean;
import com.qianlihu.solanawallet.rpc.bean.TokenAccountsByDelegateBean;
import com.qianlihu.solanawallet.rpc.bean.TransactionBean;
import com.qianlihu.solanawallet.solanadapp.AnchorBasicTutorialProgram;
import com.qianlihu.solanawallet.solanadapp.bean.AddLiquidityBean;
import com.qianlihu.solanawallet.solanadapp.bean.InitializePoolBean;
import com.qianlihu.solanawallet.solanadapp.bean.RemoveLiquidityBean;
import com.qianlihu.solanawallet.solanadapp.bean.SwapBean;
import com.qianlihu.solanawallet.utils.wallet_utils.AssociatedAccountUtil;
import com.qianlihu.solanawallet.utils.wallet_utils.wallet.key.AssociatedTokenProgram;
import com.qianlihu.solanawallet.utils.wallet_utils.wallet.key.MemoProgram;
import com.qianlihu.solanawallet.utils.wallet_utils.wallet.key.SystemProgram;
import com.qianlihu.solanawallet.utils.wallet_utils.wallet.key.TokenProgram;
import com.qianlihu.solanawallet.utils.wallet_utils.wallet.key.Transaction;
import com.qianlihu.solanawallet.wallet.Account;
import com.qianlihu.solanawallet.wallet.PublicKey;

import org.bitcoinj.core.Base58;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

public class SolanaUtil {

    //获取账户信息
    public static AccountInfo getAccountInfo(String puk, String encoding) throws RpcException {
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        AccountInfo info = client.getApi().getAccountInfo(puk, encoding);
        return info;
    }

    //获取余额
    public static long balanc(String puk) throws RpcException {
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        long result = client.getApi().getBalance(puk);
        return result;
    }

    //solana转账
    public static String transferSol(String fromPublicKey, String toPublickKey, String amount, String signerBase58String, String memo) throws RpcException {
        long decimal = 1000000000L;
        long lamport = (long) (Double.parseDouble(amount) * decimal);
        PublicKey from = new PublicKey(fromPublicKey);
        PublicKey to = new PublicKey(toPublickKey);
        Account signer = new Account(Base58.decode(signerBase58String));
        Transaction transaction = new Transaction();
        transaction.addInstruction(SystemProgram.transfer(from, to, lamport));
        if (!TextUtils.isEmpty(memo)) {
            transaction.addInstruction(MemoProgram.writeUtf8(from, memo));
        }
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        String result = client.getApi().sendTransaction(transaction, signer, 3);
        return result;
    }

    //solana代币转账
    public static String sendSPLTokens(String fromPublicKey, String toPublickKey, String mint, String owner, String myAssTokenAddress, String assTokenAddress, int decimal, String amount, String signerBase58String, boolean isCreatAcount) throws RpcException {
        double pow = Math.pow(10, decimal);
        long lamport = (long) (Double.parseDouble(amount) * pow);
        PublicKey from = new PublicKey(fromPublicKey);
        PublicKey to = new PublicKey(toPublickKey);
        PublicKey mintP = new PublicKey(mint);
        PublicKey ownerP = new PublicKey(owner);
        Account signer = new Account(Base58.decode(signerBase58String));
        PublicKey assTokenAddressP = new PublicKey(assTokenAddress);
        PublicKey myAssTokenAddressP = new PublicKey(myAssTokenAddress);
        Transaction transaction = new Transaction();
        if (isCreatAcount) {
            transaction.addInstruction(AssociatedTokenProgram.assertOwner(to));
            transaction.addInstruction(AssociatedTokenProgram.createAssociatedTokenAccountInstruction(from, assTokenAddressP, to, mintP));
        }
        transaction.addInstruction(TokenProgram.transfer(myAssTokenAddressP, assTokenAddressP, lamport, ownerP));
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        String result = client.getApi().sendTransactionToken(transaction, Arrays.asList(signer), 3);
        return result;
    }

    //关闭wsol
    public static String createCloseAccountInstruction(String tmpAccount, String ALICE1, String ALICE2, String signerBase58String) throws RpcException {
        PublicKey account = new PublicKey(tmpAccount);
        PublicKey dest = new PublicKey(ALICE1);
        PublicKey owner = new PublicKey(ALICE2);
        Account signer = new Account(Base58.decode(signerBase58String));
        Transaction transaction = new Transaction();

        transaction.addInstruction(TokenProgram.closeAccount(account, dest, owner));
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        String result = client.getApi().sendTransactionToken(transaction, Arrays.asList(signer), 3);
        return result;
    }

    //dapp代币兑换签名
    public static String sendSPLTokensSign(String fromPublicKey, String toPublickKey, String mint, String assTokenAddress, String signerBase58String, boolean isCreatAcount, DappSignDataBean bean) throws RpcException {
        PublicKey from = new PublicKey(fromPublicKey);
        PublicKey to = new PublicKey(toPublickKey);
        PublicKey mintP = new PublicKey(mint);
        PublicKey assTokenAddressP = new PublicKey(assTokenAddress);
        Transaction transaction = new Transaction();
        if (isCreatAcount) {
            transaction.addInstruction(AssociatedTokenProgram.assertOwner(from));
            transaction.addInstruction(AssociatedTokenProgram.createAssociatedTokenAccountInstruction(from, assTokenAddressP, from, mintP));
        }
        PublicKey takerAccount = new PublicKey(bean.getAccountAddress());
        PublicKey takerYToken = new PublicKey(bean.getAssociatedAddressY());
        PublicKey takerXToken = new PublicKey(bean.getAssociatedAddressX());
        PublicKey XTokenTemp = new PublicKey(bean.getXTokenTempAccountPubkey());
        PublicKey initializer = new PublicKey(bean.getInitializerAccountPubkey());
        PublicKey initializerY = new PublicKey(bean.getInitializerYTokenAccount());
        PublicKey escrow = new PublicKey(bean.getEscrowAccountPubkey());
        PublicKey PDA = new PublicKey(bean.getPad());
        PublicKey TOKEN_PROGRAM_ID = new PublicKey(bean.getTOKEN_PROGRAM_ID());
        PublicKey programId = new PublicKey(bean.getProgramId());
        List<Integer> amount = bean.getData().getData();
        transaction.addInstruction(TokenProgram.exchange(
                takerAccount,
                takerYToken,
                takerXToken,
                XTokenTemp,
                initializer,
                initializerY,
                escrow,
                PDA,
                TOKEN_PROGRAM_ID,
                programId,
                amount));
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        Account signer = new Account(Base58.decode(signerBase58String));
        String result = client.getApi().sendTransactionToken2(transaction, Arrays.asList(signer), 3);
        return result;
    }

    //初始化流动池签名
    public static String initializePoolSign(String signerBase58String, InitializePoolBean bean) throws RpcException {
        Transaction transaction = new Transaction();
        PublicKey mint0 = new PublicKey(bean.getMint0());
        PublicKey mint1 = new PublicKey(bean.getMint1());
        PublicKey amm = new PublicKey(bean.getAmm());
        PublicKey ammFeeTo = new PublicKey(bean.getAmmFeeTo());
        PublicKey pool = new PublicKey(bean.getPool());
        PublicKey poolAuthority = new PublicKey(bean.getPoolAuthority());
        PublicKey vault0 = new PublicKey(bean.getVault0());
        PublicKey vault1 = new PublicKey(bean.getVault1());
        PublicKey poolMint = new PublicKey(bean.getPoolMint());
        PublicKey liquidityLocker = new PublicKey(bean.getLiquidityLocker());
        PublicKey poolFeeTo = new PublicKey(bean.getPoolFeeTo());
        PublicKey payer = new PublicKey(bean.getPayer());
        PublicKey systemProgram = new PublicKey(bean.getSystemProgram());
        PublicKey tokenProgram = new PublicKey(bean.getTokenProgram());
        PublicKey associatedTokenProgram = new PublicKey(bean.getAssociatedTokenProgram());
        PublicKey rent = new PublicKey(bean.getRent());
        Long slip_molecule = Long.parseLong(bean.getSlip_molecule(), 16);
        Long slip_denominator = Long.parseLong(bean.getSlip_denominator(), 16);
        transaction.addInstruction(AnchorBasicTutorialProgram.initializePool(
                mint0,
                mint1,
                amm,
                ammFeeTo,
                pool,
                poolAuthority,
                vault0,
                vault1,
                poolMint,
                liquidityLocker,
                poolFeeTo,
                payer,
                systemProgram,
                tokenProgram,
                associatedTokenProgram,
                rent,
                slip_molecule, slip_denominator));
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        Account signer = new Account(Base58.decode(signerBase58String));
        String result = client.getApi().sendTransactionToken2(transaction, Arrays.asList(signer), 3);
        return result;
    }

    //添加流动池签名
    public static String addLiquiditySign(String fromPublicKey, String signerBase58String, AddLiquidityBean bean) throws RpcException {
        Account signer = new Account(Base58.decode(signerBase58String));
        Transaction transaction = new Transaction();

        if (bean.getSolana().getUser() != null) {
            PublicKey from = new PublicKey(fromPublicKey);
            PublicKey to = new PublicKey(bean.getSolana().getUser());
            PublicKey  fromTokenAccount = new PublicKey(bean.getSolana().getUser());
            PublicKey TOKEN_PROGRAM_ID = new PublicKey(Constant.SOL_TOKEN_PROGRAM);
            transaction.addInstruction(SystemProgram.transfer(from, to, bean.getSolana().getNumber()));
            transaction.addInstruction(AnchorBasicTutorialProgram.tranferSwap(fromTokenAccount, TOKEN_PROGRAM_ID));
        }

        PublicKey amm = new PublicKey(bean.getAmm());
        PublicKey pool = new PublicKey(bean.getPool());
        PublicKey poolAuthority = new PublicKey(bean.getPoolAuthority());
        PublicKey vault0 = new PublicKey(bean.getVault0());
        PublicKey vault1 = new PublicKey(bean.getVault1());
        PublicKey poolMint = new PublicKey(bean.getPoolMint());
        PublicKey liquidityLocker = new PublicKey(bean.getLiquidityLocker());
        PublicKey feeTo = new PublicKey(bean.getFeeTo());
        PublicKey user0 = new PublicKey(bean.getUser0());
        PublicKey user1 = new PublicKey(bean.getUser1());
        PublicKey userPoolAta = new PublicKey(bean.getUserPoolAta());
        PublicKey owner = new PublicKey(bean.getOwner());
        PublicKey tokenProgram = new PublicKey(bean.getTokenProgram());
        Long amount1 = Long.parseLong(bean.getNumber1(), 16);
        Long amount2 = Long.parseLong(bean.getNumber2(), 16);
        transaction.addInstruction(AnchorBasicTutorialProgram.addLiquidity(
                amm,
                pool,
                poolAuthority,
                vault0,
                vault1,
                poolMint,
                liquidityLocker,
                feeTo,
                user0,
                user1,
                userPoolAta,
                owner,
                tokenProgram,
                amount1,
                amount2));
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        String result = client.getApi().sendTransactionToken2(transaction, Arrays.asList(signer), 3);
        return result;
    }

    //swap交易签名
    public static String swapTransferSign(String fromPublicKey, String signerBase58String, SwapBean bean) throws RpcException {
        Transaction transaction = new Transaction();
        PublicKey from = new PublicKey(fromPublicKey);
        if (bean.getSolana().getLood_mint_ata() != null) {
            PublicKey to = new PublicKey(bean.getSolana().getLood_mint_ata());
            PublicKey  fromTokenAccount = new PublicKey(bean.getSolana().getLood_mint_ata());
            PublicKey TOKEN_PROGRAM_ID = new PublicKey(Constant.SOL_TOKEN_PROGRAM);
            transaction.addInstruction(SystemProgram.transfer(from, to, bean.getSolana().getSwap_amount()));
            transaction.addInstruction(AnchorBasicTutorialProgram.tranferSwap(fromTokenAccount, TOKEN_PROGRAM_ID));
        }
        PublicKey amm = new PublicKey(bean.getAmm());
        PublicKey pool = new PublicKey(bean.getPool());
        PublicKey poolAuthority = new PublicKey(bean.getPoolAuthority());
        PublicKey vaultSrc = new PublicKey(bean.getVaultSrc());
        PublicKey vaultDst = new PublicKey(bean.getVaultDst());
        PublicKey userSrc = new PublicKey(bean.getUserSrc());
        PublicKey userDst = new PublicKey(bean.getUserDst());
        PublicKey owner = new PublicKey(bean.getOwner());
        PublicKey tokenProgram = new PublicKey(bean.getTokenProgram());
        transaction.addInstruction(AnchorBasicTutorialProgram.swapTrasfer(
                amm,
                pool,
                poolAuthority,
                vaultSrc,
                vaultDst,
                userSrc,
                userDst,
                tokenProgram,
                owner,
                Long.parseLong(bean.getAmount(), 16),
                Long.parseLong(bean.getSlip(), 16),
                Long.parseLong(bean.getTime(), 16)));
        if (bean.getTrade().getFee_address() != null) {
            long lamport = bean.getTrade().getFee_number();
            String mTokenAddress = bean.getTrade().getFee_coin();
            String mySplTokenAddress = null;
            String associatedTokenAddress = null;
            try {
                mySplTokenAddress = AssociatedAccountUtil.createAssociatedAccountAddress(fromPublicKey, mTokenAddress);
                associatedTokenAddress = AssociatedAccountUtil.createAssociatedAccountAddress(bean.getTrade().getFee_address(), mTokenAddress);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            PublicKey assTokenAddressP = new PublicKey(associatedTokenAddress);
            PublicKey myAssTokenAddressP = new PublicKey(mySplTokenAddress);
            transaction.addInstruction(TokenProgram.transfer(myAssTokenAddressP, assTokenAddressP, lamport, from));
        }
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        Account signer = new Account(Base58.decode(signerBase58String));
        String result = client.getApi().sendTransactionToken2(transaction, Arrays.asList(signer), 3);
        return result;
    }

    //移除流动池
    public static String removeLiquiditySign(String signerBase58String, RemoveLiquidityBean bean) throws RpcException {
        Account signer = new Account(Base58.decode(signerBase58String));
        Transaction transaction = new Transaction();
        PublicKey amm = new PublicKey(bean.getAmm());
        PublicKey pool = new PublicKey(bean.getPool());
        PublicKey poolAuthority = new PublicKey(bean.getPoolAuthority());
        PublicKey vault0 = new PublicKey(bean.getVault0());
        PublicKey vault1 = new PublicKey(bean.getVault1());
        PublicKey poolMint = new PublicKey(bean.getPoolMint());
        PublicKey feeTo = new PublicKey(bean.getFeeTo());
        PublicKey user0 = new PublicKey(bean.getUser0());
        PublicKey user1 = new PublicKey(bean.getUser1());
        PublicKey userPoolAta = new PublicKey(bean.getUserPoolAta());
        PublicKey owner = new PublicKey(bean.getOwner());
        PublicKey tokenProgram = new PublicKey(bean.getTokenProgram());
        Long amount1 = Long.parseLong(bean.getNumber(), 16);
        transaction.addInstruction(AnchorBasicTutorialProgram.removeLiquidity(
                amm,
                pool,
                poolAuthority,
                vault0,
                vault1,
                poolMint,
                feeTo,
                user0,
                user1,
                userPoolAta,
                owner,
                tokenProgram,
                amount1));
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        String result = client.getApi().sendTransactionToken2(transaction, Arrays.asList(signer), 3);
        return result;
    }

    public static String transferSwap(String fromPublicKey, String toPublickKey, String amount, String signerBase58String) throws RpcException {
        long decimal = 1000000000L;
//        long lamport = (long) (Double.parseDouble(amount) * decimal);
        long lamport = 100000000l;
        PublicKey from = new PublicKey(fromPublicKey);
        PublicKey to = new PublicKey(toPublickKey);
        PublicKey  fromTokenAccount = new PublicKey(toPublickKey);
        PublicKey TOKEN_PROGRAM_ID = new PublicKey(Constant.SOL_TOKEN_PROGRAM);
        Account signer = new Account(Base58.decode(signerBase58String));
        Transaction transaction = new Transaction();
        transaction.addInstruction(SystemProgram.transfer(from, to, lamport));
        transaction.addInstruction(AnchorBasicTutorialProgram.tranferSwap(fromTokenAccount, TOKEN_PROGRAM_ID));
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        String result = client.getApi().sendTransaction(transaction, signer, 3);
        return result;
    }

    public static Long getMinimumBalanceForRentExemption(Long dataLength) throws RpcException {
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        Long result = client.getApi().getMinimumBalanceForRentExemption(dataLength);
        return result;
    }

    //获取转账记录哈希
    public static List<SignaturesForAddressBean> getSignaturesForAddress(String puk) throws RpcException {
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        List<SignaturesForAddressBean> result = client.getApi().getSignaturesForAddress(puk);
        return result;
    }

    //获取转账记录详情
    public static TransactionBean getTransaction(String key) throws RpcException {
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        TransactionBean result = client.getApi().getTransaction(key);
        return result;
    }

    public static TokenAccountsByDelegateBean getTokenAccountsByDelegate(String puk, String mint) throws RpcException {
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        TokenAccountsByDelegateBean result = client.getApi().getTokenAccountsByDelegate(puk, mint);
        return result;
    }

    public static TokenAccountBalanceBean getTokenAccountBalance(String tokenPuk) throws RpcException {
        final RpcClient client = new RpcClient(Cluster.MAINNET);
        TokenAccountBalanceBean result = client.getApi().getTokenAccountBalance(tokenPuk);
        return result;
    }

}
