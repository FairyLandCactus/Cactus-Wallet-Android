package com.qianlihu.solanawallet.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.WalletBean;

import java.util.List;

import com.qianlihu.solanawallet.application.MyApplication;

/**
 * author : Duan
 * date   : 2021/11/210:11
 * desc   : 我的钱包适配器
 * version: 1.0.0
 */
public class MyWalletAdapter extends BaseQuickAdapter<WalletBean, BaseViewHolder> {

    private int select = 666666;

    public MyWalletAdapter(@Nullable List<WalletBean> data) {
        super(R.layout.item_my_wallet, data);
    }

    public void selectItem(int select) {
        this.select = select;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletBean item) {

        helper.setText(R.id.tv_wallet_name, item.getName());
        helper.addOnClickListener(R.id.iv_ed_wallet);
        LinearLayout llMyWalletBg = helper.getView(R.id.ll_wallet_bg);
        ImageView ivSelect = helper.getView(R.id.iv_select);
        int position = helper.getLayoutPosition();
        Context context = MyApplication.getContexts();
        if (position == select) {
            ivSelect.setVisibility(View.VISIBLE);
            llMyWalletBg.setBackground(context.getDrawable(R.drawable.my_wallet_select_bord));
        } else {
            ivSelect.setVisibility(View.GONE);
            llMyWalletBg.setBackground(context.getDrawable(R.drawable.my_wallet_unselect_bord));
        }
    }
}
