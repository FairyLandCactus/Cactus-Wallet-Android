package com.qianlihu.solanawallet.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.TokenTypeBean;
import com.qianlihu.solanawallet.constant.Constant;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/2211:11
 * desc   : 排行类型适配器
 * version: 1.0.0
 */
public class DappRankTypeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int position = 0;

    public DappRankTypeAdapter(@Nullable List<String> data) {
        super(R.layout.item_dapp_left_type, data);
    }

    public void select(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name, item);
        ImageView ivLine = helper.getView(R.id.iv_line);
        TextView tvName = helper.getView(R.id.tv_name);
        if (position == helper.getLayoutPosition()) {
            ivLine.setBackgroundColor(mContext.getColor(R.color.txt_6288FF));
            if (SPUtils.getInstance().getBoolean(Constant.NIGHT)) {
                tvName.setTextColor(mContext.getColor(R.color.white));
            } else {
                tvName.setTextColor(mContext.getColor(R.color.txt_333));
            }
        } else {
            ivLine.setBackgroundColor(mContext.getColor(R.color.transparent));
            tvName.setTextColor(mContext.getColor(R.color.txt_A4ABC1));
        }
    }
}
