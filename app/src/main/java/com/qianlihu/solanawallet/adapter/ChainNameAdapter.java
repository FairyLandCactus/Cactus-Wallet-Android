package com.qianlihu.solanawallet.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.ChainNameBean;
import com.qianlihu.solanawallet.bean.TokenTypeBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/2211:11
 * desc   :
 * version: 1.0.0
 */
public class ChainNameAdapter extends BaseQuickAdapter<ChainNameBean, BaseViewHolder> {

    private int position = 0;

    public ChainNameAdapter(@Nullable List<ChainNameBean> data) {
        super(R.layout.item_chain_name, data);
    }

    public void select(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, ChainNameBean item) {

        ImageView ivIcon = helper.getView(R.id.iv_icon);
        if (position == helper.getLayoutPosition()) {
            ivIcon.setImageResource(item.getUnIcon());
        } else {
            ivIcon.setImageResource(item.getIcon());
        }
    }
}
