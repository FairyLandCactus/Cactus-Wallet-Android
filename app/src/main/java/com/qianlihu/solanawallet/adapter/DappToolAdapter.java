package com.qianlihu.solanawallet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.TokenTypeBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/2211:11
 * desc   :
 * version: 1.0.0
 */
public class DappToolAdapter extends BaseQuickAdapter<TokenTypeBean, BaseViewHolder> {

    private int position = 0;

    public DappToolAdapter(@Nullable List<TokenTypeBean> data) {
        super(R.layout.item_token_type, data);
    }

    public void select(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, TokenTypeBean item) {
        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setVisibility(View.GONE);
        ImageView ivIcon = helper.getView(R.id.iv_icon);
        if (position == helper.getLayoutPosition()) {
            ivIcon.setImageResource(item.getIcon());
        } else {
            ivIcon.setImageResource(item.getUnIcon());
        }
    }
}
