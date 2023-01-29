package com.qianlihu.solanawallet.mvp.member_manager;

import com.qianlihu.solanawallet.base.BaseView;

/**
 * author : Duan
 * date   : 2023/1/616:15
 * desc   :
 * version: 1.0.0
 */
public interface MemberManagerView extends BaseView {

    void kickoutRoomCallback(String msg);

}
