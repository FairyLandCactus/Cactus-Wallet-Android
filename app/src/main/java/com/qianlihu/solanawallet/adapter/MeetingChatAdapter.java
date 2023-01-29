package com.qianlihu.solanawallet.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.ChatBean;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.MemberBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2023/1/1115:42
 * desc   :
 * version: 1.0.0
 */
public class MeetingChatAdapter extends BaseQuickAdapter<ChatBean, BaseViewHolder> {
    List<ChatBean> data;
    public MeetingChatAdapter(@Nullable List<ChatBean> data) {
        super(R.layout.item_chat_msg, data);
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, ChatBean item) {
        helper.setText(R.id.tv_user_name, item.getUserName());
        helper.setText(R.id.tv_msg, item.getMsg());
    }

    @Override
    public void onBindViewHolder(BaseViewHolder helper, int position) {
        ChatBean item = data.get(position);
        helper.setText(R.id.tv_user_name, item.getUserName()+":");
        helper.setText(R.id.tv_msg, item.getMsg());
    }

}
