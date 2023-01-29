package com.qianlihu.solanawallet.bean;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.binance.BnbTransferRecordBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.ethereum.EthWalletUtil;
import com.qianlihu.solanawallet.event.DataUpdateEvent;
import com.qianlihu.solanawallet.event.ErrorEvent;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * author : Duan
 * date   : 2022/4/1415:46
 * desc   : 更新Eth转账记录
 * version: 1.0.0
 */
public class UpdateEthTransferRecordInfo {

    public void transferRecord(String mPuk, String walletType, String mints, String name, int page, int pageSize) {
        boolean isConnect = SPUtils.getInstance().getBoolean(Constant.SOL_IS_NET);
        if (walletType.equals("1") && !isConnect) {
            return;
        }
        List<EthTransferRecordDB> dbs = LitePal.where("myPuk = ? and walletType = ? and contractAddress = ?", mPuk, walletType, mints.toLowerCase())
                .order("timeStamp desc")
                .limit(10)
                .find(EthTransferRecordDB.class);
        if (dbs.size() == 0) {
            EventBus.getDefault().post(new DataUpdateEvent(0, pageSize));
        }
        EthWalletUtil.getBlockNumber().subscribe(new SingleObserver<BigInteger>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(BigInteger bigInteger) {
                Log.i("duan==record", "当前节点=== " + bigInteger.toString());
                int endBlock = bigInteger.intValue();
                if (name.equals(Constant.ETH_CHAIN)) {
                    getBnbTransferRecord(endBlock, mPuk, walletType, mints, page, page);
                } else {
                    getBnbTokenTransferRecord(endBlock, mPuk, mints, walletType, page, pageSize);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    private void getBnbTransferRecord(int endblock, String mPuk, String mWalletType, String mint, int page, int pageSize) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("module", "account");
        linkedHashMap.put("action", "txlist");
        linkedHashMap.put("address", mPuk);
        linkedHashMap.put("startblock", 0);
        linkedHashMap.put("endblock", endblock);
        linkedHashMap.put("page", page);
        linkedHashMap.put("offset", pageSize);
        linkedHashMap.put("sort", "desc");
        linkedHashMap.put("apikey", Constant.ETHSCAN_APIKEY);

        HttpService.doGet(Constant.ETHSCAN_URL, linkedHashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("duan==record", "获取失败==== " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("duan==record", "交易记录==== " + body);
                if (!TextUtils.isEmpty(body)) {
                    JSONObject object = null;
                    try {
                        object = new JSONObject(body);
                        String status = object.getString("status");
                        if (status.equals("1")) {
                            Gson gson = new Gson();
                            BnbTransferRecordBean recordBean = gson.fromJson(body, BnbTransferRecordBean.class);
                            if (recordBean != null) {
                                if (recordBean.getResult() != null) {
                                    bnbSave(recordBean, mPuk, mWalletType, mint);
                                }
                            }
                        }else {
                            String result = object.getString("result");
                            String message = object.getString("message");
                            String err = "";
                            if (message.equals("NOTOK")) {
                                err = result;
                            } else {
                                err = message;
                            }
                            EventBus.getDefault().post(new ErrorEvent(412, err));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    private void getBnbTokenTransferRecord(int endblock, String mPuk, String mMint, String mWalletType, int page, int pageSize) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("module", "account");
        linkedHashMap.put("action", "tokentx");
        linkedHashMap.put("contractaddress", mMint);
        linkedHashMap.put("address", mPuk);
        linkedHashMap.put("startblock", 0);
        linkedHashMap.put("endblock", endblock);
        linkedHashMap.put("page", page);
        linkedHashMap.put("offset", pageSize);
        linkedHashMap.put("sort", "desc");
        linkedHashMap.put("apikey", Constant.ETHSCAN_APIKEY);

        HttpService.doGet(Constant.ETHSCAN_URL, linkedHashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("duan==record", "获取失败==== " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("duan==record", "交易记录==== " + body);
                if (!TextUtils.isEmpty(body)) {
                    try {
                        JSONObject object = new JSONObject(body);
                        String status = object.getString("status");
                        if (status.equals("1")) {
                            Gson gson = new Gson();
                            BnbTransferRecordBean recordBean = gson.fromJson(body, BnbTransferRecordBean.class);
                            if (recordBean != null) {
                                if (recordBean.getResult() != null) {
                                    bnbSave(recordBean, mPuk, mWalletType, mMint);
                                }
                            }
                        } else {
                            String result = object.getString("result");
                            String message = object.getString("message");
                            String err = "";
                            if (message.equals("NOTOK")) {
                                err = result;
                            } else {
                                err = message;
                            }
                            EventBus.getDefault().post(new ErrorEvent(412, err));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void bnbSave(BnbTransferRecordBean bean, String mPuk, String mWalletType, String mint) {
        for (int i = 0; i < bean.getResult().size(); i++) {
            String hash = bean.getResult().get(i).getHash();
            List<EthTransferRecordDB> dbs = LitePal.where("myPuk = ? and hash = ?", mPuk, hash).find(EthTransferRecordDB.class);
            if (dbs.size() == 0) {
                String contract = bean.getResult().get(i).getContractAddress();
                if (TextUtils.isEmpty(contract)) {
                    contract = Constant.ETH_TOKEN_ADDRESS.toLowerCase();
                }
                EthTransferRecordDB db = new EthTransferRecordDB();
                db.setMyPuk(mPuk);
                db.setWalletType(mWalletType);
                db.setTimeStamp(bean.getResult().get(i).getTimeStamp());
                db.setHash(hash);
                db.setFrom(bean.getResult().get(i).getFrom());
                db.setContractAddress(contract);
                db.setTo(bean.getResult().get(i).getTo());
                db.setTokenDecimal(bean.getResult().get(i).getTokenDecimal());
                db.setGasPrice(bean.getResult().get(i).getGasPrice());
                db.setGasUsed(bean.getResult().get(i).getGasUsed());
                db.setValue(bean.getResult().get(i).getValue());
                db.setInput(bean.getResult().get(i).getInput());
                db.save();
            }
        }
        List<EthTransferRecordDB> dbs = LitePal.where("myPuk = ? and walletType = ? and contractAddress = ?", mPuk, mWalletType, mint.toLowerCase())
                .order("timeStamp desc")
                .limit(10)
                .find(EthTransferRecordDB.class);
        EventBus.getDefault().post(dbs);
    }
}
