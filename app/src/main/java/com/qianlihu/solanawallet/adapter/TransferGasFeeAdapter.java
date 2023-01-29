package com.qianlihu.solanawallet.adapter;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.wallet_bean.TransferGasFeeBean;

import java.util.List;

import static com.xuexiang.xutil.resource.ResUtils.getColor;
import static com.xuexiang.xutil.resource.ResUtils.getString;

/**
 * author : Duan
 * date   : 2022/11/810:00
 * desc   :
 * version: 1.0.0
 */
public class TransferGasFeeAdapter extends BaseQuickAdapter<TransferGasFeeBean, BaseViewHolder> {

    private int position = 1;
    private String unit;

    public TransferGasFeeAdapter(@Nullable List<TransferGasFeeBean> data, String unit) {
        super(R.layout.item_gas_price, data);
        this.unit = unit;
    }

    public void select(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, TransferGasFeeBean data) {
        TextView tvSpeed = helper.getView(R.id.tv_speed);
        TextView tvGasFee = helper.getView(R.id.tv_fee);
        TextView tvUsd = helper.getView(R.id.tv_usd);
        TextView tvTime = helper.getView(R.id.tv_time);
        LinearLayout llGasFee = helper.getView(R.id.ll_gas_fee);
        tvSpeed.setText(data.getSpeed());
        tvGasFee.setText(MyUtils.decimalFormat6(data.getGasFee()) + unit);
        tvUsd.setText("≈$" + MyUtils.decimalFormat(data.getUsdFee()));
        if (unit.equals(Constant.BNB_CHAIN)) {
            tvGasFee.setText(MyUtils.decimalFormat6(data.getGasFee()/10) + unit);
            tvUsd.setText("≈$" + MyUtils.decimalFormat(data.getUsdFee()/10));
        }

        if (data.getUsdFee() == 0) {
            tvUsd.setText(getString(R.string.usd_unknown));
        }
        tvTime.setText(data.getTime());
        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        if (isDark) {
            if (position == helper.getLayoutPosition()) {
                tvSpeed.setTextColor(getColor(R.color.txt_3757FF));
                tvGasFee.setTextColor(getColor(R.color.txt_3757FF));
                tvUsd.setTextColor(getColor(R.color.txt_3757FF));
                tvTime.setTextColor(getColor(R.color.txt_3757FF));
                llGasFee.setBackground(mContext.getDrawable(R.drawable.gas_price_bg_bord_dark));
            } else {
                tvSpeed.setTextColor(getColor(R.color.white));
                tvGasFee.setTextColor(getColor(R.color.white));
                tvUsd.setTextColor(getColor(R.color.white));
                tvTime.setTextColor(getColor(R.color.white));
                llGasFee.setBackground(mContext.getDrawable(R.drawable.un_gas_price_bg_bord_dark));
            }
        } else {
            if (position == helper.getLayoutPosition()) {
                tvSpeed.setTextColor(getColor(R.color.txt_6880FF));
                tvGasFee.setTextColor(getColor(R.color.txt_6880FF));
                tvUsd.setTextColor(getColor(R.color.txt_6880FF));
                tvTime.setTextColor(getColor(R.color.txt_6880FF));
                llGasFee.setBackground(mContext.getDrawable(R.drawable.gas_price_bg_bord_light));
            } else {
                tvSpeed.setTextColor(getColor(R.color.txt_C1C1C1));
                tvGasFee.setTextColor(getColor(R.color.txt_C1C1C1));
                tvUsd.setTextColor(getColor(R.color.txt_C1C1C1));
                tvTime.setTextColor(getColor(R.color.txt_C1C1C1));
                llGasFee.setBackground(mContext.getDrawable(R.drawable.un_gas_price_bg_bord_light));
            }
        }

    }
}
