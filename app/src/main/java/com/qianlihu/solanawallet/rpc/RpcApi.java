package com.qianlihu.solanawallet.rpc;

import android.util.Log;

import com.qianlihu.solanawallet.rpc.bean.AccountInfo;
import com.qianlihu.solanawallet.rpc.bean.FeesBean;
import com.qianlihu.solanawallet.rpc.bean.RecentBlockhash;
import com.qianlihu.solanawallet.rpc.bean.SignaturesForAddressBean;
import com.qianlihu.solanawallet.rpc.bean.TokenAccountBalanceBean;
import com.qianlihu.solanawallet.rpc.bean.TokenAccountsByDelegateBean;
import com.qianlihu.solanawallet.rpc.bean.TransactionBean;
import com.qianlihu.solanawallet.rpc.type.RpcMintConfig;
import com.qianlihu.solanawallet.rpc.type.RpcResultTypes;
import com.qianlihu.solanawallet.rpc.type.RpcSendTransactionConfig;
import com.qianlihu.solanawallet.rpc.type.RpcSendTransactionConfig2;
import com.qianlihu.solanawallet.utils.wallet_utils.Base58;
import com.qianlihu.solanawallet.utils.wallet_utils.wallet.key.Transaction;
import com.qianlihu.solanawallet.wallet.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RpcApi {
    private RpcClient client;

    public RpcApi(RpcClient client) {
        this.client = client;
    }

    public long getBalance(String puk) throws RpcException {
        List<Object> params = new ArrayList<Object>();
        params.add(puk);
        return client.call("getBalance", params, RpcResultTypes.ValueLong.class).getValue();
    }

    public String getRecentBlockhash() throws RpcException {
        return client.call("getRecentBlockhash", null, RecentBlockhash.class).getRecentBlockhash();
    }

    public AccountInfo getAccountInfo(String account, String encoding) throws RpcException {
        List<Object> params = new ArrayList<Object>();
        Map<String, String> map = new HashMap<>();
        map.put("encoding", encoding);
        params.add(account);
        params.add(map);
        return client.call("getAccountInfo", params, AccountInfo.class);
    }

    public String sendTransaction(Transaction transaction, Account signer, int flag) throws RpcException {
        return sendTransaction(transaction, Arrays.asList(signer),flag);
    }

    public String sendTransaction(Transaction transaction, List<Account> signers, int flag) throws RpcException {
        String recentBlockhash = getRecentBlockhash();
        transaction.setRecentBlockHash(recentBlockhash);
        transaction.sign(signers,flag);
        byte[] serializedTransaction = transaction.serialize();
        String base58Trx = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            base58Trx = Base64.getEncoder().encodeToString(serializedTransaction);
            base58Trx = Base58.encode(serializedTransaction);
        }
        List<Object> params = new ArrayList<Object>();
        params.add(base58Trx);
        params.add(new RpcSendTransactionConfig());
        Log.i("duan==wallet", "请求数据==  " + params);
        return client.call("sendTransaction", params, String.class);
    }

    public String sendTransactionToken(Transaction transaction, List<Account> signers, int flag) throws RpcException {
        String recentBlockhash = getRecentBlockhash();
        transaction.setRecentBlockHash(recentBlockhash);
        transaction.sign(signers,flag);
        byte[] serializedTransaction = transaction.serialize();
        String base58Trx = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            base58Trx = Base64.getEncoder().encodeToString(serializedTransaction);
//            base58Trx = Base58.encode(serializedTransaction);
        }
        Log.i("duan==wallet", "加密数据长==  " + base58Trx.length());
        List<Object> params = new ArrayList<Object>();
        Map<String, String> map = new HashMap<>();
        map.put("encoding", "base64");
        params.add(base58Trx);
        params.add(map);
        Log.i("duan==wallet", "请求数据==  " + params);
        return client.call("sendTransaction", params, String.class);
    }

    public String sendTransactionToken2(Transaction transaction, List<Account> signers, int flag) throws RpcException {
        String recentBlockhash = getRecentBlockhash();
        transaction.setRecentBlockHash(recentBlockhash);
        transaction.sign(signers,flag);
        byte[] serializedTransaction = transaction.serialize();
        String base58Trx = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            base58Trx = Base64.getEncoder().encodeToString(serializedTransaction);
//            base58Trx = Base58.encode(serializedTransaction);
        }
        Log.i("duan==wallet", "加密数据长==  " + base58Trx.length());
        List<Object> params = new ArrayList<Object>();
        Map<String, Object> map = new HashMap<>();
        map.put("encoding", "base64");
        map.put("preflightCommitment", "finalized");
        map.put("skipPreflight", false);
        params.add(base58Trx);
        params.add(map);
        Log.i("duan==wallet", "请求数据==  " + params);
        return client.call("sendTransaction", params, String.class);
    }

    public String sendTransactionToken3(Transaction transaction, List<Account> signers, int flag) throws RpcException {
        String recentBlockhash = getRecentBlockhash();
        transaction.setRecentBlockHash(recentBlockhash);
        transaction.sign(signers,flag);
        byte[] serializedTransaction = transaction.serialize();
        String base58Trx = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            base58Trx = Base64.getEncoder().encodeToString(serializedTransaction);
//            base58Trx = Base58.encode(serializedTransaction);
        }
        Log.i("duan==wallet", "加密数据长==  " + base58Trx.length());
        List<Object> params = new ArrayList<Object>();
        Map<String, Object> map = new HashMap<>();
        map.put("encoding", "base64");
        map.put("preflightCommitment", "finalized");
        map.put("skipPreflight", false);
        params.add(base58Trx);
        params.add(map);
        Log.i("duan==wallet", "请求数据==  " + params);
        return client.call("sendTransaction", params, String.class);
    }


    public FeesBean getFees() throws RpcException {
        return client.call("getFees", null, FeesBean.class);
    }

    public Long getMinimumBalanceForRentExemption(Long dataLength) throws RpcException {
        List<Object> params = new ArrayList<Object>();
        params.add(dataLength);
        return client.call("getMinimumBalanceForRentExemption", params, Long.class);
    }

    //获取已交易签名
    public List<SignaturesForAddressBean> getSignaturesForAddress(String puk) throws RpcException {
        List<Object> params = new ArrayList<Object>();
        params.add(puk);
        Map<String, Integer> map = new HashMap<>();
//        map.put("limit", 30);
//        map.put("before", "fWqWMjuaJoKRhf2fBZuwu5eC5eBNbsij4R3r5WugVHbc8JmMRsUKXZMyT2tw5W9cJzYukggKdcLvCKA9AzRKs8w");
//        params.add(map);
        Log.i("duan==params", "提交数据== " + params);
        return client.call("getSignaturesForAddress", params, List.class);
    }

    //获取交易记录
    public TransactionBean getTransaction(String transgerSign) throws RpcException {
        List<Object> params = new ArrayList<>();
        params.add(transgerSign);
        params.add("json");
        Log.i("duan==params", "提交数据== " + params);
        return client.call("getTransaction", params, TransactionBean.class);
    }

    public TokenAccountsByDelegateBean getTokenAccountsByDelegate(String puk, String mint) throws RpcException {
        List<Object> params = new ArrayList<>();
        params.add(puk);
        params.add(new RpcMintConfig(mint));
//        params.add(new RpcEncoding("jsonParsed"));
        params.add(new RpcSendTransactionConfig2());
        return client.call("getTokenAccountsByDelegate", params, TokenAccountsByDelegateBean.class);
    }

    public TokenAccountBalanceBean getTokenAccountBalance(String tokenPuk) throws RpcException {
        List<Object> params = new ArrayList<>();
        params.add(tokenPuk);
        return client.call("getTokenAccountBalance", params, TokenAccountBalanceBean.class);
    }
}
