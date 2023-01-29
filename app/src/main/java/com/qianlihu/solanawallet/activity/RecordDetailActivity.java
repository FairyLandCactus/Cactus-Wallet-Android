package com.qianlihu.solanawallet.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.BNBTransferRecordDB;
import com.qianlihu.solanawallet.bean.EthTransferRecordDB;
import com.qianlihu.solanawallet.bean.SolTokenTransferRecordBean;
import com.qianlihu.solanawallet.bean.SolanaTokenTransferRecordDetailBean;
import com.qianlihu.solanawallet.bean.SolanaTransferRecordBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.DateTimeUtils;
import com.qianlihu.solanawallet.utils.MyUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @Author: duan
 * @Date: 2022/7/26
 * @Description: 交易记录详情
 */

public class RecordDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_my_tansfer)
    TextView tvMyTansfer;
    @BindView(R.id.tv_transfer_in)
    TextView tvTransferIn;
    @BindView(R.id.tv_fee)
    TextView tvFee;
    @BindView(R.id.tv_transfer_id)
    TextView tvTransferId;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.tv_uint)
    TextView tvUnit;

    private String mPuk = "";
    private String name = "";

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_detail;
    }

    @Override
    protected void initView() {

        String from = "";
        String to = "";
        long longTime = 0;
        String amountStr = "";
        String signtrue = "";
        String fee = "";

        name = getIntent().getStringExtra("name");
        mPuk = getIntent().getStringExtra(Constant.SOL_PUK);
        String mainChain = getIntent().getStringExtra(Constant.MAIN_CHAIN);
        if (mainChain.equals(Constant.SOL_CHAIN)) {
            if (name.equals(Constant.SOL_CHAIN)) {
                solRecordDetail();
            } else {
                getSolTokenRecordDetail();
            }
        } else {
            String value = "";
            String prices = "5000000000";
            String gas = "300000";
            if (mainChain.equals(Constant.BNB_CHAIN)) {
                BNBTransferRecordDB bean = (BNBTransferRecordDB) getIntent().getSerializableExtra("record");
                from = bean.getFrom();
                to = bean.getTo();
                longTime = Long.valueOf(bean.getTimeStamp());
                signtrue = bean.getHash();
                value = bean.getValue();
                if (!TextUtils.isEmpty(bean.getGasPrice())) {
                    prices = bean.getGasPrice();
                }
                gas = bean.getGasUsed();
            } else {
                EthTransferRecordDB bean = (EthTransferRecordDB) getIntent().getSerializableExtra("record");
                from = bean.getFrom();
                to = bean.getTo();
                longTime = Long.valueOf(bean.getTimeStamp());
                signtrue = bean.getHash();
                value = bean.getValue();
                if (!TextUtils.isEmpty(bean.getGasPrice())) {
                    prices = bean.getGasPrice();
                }
                gas = bean.getGasUsed();
            }

            int decimals = getIntent().getIntExtra("decimals", 0);
            BigInteger bigInteger = new BigInteger(value);
            double amountF = bigInteger.doubleValue() / Math.pow(10, decimals);
            amountStr = MyUtils.decimalFormat6(amountF);
            BigInteger gasPrice = new BigInteger(prices);
            BigInteger gasUsed = new BigInteger(gas);
            fee = MyUtils.decimalFormat6(gasUsed.doubleValue() / (Math.pow(10, decimals) / gasPrice.doubleValue()));
            tvFee.setText(fee);
            tvMyTansfer.setText(mPuk);
            tvTransferId.setText(signtrue);
            String type = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
            String time = "";
            if (type.equals("en")) {
                time = DateTimeUtils.convertTimestamp2DateEnglish(longTime);
            } else {
                time = DateTimeUtils.convertTimestamp2Date(longTime);
            }
            tvTime.setText(time);
            tvTitle.setText(name + getString(R.string.transfer));
            tvUnit.setText("  " + name);
            if (mPuk.toLowerCase().equals(from)) {
                tvMoney.setText("- " + amountStr);
                tvCollection.setText(getString(R.string.transfer_completed));
                tvTransferIn.setText(to);
            } else {
                tvMoney.setText("+ " + amountStr);
                tvCollection.setText(getString(R.string.collection_completed));
                tvTransferIn.setText(from);
            }
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
    }

    @OnClick({R.id.iv_back, R.id.tv_my_tansfer, R.id.tv_transfer_in, R.id.tv_transfer_id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_my_tansfer:
                String myTansfer = tvMyTansfer.getText().toString().trim();
                ClickCopyUtils.copy(this, myTansfer);
                break;
            case R.id.tv_transfer_in:
                String transferIn = tvTransferIn.getText().toString().trim();
                ClickCopyUtils.copy(this, transferIn);
                break;
            case R.id.tv_transfer_id:
                String transferId = tvTransferId.getText().toString().trim();
                ClickCopyUtils.copy(this, transferId);
                break;
        }
    }

    private void solRecordDetail() {
        SolanaTransferRecordBean.DataBean.TxBean.TransactionsBean bean = (SolanaTransferRecordBean.DataBean.TxBean.TransactionsBean) getIntent().getSerializableExtra("record");
        String from = bean.getSrc();
        String to = bean.getDst();
        long longTime = bean.getBlockTime();
        String amount = bean.getLamport();
        int decimals = bean.getDecimals();
        String signtrue = bean.getTxHash();
        String fee = bean.getFee();
        double amountDb = 0.0;
        double feeDb = 0.0;
        if (!TextUtils.isEmpty(amount)) {
            BigInteger bg = new BigInteger(amount);
            amountDb = bg.doubleValue() / Math.pow(10, decimals);
        }
        String amountStr = MyUtils.decimalFormat6(amountDb);
        if (!TextUtils.isEmpty(fee)) {
            BigInteger bg = new BigInteger(fee);
            feeDb = bg.doubleValue() / Math.pow(10, decimals);
        }
        String feeStr = MyUtils.decimalFormat6(feeDb);
        tvFee.setText(feeStr);
        tvMyTansfer.setText(mPuk);
        tvTransferId.setText(signtrue);
        String type = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
        String time = "";
        if (type.equals("en")) {
            time = DateTimeUtils.convertTimestamp2DateEnglish(longTime);
        } else {
            time = DateTimeUtils.convertTimestamp2Date(longTime);
        }
        tvTime.setText(time);
        tvTitle.setText(name + getString(R.string.transfer));
        tvUnit.setText("  " + name);
        if (!to.equals(mPuk)) {
            tvMoney.setText("- " + amountStr);
            tvCollection.setText(getString(R.string.transfer_completed));
            tvTransferIn.setText(to);
        } else {
            tvMoney.setText("+ " + amountStr);
            tvCollection.setText(getString(R.string.collection_completed));
            tvTransferIn.setText(from);
        }
    }

    private void getSolTokenRecordDetail() {
        SolTokenTransferRecordBean.DataBean.TxBean.TransactionsBean bean = (SolTokenTransferRecordBean.DataBean.TxBean.TransactionsBean) getIntent().getSerializableExtra("record");
        String to = bean.getChange().getAddress();
        long longTime = bean.getBlockTime();
        String amount = bean.getChange().getChangeAmount();
        int decimals = bean.getChange().getDecimals();
        String sym = bean.getChange().getSymbol();
        String signtrue = bean.getTxHash();
        String fee = String.valueOf(bean.getChange().getFee());
        double amountDb = 0.0;
        double feeDb = 0.0;
        if (!TextUtils.isEmpty(amount)) {
            BigInteger bg = new BigInteger(amount);
            amountDb = bg.doubleValue() / Math.pow(10, decimals);
        }
        String amountStr = MyUtils.decimalFormat6(amountDb);
        if (!TextUtils.isEmpty(fee)) {
            BigInteger bg = new BigInteger(fee);
            feeDb = bg.doubleValue() / Math.pow(10, 9);
        }
        String feeStr = MyUtils.decimalFormat6(feeDb);
        tvFee.setText(feeStr);
        tvMyTansfer.setText(mPuk);
        tvTransferId.setText(signtrue);
        String type = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
        String time = "";
        if (type.equals("en")) {
            time = DateTimeUtils.convertTimestamp2DateEnglish(longTime);
        } else {
            time = DateTimeUtils.convertTimestamp2Date(longTime);
        }
        tvTime.setText(time);
        if (TextUtils.isEmpty(sym)) {
            sym = name;
        }
        tvTitle.setText(sym + getString(R.string.transfer));
        tvUnit.setText("  " + sym);
        if (amountDb < 0) {
            tvMoney.setText(amountStr);
            tvCollection.setText(getString(R.string.transfer_completed));
        } else {
            tvMoney.setText("+ " + amountStr);
            tvCollection.setText(getString(R.string.collection_completed));
        }
//        tvTransferIn.setText(to);
        getSolTokenTransferRecordDetail(signtrue);
    }

    private void getSolTokenTransferRecordDetail(String signtrue) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("tx", signtrue);
        String url = "https://api.solscan.io/transaction";
        showLoadingDialog();
        HttpService.doGet(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeLoadingDialog();
                        showerrortoast(getString(R.string.request_exception));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(() -> closeLoadingDialog());
                String body = response.body().string();
                String from = "";
                if (!TextUtils.isEmpty(body)) {
                    if (!body.startsWith("{") && !body.endsWith("}")) {
                        return;
                    }
                    Gson gson = new Gson();
                    SolanaTokenTransferRecordDetailBean bean = gson.fromJson(body, SolanaTokenTransferRecordDetailBean.class);
                    if (bean != null) {
                        if (bean.getSigner() != null) {
                            if (bean.getSigner().size() > 0) {
                                from = bean.getSigner().get(0);
                            }
                        }
                        if (bean.getMainActions() != null) {
                            if (bean.getMainActions().size() > 0) {
                                if (bean.getMainActions().get(0).getData() != null) {
                                    if (bean.getMainActions().get(0).getData().getEvent() != null) {
                                        if (bean.getMainActions().get(0).getData().getEvent().size() > 0) {
                                            if (bean.getMainActions().get(0).getData().getEvent().get(0).getDestinationOwner() != null) {
                                                String toAddress = bean.getMainActions().get(0).getData().getEvent().get(0).getDestinationOwner();
                                                if (from.equals(mPuk)) {
                                                    runOnUiThread(() -> tvTransferIn.setText(toAddress));
                                                } else {
                                                    String finalFrom = from;
                                                    runOnUiThread(() -> tvTransferIn.setText(finalFrom));
                                                }
                                            }
                                        }
                                    } else {
                                        if (bean.getMainActions().get(0).getData() != null) {
                                            if (bean.getMainActions().get(0).getData().getDestination_owner() != null) {
                                                String toAddress = bean.getMainActions().get(0).getData().getDestination_owner();
                                                if (from.equals(mPuk)) {
                                                    runOnUiThread(() -> tvTransferIn.setText(toAddress));
                                                } else {
                                                    String finalFrom = from;
                                                    runOnUiThread(() -> tvTransferIn.setText(finalFrom));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}