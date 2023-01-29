package com.qianlihu.solanawallet.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.DappInfoBean;
import com.qianlihu.solanawallet.utils.SplitUtil;
import com.qianlihu.solanawallet.wallet_bean.DappTypeBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/2211:11
 * desc   : 排行列表适配器
 * version: 1.0.0
 */
public class DappTypeListAdapter extends BaseQuickAdapter<DappTypeBean.DataBean.RowsBean, BaseViewHolder> {

    private int listSize = 0;

    public DappTypeListAdapter(@Nullable List<DappTypeBean.DataBean.RowsBean> data) {
        super(R.layout.item_dapp_right_list, data);
        this.listSize = data.size();
    }

    @Override
    protected void convert(BaseViewHolder helper, DappTypeBean.DataBean.RowsBean item) {
        helper.setText(R.id.tv_title, item.getName());
        String iconUrl = item.getImage();
        helper.setText(R.id.tv_dec, item.getDescribe());
        List<String> tag = SplitUtil.stringSplit(item.getTags());

        TextView tvTag1 = helper.getView(R.id.tv_tag_1);
        TextView tvTag2 = helper.getView(R.id.tv_tag_2);

        if (tag.size() == 1) {
            tvTag1.setVisibility(View.VISIBLE);
            tvTag1.setText(tag.get(0));
        }
        if (tag.size() >= 2) {
            tvTag1.setVisibility(View.VISIBLE);
            tvTag2.setVisibility(View.VISIBLE);
            tvTag1.setText(tag.get(0));
            tvTag2.setText(tag.get(1));
        }

        Glide.with(mContext).load(iconUrl).error(R.mipmap.icon_unknown).into((ImageView) helper.getView(R.id.iv_icon));
        View line = helper.getView(R.id.view_line);
        if (helper.getLayoutPosition() == (listSize - 1)) {
            line.setVisibility(View.GONE);
        }
        helper.addOnClickListener(R.id.tv_more);

    }
}
