package com.qianlihu.solanawallet.mvp.join_room;

import android.content.Context;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.api.retrofit.ApiResponse;
import com.qianlihu.solanawallet.base.BaseObserver;
import com.qianlihu.solanawallet.base.BasePresenter;

/**
 * author : Duan
 * date   : 2022/12/2917:50
 * desc   :
 * version: 1.0.0
 */
public class JoinRoomPresenter extends BasePresenter<JoinRoomView> {
    public JoinRoomPresenter(JoinRoomView baseView) {
        super(baseView);
    }

    public void joinRoom(Context context, String languge, String roomNo, String address, String userName, String userHeader, int isVideo) {

        baseView.showLoadingInfos(context.getString(R.string.join_meeting), context.getString(R.string.entering_meeting), false);
        addDisposable(apiServer.joinMeetingRoom(languge, address, roomNo, isVideo,1, userName, userHeader), new BaseObserver<ApiResponse<JoinRoomBean>>(baseView) {
            @Override
            public void onSuccess(ApiResponse<JoinRoomBean> data) {
                baseView.joinRoomCallback(roomNo,address,userName);
                baseView.hideLoadingInfos();
            }

            @Override
            public void onError(String msg, int code) {
                baseView.hideLoadingInfos();
            }
        });
    }
}
