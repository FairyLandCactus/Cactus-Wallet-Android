package com.qianlihu.solanawallet.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.WalletBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/11/2 10:11
 * desc   : 选择需要添加代币的钱包适配器
 * version: 1.0.0
 */
public class SelectAddTokenWalletAdapter extends BaseQuickAdapter<WalletBean, BaseViewHolder> {

    public SelectAddTokenWalletAdapter(@Nullable List<WalletBean> data) {
        super(R.layout.item_select_add_token_wallet, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletBean data) {
        helper.setText(R.id.tv_wallet_name, data.getName());
    }
}
