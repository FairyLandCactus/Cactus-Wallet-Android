package com.qianlihu.solanawallet.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.TransferGasFeeAdapter;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.wallet_bean.TransferGasFeeBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author : Duan
 * date   : 2022/11/716:32
 * desc   :
 * version: 1.0.0
 */
public class GasSetView extends LinearLayout {

    private RecyclerView rvGasFee;
    private TextView tvCustomGas;
    private LinearLayout llCustom;
    private EditText etGasPrice;
    private EditText etGasLimit;
    private TextView tvEstimatedFee;

    private double[] gasPriceArray = {5, 5.05, 7};
    private Long[] gasLimitArray = {300000l, 300000l, 300000l};

    private double gasPrice = 0;
    private Long gasLimit = 0l;
    private Long decimal = 1000000000l;

    private boolean isItem = false;
    private double usd = 0.0;
    public double gasFee = 0.0;
    private String type = "";
    private Context context;

    public void setUsdChain(double usd, String type) {
        this.usd = usd;
        this.type = type;
        initData();
    }

    public GasSetView(Context context) {
        super(context);
    }

    public GasSetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.view_gas_set, this);
        this.context = context;
        rvGasFee = findViewById(R.id.rv_gas_fee);
        tvCustomGas = findViewById(R.id.tv_custom_gas);
        llCustom = findViewById(R.id.ll_custom);
        etGasLimit = findViewById(R.id.et_gas_limit);
        etGasPrice = findViewById(R.id.et_gas_price);
        tvEstimatedFee = findViewById(R.id.tv_estimated_fee);
    }

    private void initData() {
        rvGasFee.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvGasFee.addItemDecoration(new GridSpaceItemDecoration(3, 30, 40));

        String[] speed = {context.getString(R.string.speed_slow), context.getString(R.string.speed_recommend), context.getString(R.string.speed_fast)};
        String[] time = {"≈5 s", "≈3 s", "≈2 s"};
        if (type.equals(Constant.ETH_CHAIN)) {
            time = new String[]{"≈15 min", "≈3 min", "≈1 min"};
            gasPriceArray = new double[]{15,16.8,19.2};
            etGasPrice.setText("16.2");
        }
        List<TransferGasFeeBean> beanList = new ArrayList<>();
        for (int i = 0; i < time.length; i++) {
            TransferGasFeeBean bean = new TransferGasFeeBean();
            bean.setGas(gasPriceArray[i]);
            bean.setLimit(gasLimitArray[i]);
            bean.setSpeed(speed[i]);
            bean.setTime(time[i]);
            double feeTotal = gasPriceArray[i] * (gasLimitArray[i] * decimal / Math.pow(10, 18));
            String feeTotalStr = MyUtils.decimalFormat6(feeTotal);
            double total = Double.valueOf(feeTotalStr);
            bean.setGasFee(total);
            bean.setUsdFee(usd * total);
            beanList.add(bean);
        }

        TransferGasFeeAdapter feeAdapter = new TransferGasFeeAdapter(beanList, type);
        rvGasFee.setAdapter(feeAdapter);
        isItem = true;
        gasLimit = beanList.get(1).getLimit();
        gasPrice = beanList.get(1).getGas();
        gasFee = beanList.get(1).getGasFee();
        feeAdapter.setOnItemClickListener((adapter, view, position) -> {
            feeAdapter.select(position);
            gasLimit = beanList.get(position).getLimit();
            gasPrice = beanList.get(position).getGas();
            gasFee = beanList.get(position).getGasFee();
            isItem = true;
            llCustom.setVisibility(GONE);
        });

        tvCustomGas.setOnClickListener(v -> {
            isItem = false;
            feeAdapter.select(-1);
            llCustom.setVisibility(VISIBLE);
        });

        etGasLimit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUsd();
            }
        });

        etGasPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateUsd();
            }
        });
        updateUsd();
    }

    public Long getGasPrices() {
        if (isItem) {
            return new Double(gasPrice * decimal).longValue();
        }
        String gasPriceEd = etGasPrice.getText().toString().trim();
        double gasP = 0.0;
        Long gasL = 0l;
        if (!TextUtils.isEmpty(gasPriceEd)) {
            gasL = Long.valueOf((long) (gasP * decimal));
        }
        return gasL;
    }

    public Long getGasLimits() {
        if (isItem) {
            return gasLimit;
        }
        String gasLimitEd = etGasLimit.getText().toString().trim();
        Long limitL = 0l;
        if (!TextUtils.isEmpty(gasLimitEd)) {
            limitL = Long.valueOf(gasLimitEd);
        }
        return limitL;
    }

    private void updateUsd() {
        String gasPriceEd = etGasPrice.getText().toString().trim();
        String gasLimitEd = etGasLimit.getText().toString().trim();
        double usdTotal = 0.0;
        double gasFeeTotal = 0.0;
        double gasPrice = 0.0;
        Long gasLimit = 0l;
        if (!TextUtils.isEmpty(gasPriceEd)) {
            gasPrice = Double.valueOf(gasPriceEd);
        }
        if (!TextUtils.isEmpty(gasLimitEd)) {
            gasLimit = Long.valueOf(gasLimitEd);
        }
        gasFeeTotal = (gasPrice * gasLimit * decimal) / Math.pow(10, 18);
        usdTotal = usd * gasFeeTotal;
        gasFee = Double.valueOf(MyUtils.decimalFormat6(gasFeeTotal));
        tvEstimatedFee.setText(MyUtils.decimalFormat6(gasFeeTotal) + type + "≈$" + MyUtils.decimalFormat(usdTotal));
    }

}
