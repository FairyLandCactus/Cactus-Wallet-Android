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
public class ChainAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int position = 111;

    public ChainAdapter(@Nullable List<String> data) {
        super(R.layout.item_chain, data);
    }

    public void select(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, String data) {
        TextView tvChain = helper.getView(R.id.tv_chain);
        tvChain.setText(data);
        LinearLayout llChain = helper.getView(R.id.ll_chain);
        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        if (isDark) {
            if (position == helper.getLayoutPosition()) {
                tvChain.setTextColor(getColor(R.color.txt_3757FF));
                llChain.setBackground(mContext.getDrawable(R.drawable.gas_price_bg_bord_dark));
            } else {
                tvChain.setTextColor(getColor(R.color.white));
                llChain.setBackground(mContext.getDrawable(R.drawable.un_gas_price_bg_bord_dark));
            }
        } else {
            if (position == helper.getLayoutPosition()) {
                tvChain.setTextColor(getColor(R.color.txt_6880FF));
                llChain.setBackground(mContext.getDrawable(R.drawable.gas_price_bg_bord_light));
            } else {
                tvChain.setTextColor(getColor(R.color.txt_C1C1C1));
                llChain.setBackground(mContext.getDrawable(R.drawable.un_gas_price_bg_bord_light));
            }
        }

    }
}
