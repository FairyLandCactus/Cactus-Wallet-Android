package com.qianlihu.solanawallet.mvp.meeting_room.view;

import com.qianlihu.solanawallet.base.BaseView;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.MeetingRoomUserBean;

/**
 * author : Duan
 * date   : 2022/12/1916:32
 * desc   :
 * version: 1.0.0
 */
public interface MeetingRoomView extends BaseView {

    void inviteTypeCheck(int position);

    void roomMemberListCallback(MeetingRoomUserBean bean);
}
