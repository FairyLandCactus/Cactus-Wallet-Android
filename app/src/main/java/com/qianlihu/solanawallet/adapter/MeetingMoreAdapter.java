package com.qianlihu.solanawallet.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.meeting.IconNameBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/12/1410:09
 * desc   : 会议室更多设置的适配器
 * version: 1.0.0
 */
public class MeetingMoreAdapter extends BaseQuickAdapter<IconNameBean, BaseViewHolder> {

    public MeetingMoreAdapter(@Nullable List<IconNameBean> data) {
        super(R.layout.item_icon_name, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IconNameBean item) {

        helper.setImageDrawable(R.id.iv_icon, mContext.getDrawable(item.getIcon()));
        helper.setText(R.id.tv_name, item.getName());
    }
}
