package com.qianlihu.solanawallet.adapter;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.TokenIconUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author : Duan
 * date   : 2021/11/210:11
 * desc   :
 * version: 1.0.0
 */
public class AssetsAdapter extends BaseQuickAdapter<AddTokenDB, BaseViewHolder> {

    public AssetsAdapter(@Nullable List<AddTokenDB> data) {
        super(R.layout.item_assets, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddTokenDB db) {
        String name = "";
        if (db.getSymbol() != null) {
            name = db.getSymbol();
        } else {
            name = db.getTokenAddress();
        }
        helper.setText(R.id.tv_sol_money, MyUtils.decimalFormat(db.getAmount()));
        helper.setText(R.id.tv_usdt, "$" + MyUtils.decimalFormat(db.getUsdTotal()));
        helper.setText(R.id.tv_name, name);
        CircleImageView ivLogo = helper.getView(R.id.iv_logo);
        String logoUrl = db.getLogoURI();
//        RequestOptions options = new RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        Glide.with(mContext).load(logoUrl).placeholder(TokenIconUtils.icon(name)).error(TokenIconUtils.icon(name)).into(ivLogo);
    }
}
