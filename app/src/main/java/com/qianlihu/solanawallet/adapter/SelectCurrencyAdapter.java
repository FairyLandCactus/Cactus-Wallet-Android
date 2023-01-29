package com.qianlihu.solanawallet.adapter;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.TokenIconUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author : Duan
 * date   : 2021/11/2 10:11
 * desc   : 选择币种类型适配器
 * version: 1.0.0
 */
public class SelectCurrencyAdapter extends BaseQuickAdapter<AddTokenDB, BaseViewHolder> {

    public SelectCurrencyAdapter(@Nullable List<AddTokenDB> data) {
        super(R.layout.item_select_currency, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddTokenDB data) {
        CircleImageView ivPic = helper.getView(R.id.iv_pic);
        helper.addOnClickListener(R.id.iv_select);
        String pic = data.getLogoURI();
        String name = data.getSymbol();
        Glide.with(mContext).load(pic).error(TokenIconUtils.icon(name)).into(ivPic);

        helper.setText(R.id.tv_type_name, data.getSymbol());
        helper.setText(R.id.tv_type_dec, data.getName());
        double amount = data.getAmount();
        helper.setText(R.id.tv_sol_money, MyUtils.decimalFormat(amount));
        helper.setText(R.id.tv_rmb_money, "≈" + MyUtils.decimalFormat(data.getUsdt()) + "USDT");

    }
}
