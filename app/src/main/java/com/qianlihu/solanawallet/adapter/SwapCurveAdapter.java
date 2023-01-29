package com.qianlihu.solanawallet.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.event.UsdtEvent;
import com.qianlihu.solanawallet.utils.MyUtils;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/11/210:11
 * desc   :
 * version: 1.0.0
 */
public class SwapCurveAdapter extends BaseQuickAdapter<UsdtEvent, BaseViewHolder> {
    public SwapCurveAdapter(@Nullable List<UsdtEvent> data) {
        super(R.layout.item_swap_curve, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UsdtEvent data) {
        TextView tvName = helper.getView(R.id.tv_name);
        TextView tvPoint = helper.getView(R.id.tv_point);
        TextView tvValue = helper.getView(R.id.tv_value);
        ImageView ivLine = helper.getView(R.id.iv_line);
        LinearLayout llCurve = helper.getView(R.id.ll_curve);

        String instId = data.getArg().getInstId();
        String last = "0.00";
        String open24h = "0.00";
        if (data.getData().size() > 0) {
            last = data.getData().get(0).getLast();
            open24h = data.getData().get(0).getOpen24h();
        }
        double lastD = Double.valueOf(last);
        double open24hD = Double.valueOf(open24h);
        double interestRate = (lastD-open24hD)/100;
        String interestRateStr = MyUtils.decimalFormat2(interestRate);

        if (instId.equals("SOL-USDT")) {
            tvName.setText("SOL");
//            SPUtils.getInstance().put(Constant.USDT_SOL, last);
        } else if (instId.equals("TRX-USDT")) {
            tvName.setText("TRX");
        }else if(instId.equals("BNB-USDT")){
            tvName.setText("BNB");
        }
        tvValue.setText("$"+last);
        tvPoint.setText(interestRateStr + "%");

        if (interestRate >= 0) {
            tvPoint.setTextColor(mContext.getColor(R.color.txt_01CA15));
            tvValue.setTextColor(mContext.getColor(R.color.txt_01CA15));
            ivLine.setImageResource(R.mipmap.yuan_universe_line_2);
            llCurve.setBackground(mContext.getDrawable(R.drawable.swap_curve_green_bord));
        } else {
            tvPoint.setTextColor(mContext.getColor(R.color.txt_EB1D1D));
            tvValue.setTextColor(mContext.getColor(R.color.txt_EB1D1D));
            ivLine.setImageResource(R.mipmap.yuan_universe_line_1);
            llCurve.setBackground(mContext.getDrawable(R.drawable.swap_curve_red_bord));
        }
    }
}
