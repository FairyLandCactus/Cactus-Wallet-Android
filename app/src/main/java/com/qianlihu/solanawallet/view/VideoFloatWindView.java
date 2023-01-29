package com.qianlihu.solanawallet.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;
import com.lzf.easyfloat.interfaces.OnInvokeView;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.MeetingRoomActivity;
import com.qianlihu.solanawallet.application.MyApplication;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.CacheData;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VideoFloatWindView {

    private static final String TAG_FLOAT_VIEW = "TAG_FLOAT_VIEW";

    public static void doOnActivityResume(Activity activity) {
//        //客服界面不弹出悬浮窗
        EasyFloat.Builder builder = EasyFloat.with(activity)
                .setLayout(R.layout.float_video_window_layout, new OnInvokeView() {
                    @Override
                    public void invoke(View view) {
                        View click_view_float = view.findViewById(R.id.iv_float_call);
                        click_view_float.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MeetingRoomActivity.start(MyApplication.getContexts());
                            }
                        });
                    }
                })
                .setFilter(MeetingRoomActivity.class)
                .setShowPattern(ShowPattern.ALL_TIME)
                .setAnimator(null)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setTag(TAG_FLOAT_VIEW);

        builder.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        builder.show();
    }

    public static void dismissFloatView(Activity activity) {
        EasyFloat.dismissAppFloat(TAG_FLOAT_VIEW);
    }

}
