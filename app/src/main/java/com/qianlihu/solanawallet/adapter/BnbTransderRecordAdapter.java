package com.qianlihu.solanawallet.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.SaveTranferRecordBean;
import com.qianlihu.solanawallet.bean.TransferRecordBodyBean;
import com.qianlihu.solanawallet.binance.BnbTransferRecordBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.DateTimeUtils;
import com.qianlihu.solanawallet.utils.MyUtils;

import java.math.BigInteger;
import java.util.List;

/**
 * author : Duan
 * date   : 2021/11/210:11
 * desc   : bnb转账记录适配器
 * version: 1.0.0
 */
public class BnbTransderRecordAdapter extends BaseQuickAdapter<BnbTransferRecordBean.ResultBean, BaseViewHolder> {

    private String mPuk = "";
    public BnbTransderRecordAdapter(@Nullable List<BnbTransferRecordBean.ResultBean> data, String puk) {
        super(R.layout.item_transfer_record, data);
        this.mPuk = puk;
    }

    @Override
    protected void convert(BaseViewHolder helper, BnbTransferRecordBean.ResultBean db) {
        String type = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
        String fromff = db.getFrom();
        String to = db.getTo();
        String longTimeStr = db.getTimeStamp();
        String value = db.getValue();
        int decimals = TextUtils.isEmpty(db.getTokenDecimal()) ? 18 : Integer.parseInt(db.getTokenDecimal());

        BigInteger bigInteger = new BigInteger(value);
        double amountF = bigInteger.doubleValue() / Math.pow(10,decimals);
        String amountStr = MyUtils.decimalFormat6(amountF);

        ImageView ivIcon = helper.getView(R.id.iv_icon);

        if (fromff.equals(mPuk.toLowerCase())) {
            ivIcon.setImageResource(R.mipmap.sol_page_transfer);
            helper.setText(R.id.tv_sol_money, "-" + amountStr);
            helper.setText(R.id.tv_transfer_address, "Transfer  " + to);
        } else {
            ivIcon.setImageResource(R.mipmap.sol_page_receive);
            helper.setText(R.id.tv_sol_money, "+" + amountStr);
            helper.setText(R.id.tv_transfer_address, "Transfer  " + fromff);
        }

        long longTime = Long.valueOf(longTimeStr);
        String time = "";
        if (type.equals("en")) {
            time = DateTimeUtils.convertTimestamp2DateEnglish(longTime);
        } else {
            time = DateTimeUtils.convertTimestamp2Date(longTime);
        }

        helper.setText(R.id.tv_time, time);
    }
}
