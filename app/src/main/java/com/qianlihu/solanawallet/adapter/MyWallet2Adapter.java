package com.qianlihu.solanawallet.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.WalletBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/11/2 10:11
 * desc   : 我的钱包适配器
 * version: 1.0.0
 */
public class MyWallet2Adapter extends BaseQuickAdapter<WalletBean, BaseViewHolder> {

    public MyWallet2Adapter(@Nullable List<WalletBean> data) {
        super(R.layout.item_my_wallet_2, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletBean item) {
        helper.setText(R.id.tv_wallet_name, item.getName());
        ImageView ivDot = helper.getView(R.id.iv_dot);
        if (item.getSelect() == 1) {
            ivDot.setVisibility(View.VISIBLE);
        } else {
            ivDot.setVisibility(View.INVISIBLE);
        }
    }
}
