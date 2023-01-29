package com.qianlihu.solanawallet.mvp.create_room.presenter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.BindUserActivity;
import com.qianlihu.solanawallet.activity.WalletConnect3Activity;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.api.retrofit.ApiResponse;
import com.qianlihu.solanawallet.api.retrofit.ApiRetrofit;
import com.qianlihu.solanawallet.api.retrofit.ApiServer;
import com.qianlihu.solanawallet.base.BaseObserver;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.BoundBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.mvp.create_room.bean.CreateMeetingBody;
import com.qianlihu.solanawallet.mvp.create_room.bean.CreateRoomBean;
import com.qianlihu.solanawallet.mvp.create_room.view.CreateRoomView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.LinkedHashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;
import static com.xuexiang.xutil.resource.ResUtils.getString;

/**
 * author : Duan
 * date   : 2022/12/2311:43
 * desc   :
 * version: 1.0.0
 */
public class CreateRoomPresenter extends BasePresenter<CreateRoomView> {
    public CreateRoomPresenter(CreateRoomView baseView) {
        super(baseView);
    }

    public void createRoom(Context context, String languge, String address, String roomNo, String roomName, int num, String userName, String userHeader) {
        baseView.showLoadingInfos(context.getString(R.string.creat_meeting), context.getString(R.string.entering_meeting), false);
        addDisposable(apiServer.createMeetingRoom(languge, address, roomNo, roomName, num, userName, userHeader), new BaseObserver<ApiResponse<CreateRoomBean>>(baseView) {
            @Override
            public void onSuccess(ApiResponse<CreateRoomBean> data) {
                baseView.createMeetingCallback(address, roomNo, roomName, userName, num);
                baseView.hideLoadingInfos();
            }

            @Override
            public void onError(String msg, int code) {
                baseView.hideLoadingInfos();
            }
        });

//        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
//        map.put("language", languge);
//        map.put("address", address);
//        map.put("room_id", roomNo);
//        map.put("room_name", roomName);
//        map.put("room_man_num", num);
//        map.put("user_name", userName);
//        map.put("user_img", userHeader);
//        String url = Constant.MEETING_BASE_URL + Constant.CREAT_ROOM;
//        HttpService.post(url, map, true, new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.i("duan==bound", "请求失败===   " + e.getMessage());
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String body = response.body().string();
//                Log.i("duan==bound", "请求成功===   " + body);
//                Gson gson = new Gson();
//                runOnUiThread(() -> {
//                    if (!TextUtils.isEmpty(body)) {
//                        if (!body.startsWith("{") && !body.endsWith("}")) {
//                            return;
//                        }
//                        baseView.createMeetingCallback(address, roomNo, roomName, userName, num);
//
//                    } else {
//                    }
//                });
//            }
//        });
    }
}
