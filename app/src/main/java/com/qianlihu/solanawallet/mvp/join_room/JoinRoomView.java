package com.qianlihu.solanawallet.mvp.join_room;

import com.qianlihu.solanawallet.base.BaseView;

/**
 * author : Duan
 * date   : 2022/12/2917:49
 * desc   :
 * version: 1.0.0
 */
public interface JoinRoomView extends BaseView {

    void joinRoomCallback(String roomNo, String address, String userName);
}
