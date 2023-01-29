package com.qianlihu.solanawallet.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.rpc.bean.mintTokenList.Tokens;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author : Duan
 * date   : 2021/11/2 10:11
 * desc   : swap兑换选择代币适配器
 * version: 1.0.0
 */
public class SwappExchangeTokenAdapter extends BaseQuickAdapter<Tokens, BaseViewHolder> {

    public SwappExchangeTokenAdapter(@Nullable List<Tokens> dataList) {
        super(R.layout.item_swap_token, dataList);
    }

    @Override
    protected void convert(BaseViewHolder helper, Tokens data) {
        CircleImageView ivPic = helper.getView(R.id.iv_pic);
        helper.addOnClickListener(R.id.iv_select);
        String pic = data.getLogoURI();
        Glide.with(mContext).load(pic).placeholder(R.mipmap.icon_solana).error(R.mipmap.icon_solana).into(ivPic);

        helper.setText(R.id.tv_name, data.getName());
        helper.setText(R.id.tv_symbol, data.getSymbol());
        helper.setText(R.id.tv_amount, data.getAmount()+"");
    }
}
