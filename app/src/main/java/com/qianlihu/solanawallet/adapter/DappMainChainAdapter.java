package com.qianlihu.solanawallet.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.SelectMainChainBean;
import com.qianlihu.solanawallet.bean.TokenTypeBean;
import com.qianlihu.solanawallet.constant.Constant;

import org.litepal.util.Const;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * author : Duan
 * date   : 2021/11/2 10:11
 * desc   : 选择主链适配器
 * version: 1.0.0
 */
public class DappMainChainAdapter extends BaseQuickAdapter<TokenTypeBean, BaseViewHolder> {

    private int position = 0;

    public DappMainChainAdapter(@Nullable List<TokenTypeBean> data) {
        super(R.layout.item_dapp_tool_select_main_chain, data);
    }

    public void select(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, TokenTypeBean data) {
        TextView tvName = helper.getView(R.id.tv_name);
        tvName.setText(data.getTokenName());
        ImageView ivIcon = helper.getView(R.id.iv_logo);
        Glide.with(mContext).load(data.getIcon()).into(ivIcon);
        LinearLayout llChain = helper.getView(R.id.ll_select_main_chain);
        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        if (isDark) {
            if (position == helper.getLayoutPosition()) {
                llChain.setBackground(mContext.getDrawable(R.drawable.dapp_tool_mian_chain_bord));
                tvName.setTextColor(mContext.getColor(R.color.text_1F97FF));
            } else {
                llChain.setBackground(mContext.getDrawable(R.drawable.dapp_tool_unselect_mian_chain_dark_bord));
                tvName.setTextColor(mContext.getColor(R.color.white));
            }
        } else {
            if (position == helper.getLayoutPosition()) {
                llChain.setBackground(mContext.getDrawable(R.drawable.dapp_tool_mian_chain_bord));
                tvName.setTextColor(mContext.getColor(R.color.text_1F97FF));
            } else {
                llChain.setBackground(mContext.getDrawable(R.drawable.dapp_tool_unselect_mian_chain_light_bord));
                tvName.setTextColor(mContext.getColor(R.color.txt_333));
            }
        }

    }
}
