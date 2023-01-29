package com.qianlihu.solanawallet.adapter;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.SelectMainChainBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author : Duan
 * date   : 2021/11/2 10:11
 * desc   : 选择主链适配器
 * version: 1.0.0
 */
public class SelectMainChainAdapter extends BaseQuickAdapter<SelectMainChainBean, BaseViewHolder> {

    public SelectMainChainAdapter(@Nullable List<SelectMainChainBean> data) {
        super(R.layout.item_select_main_chain, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SelectMainChainBean data) {
        Glide.with(mContext).load(data.getLogo()).fitCenter().into((CircleImageView) helper.getView(R.id.iv_pic));
        helper.setText(R.id.tv_symbol, data.getSymbol())
                .setText(R.id.tv_name, data.getName());
    }
}
