package com.qianlihu.solanawallet.adapter;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.meeting.MeetingTypeBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/12/1214:08
 * desc   :
 * version: 1.0.0
 */
public class CommunityAdapter extends BaseQuickAdapter<MeetingTypeBean, BaseViewHolder> {

    private int position = 0;

    public CommunityAdapter(@Nullable List<MeetingTypeBean> data) {
        super(R.layout.item_meeting, data);
    }

    public void select(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @Override
    protected void convert(BaseViewHolder helper, MeetingTypeBean item) {

        ImageView ivSelect = helper.getView(R.id.iv_select);
        TextView tvTitle = helper.getView(R.id.tv_title);
        RelativeLayout rlBg = helper.getView(R.id.rl_bg);

        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        if (isDark) {
            if (position == helper.getLayoutPosition()) {
                ivSelect.setVisibility(View.VISIBLE);
                tvTitle.setTextColor(mContext.getColor(R.color.blue48));
                rlBg.setBackground(mContext.getDrawable(R.drawable.selected_meeting_stock_bord_1));
            } else {
                ivSelect.setVisibility(View.GONE);
                tvTitle.setTextColor(mContext.getColor(R.color.white));
                rlBg.setBackground(mContext.getDrawable(R.drawable.unselected_meeting_stock_bord_1));
            }
        }else {
            if (position == helper.getLayoutPosition()) {
                ivSelect.setVisibility(View.VISIBLE);
                tvTitle.setTextColor(mContext.getColor(R.color.blue48));
                rlBg.setBackground(mContext.getDrawable(R.drawable.selected_meeting_stock_bord));
            } else {
                ivSelect.setVisibility(View.GONE);
                tvTitle.setTextColor(mContext.getColor(R.color.txt_333));
                rlBg.setBackground(mContext.getDrawable(R.drawable.unselected_meeting_stock_bord));
            }
        }

        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_dec, item.getDec());
    }
}
