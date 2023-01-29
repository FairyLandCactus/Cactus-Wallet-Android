package com.qianlihu.solanawallet.adapter;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.meeting.MemberManagerBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/12/1316:50
 * desc   :
 * version: 1.0.0
 */
public class MemberManagerAdapter extends BaseQuickAdapter<MemberManagerBean, BaseViewHolder> {

    private int userType = 2;
    public MemberManagerAdapter(@Nullable List<MemberManagerBean> data, int userType) {
        super(R.layout.item_member_manager, data);
        this.userType = userType;
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberManagerBean item) {
        ImageView ivCheck = helper.getView(R.id.iv_check);
        helper.addOnClickListener(R.id.iv_check);

        if (userType == 2) {
            ivCheck.setVisibility(View.GONE);
        }

        if (item.isCheck()) {
            ivCheck.setImageDrawable(mContext.getDrawable(R.mipmap.icon_member_check));
        } else {
            ivCheck.setImageDrawable(mContext.getDrawable(R.mipmap.icon_member_uncheck));
        }

        helper.setText(R.id.tv_name, item.getUserId());
    }
}
