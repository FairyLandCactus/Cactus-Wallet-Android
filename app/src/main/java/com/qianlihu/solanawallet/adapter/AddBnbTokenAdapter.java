package com.qianlihu.solanawallet.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.binance.BNBTokenBean;
import com.qianlihu.solanawallet.rpc.bean.mintTokenList.Tokens;
import com.qianlihu.solanawallet.utils.TokenIconUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author : Duan
 * date   : 2021/11/2 10:11
 * desc   : 添加代币适配器
 * version: 1.0.0
 */
public class AddBnbTokenAdapter extends BaseQuickAdapter<BNBTokenBean, BaseViewHolder> {

    public AddBnbTokenAdapter(@Nullable List<BNBTokenBean> dataList) {
        super(R.layout.item_add_token, dataList);
    }

    @Override
    protected void convert(BaseViewHolder helper, BNBTokenBean data) {
        CircleImageView ivPic = helper.getView(R.id.iv_pic);
        ImageView ivSelect = helper.getView(R.id.iv_select);
        helper.addOnClickListener(R.id.iv_select);
        String pic = data.getLogoUrl();
        Glide.with(mContext).load(pic).diskCacheStrategy(DiskCacheStrategy.ALL).error(TokenIconUtils.icon(data.getName())).into(ivPic);

        helper.setText(R.id.tv_name, "("+data.getDec()+")");
        helper.setText(R.id.tv_mint, data.getContractAdd());
        helper.setText(R.id.tv_symbol, data.getName());

        View line = helper.getView(R.id.view_line);
        if (helper.getLayoutPosition() == 0) {
            line.setVisibility(View.GONE);
        }

        Integer selectFlag = data.getSelect();
        if (selectFlag == 1) {
            ivSelect.setImageResource(R.mipmap.choose);
        } else {
            ivSelect.setImageResource(R.mipmap.add);
        }
    }
}
