package com.qianlihu.solanawallet.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.TokenTypeBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/3017:05
 * desc   :
 * version: 1.0.0
 */
public class Web3Adapter extends BaseQuickAdapter<TokenTypeBean, BaseViewHolder> {
    public Web3Adapter(@Nullable List<TokenTypeBean> data) {
        super(R.layout.item_web3, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TokenTypeBean item) {
        ImageView ivPic = helper.getView(R.id.iv_pic);
        ivPic.setImageResource(item.getIcon());
        helper.setText(R.id.tv_title, item.getTokenName());
    }
}
