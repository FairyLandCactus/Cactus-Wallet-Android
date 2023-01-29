package com.qianlihu.solanawallet.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.DappInfoBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/2211:11
 * desc   : 排行列表适配器
 * version: 1.0.0
 */
public class DappToolItemAdapter extends BaseQuickAdapter<DappInfoBean, BaseViewHolder> {

    private int listSize = 0;

    public DappToolItemAdapter(@Nullable List<DappInfoBean> data) {
        super(R.layout.item_dapp_right_list, data);
        this.listSize = data.size();
    }

    @Override
    protected void convert(BaseViewHolder helper, DappInfoBean item) {
        helper.setText(R.id.tv_title, item.getName());
        String iconUrl = item.getIconUrl();
        helper.setText(R.id.tv_dec, item.getInfo());
        Glide.with(mContext).load(item.getIcon()).into((ImageView) helper.getView(R.id.iv_icon));
        if (!TextUtils.isEmpty(iconUrl)) {
            Glide.with(mContext).load(iconUrl).into((ImageView) helper.getView(R.id.iv_icon));
        }

        View line = helper.getView(R.id.view_line);
        if (helper.getLayoutPosition() == (listSize-1)) {
            line.setVisibility(View.GONE);
        }

        helper.addOnClickListener(R.id.tv_more);

    }
}
