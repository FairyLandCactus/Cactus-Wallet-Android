package com.qianlihu.solanawallet.bean;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.event.DataUpdateEvent;
import com.qianlihu.solanawallet.rpc.RpcException;
import com.qianlihu.solanawallet.rpc.bean.SignaturesBean;
import com.qianlihu.solanawallet.rpc.bean.SignaturesForAddressBean;
import com.qianlihu.solanawallet.rpc.bean.TransactionBean;
import com.qianlihu.solanawallet.sql.WalletManager;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.SolanaUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * author : Duan
 * date   : 2022/1/1718:02
 * desc   : 更新转账记录信息
 * version: 1.0.0
 */
public class UpdateTransactionRecordInfo {

    private boolean isConnect = false;

    public void transferRecord(String mPuk, double mUsdt, String walletType, String mints, String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isConnect = SPUtils.getInstance().getBoolean(Constant.SOL_IS_NET);
                List<SignaturesForAddressBean> bean = null;
                try {
                    if (walletType.equals("1") && !isConnect) {
                        return;
                    }
                    if (name.equals(Constant.SOL_CHAIN)) {
                        if (CacheData.shared().isRecordRequst) {
                            return;
                        }
                        bean = SolanaUtil.getSignaturesForAddress(mPuk);
                        List<TransactionDB> listt = WalletManager.getInstance().solTransferRecord(mPuk, walletType, mints, 10);
                        if (listt.size() == 0) {
                            if (bean != null) {
                                EventBus.getDefault().post(new DataUpdateEvent(0, bean.size()));
                            } else {
                                EventBus.getDefault().post(new DataUpdateEvent(0, 0));
                            }
                        }
                        Log.i("duan==Signature", "交易签名== " + bean.toString());
                        if (bean.size() > 0) {
                            String listStr = JSON.toJSONString(bean);
                            List<SignaturesBean> sLists = JSONObject.parseArray(listStr, SignaturesBean.class);
                            for (int i = 0; i < sLists.size(); i++) {
                                if (CacheData.shared().isRecordRequst) {
                                    return;
                                }
                                String signature = sLists.get(i).getSignature();
//                            TransactionBean tBeann = SolanaUtil.getTransaction(signature);
                                List<TransactionDB> dbs = LitePal.where("myPuk = ? and signatures = ?", mPuk, signature).find(TransactionDB.class);
                                if (dbs.size() == 0) {
                                    soltransferRecord(signature, mPuk, mints, walletType, mUsdt);
                                }
                            }
                            updateEvent(mPuk, walletType, mints);
                        }
                    } else {
                        if (CacheData.shared().isRecordRequst2) {
                            return;
                        }
                        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
                        map.put("address", mPuk);
                        map.put("offset", 0);
                        map.put("limit", 1000);
                        String url = "https://api.solscan.io/account/token/txs";
                        HttpService.doGet(url, map, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String body = response.body().string();
                                List<TransactionDB> listt = WalletManager.getInstance().solTransferRecord(mPuk, walletType, mints, 10);
                                if (!TextUtils.isEmpty(body)) {
                                    Gson gson = new Gson();
                                    SolTransferRecordHashBean hashBean = gson.fromJson(body, SolTransferRecordHashBean.class);
                                    if (hashBean != null) {
                                        if (hashBean.getData() != null) {
                                            if (hashBean.getData().getTx() != null && hashBean.getData().getTx().getTransactions() != null) {
                                                if (hashBean.getData().getTx().getTransactions().size() > 0) {
                                                    List<SolTransferRecordHashBean.DataBean.TxBean.TransactionsBean> signList = hashBean.getData().getTx().getTransactions();
                                                    if (listt.size() == 0) {
                                                        EventBus.getDefault().post(new DataUpdateEvent(0, signList.size()));
                                                    }
                                                    for (int i = 0; i < signList.size(); i++) {
                                                        String siginTrue = signList.get(i).getTxHash();
                                                        try {
                                                            List<TransactionDB> dbs = LitePal.where("myPuk = ? and signatures = ?", mPuk, siginTrue).find(TransactionDB.class);
                                                            if (dbs.size() == 0) {
                                                                soltransferRecord(siginTrue, mPuk, mints, walletType, mUsdt);
                                                            }
                                                        } catch (RpcException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                   updateEvent(mPuk, walletType, mints);
                                                }
                                            } else {
                                                if (listt.size() == 0) {
                                                    EventBus.getDefault().post(new DataUpdateEvent(0, 0));
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (listt.size() == 0) {
                                        EventBus.getDefault().post(new DataUpdateEvent(0, 0));
                                    }
                                }
                                Log.i("duan==transactions", "记录===  " + body);
                            }
                        });
                    }
                    updateEvent(mPuk, walletType, mints);
                } catch (RpcException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void soltransferRecord(String sign, String mPuk, String mints, String walletType, double mUsdt) throws RpcException {
        TransactionBean tBean = SolanaUtil.getTransaction(sign);
        if (tBean != null) {
            CacheData.shared().isRecordRequst = true;
            CacheData.shared().isRecordRequst2 = true;
            long preBalances = tBean.getMeta().getPreBalances().get(0);
            long postBalances = tBean.getMeta().getPostBalances().get(0);
            String address1 = tBean.getTransaction().getMessage().getAccountKeys().get(0);
            String address2 = tBean.getTransaction().getMessage().getAccountKeys().get(1);
            String address3 = tBean.getTransaction().getMessage().getAccountKeys().get(0);
            String from = "";
            String to = "";
            String mint = "";
            Integer decimals = 9;
            if (tBean.getMeta().getPreTokenBalances().size() > 0) {
                mint = tBean.getMeta().getPreTokenBalances().get(0).getMint();
            } else {
                mint = Constant.SOL_TOKEN_ADDRESS;
            }
            long blockTime = tBean.getBlockTime();
            int flag = 0;
            long transferMoney = 0;
            if (tBean.getMeta().getPostTokenBalances().size() > 0 && tBean.getMeta().getPreTokenBalances().size() > 0) {
                preBalances = Long.valueOf(tBean.getMeta().getPreTokenBalances().get(0).getUiTokenAmount().getAmount());
                postBalances = Long.valueOf(tBean.getMeta().getPostTokenBalances().get(0).getUiTokenAmount().getAmount());
                long postBalances2 = Long.valueOf(tBean.getMeta().getPostTokenBalances().get(1).getUiTokenAmount().getAmount());
                decimals = tBean.getMeta().getPostTokenBalances().get(0).getUiTokenAmount().getDecimals();
                String preOwner = tBean.getMeta().getPreTokenBalances().get(0).getOwner();
                String postOwner1 = tBean.getMeta().getPostTokenBalances().get(0).getOwner();
                String postOwner2 = tBean.getMeta().getPostTokenBalances().get(1).getOwner();
                if (address3.equals(mPuk)) {
                    if (preOwner.equals(address3)) {
                        if (postOwner2.equals(address3)) {
                            to = postOwner1;
                        } else {
                            to = postOwner2;
                        }
                    } else {
                        to = preOwner;
                    }
                } else {
                    to = mPuk;
                }
                from = address3;
                if (preOwner.equals(postOwner1)) {
                    if (postBalances > preBalances) {
                        transferMoney = postBalances - preBalances;
                    } else {
                        transferMoney = preBalances - postBalances;
                    }
                } else {
                    if (postBalances2 > preBalances) {
                        transferMoney = postBalances2 - preBalances;
                    } else {
                        transferMoney = preBalances - postBalances2;
                    }
                }

            } else {
                from = address1;
                to = address2;
                if (postBalances > preBalances) {
                    transferMoney = postBalances - preBalances;
                } else {
                    transferMoney = preBalances - postBalances;
                }
//                                        transferMoney = preBalances - postBalances;
            }

            long fee = tBean.getMeta().getFee();
            String signatures = tBean.getTransaction().getSignatures().get(0);
            double money = (double) transferMoney / Math.pow(10, decimals);
            double feeValue = (double) fee / Constant.SOL_DIVISOR;

            List<TransactionDB> list = WalletManager.getInstance().solSiginTransferRecord(mPuk, walletType, mints, signatures, 100000);
            for (int j = 0; j < list.size(); j++) {
                list.get(j).delete();
            }

            TransactionDB db = new TransactionDB();
            db.setMyPuk(mPuk);
            db.setFrom(from);
            db.setTo(to);
            db.setWalletType(walletType);
            db.setMoney(MyUtils.decimalFormat(money));
            db.setUsdt(MyUtils.decimalFormat(money * mUsdt));
            db.setFee(MyUtils.decimalFormat6(feeValue));
            db.setSignatures(signatures);
            db.setDateTime(blockTime);
            db.setMint(mint);
            db.setFlag(flag);
            db.setRemark("");
            db.save();
        }
    }

    private void getSolanaTransferRecord(String mPuk, int page, int pageSize) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("address", mPuk);
        map.put("offset", page);
        map.put("limit", pageSize);
        String url = "https://api.solscan.io/account/soltransfer/txs";
        HttpService.doGet(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                if (!TextUtils.isEmpty(body)) {
                    if (!body.startsWith("{") && !body.endsWith("}")) {
                        return;
                    }
                    Gson gson = new Gson();
                    SolanaTransferRecordBean bean = gson.fromJson(body, SolanaTransferRecordBean.class);
                    if (bean != null) {
                        if (bean.isSucccess()) {
                            if (bean.getData().getTx() != null) {
                                if (bean.getData().getTx().getTotal() > 0) {

                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private void updateEvent(String mPuk, String walletType, String mints) {
        List<TransactionDB> list = WalletManager.getInstance().solTransferRecord(mPuk, walletType, mints, 10);
        for (int i = 0; i < list.size(); i++) {
            String sigin = list.get(i).getSignatures();
            List<TransactionDB> listS = WalletManager.getInstance().solSiginTransferRecord(mPuk, walletType, mints, sigin, 10);
            if (listS.size() > 1) {
                for (int j = 1; i < listS.size(); j++) {
                    listS.get(j).delete();
                }

            }
        }
        List<TransactionDB> listtt = WalletManager.getInstance().solTransferRecord(mPuk, walletType, mints, 10);
        EventBus.getDefault().post(listtt);
    }
}
