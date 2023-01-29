package com.qianlihu.solanawallet.adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.TokenTypeBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.DateTimeUtils;
import com.qianlihu.solanawallet.wallet_bean.ArticlesListBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/22 11:11
 * desc   :  dapp肯能感兴趣适配器
 * version: 1.0.0
 */
public class DappInterestAdapter extends BaseQuickAdapter<ArticlesListBean.RowsBean, BaseViewHolder> {

    public DappInterestAdapter(@Nullable List<ArticlesListBean.RowsBean> data) {
        super(R.layout.item_dapp_interest, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArticlesListBean.RowsBean item) {
        String type = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
        helper.setText(R.id.tv_dec, item.getTitle());
        Glide.with(mContext).load(item.getImage()).into((ImageView) helper.getView(R.id.iv_pic));
        if (type.equals("en")) {
            helper.setText(R.id.tv_time, item.getAuthor()+"  "+ DateTimeUtils.convertTimestamp2DateEnglish(item.getCreatetime()));
        } else {
            helper.setText(R.id.tv_time,  item.getAuthor()+"  "+ DateTimeUtils.convertTimestamp2Date(item.getCreatetime()));
        }
    }
}
