package com.qianlihu.solanawallet.mvp.member_manager;

import com.qianlihu.solanawallet.api.retrofit.ApiResponse;
import com.qianlihu.solanawallet.base.BaseObserver;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.ExistRoomBean;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.ExistRoomBean2;

import org.checkerframework.checker.units.qual.K;

/**
 * author : Duan
 * date   : 2023/1/616:16
 * desc   :
 * version: 1.0.0
 */
public class MemberManagerPresenter extends BasePresenter<MemberManagerView> {
    public MemberManagerPresenter(MemberManagerView baseView) {
        super(baseView);
    }

    public void kickoutRoom(String roomNo, String member) {

        KickoutRoom kickoutRoom = new KickoutRoom();
        kickoutRoom.setRoom_id(roomNo);
        kickoutRoom.setUser_id(member);
        addDisposable(apiServer.kickoutRoom(kickoutRoom), new BaseObserver<ApiResponse<ExistRoomBean2>>(baseView) {
            @Override
            public void onSuccess(ApiResponse<ExistRoomBean2> data) {
                baseView.kickoutRoomCallback(data.getMsg());
            }

            @Override
            public void onError(String msg, int code) {

            }
        });
    }
}
