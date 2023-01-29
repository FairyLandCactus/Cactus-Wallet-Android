package com.qianlihu.solanawallet.mvp.create_room.view;

import com.qianlihu.solanawallet.base.BaseView;

/**
 * author : Duan
 * date   : 2022/12/2311:41
 * desc   :
 * version: 1.0.0
 */
public interface CreateRoomView extends BaseView {

    void createMeetingCallback(String address, String roomNo, String roomName, String userName, int num);

}
