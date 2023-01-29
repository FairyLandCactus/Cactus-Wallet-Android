package com.qianlihu.solanawallet.utils;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SPUtils;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.constant.Constant;

/**
 * Created by Administrator on 2017/8/22.
 */

public class ThemeUtil {
    public ThemeUtil() {
    }

    public static void setTheme(@NonNull Activity activity) {
        boolean isLight = SPUtils.getInstance().getBoolean(Constant.NIGHT, false);
        activity.setTheme(isLight ? R.style.ThemeDark :R.style.ThemeLight);
    }

    public static void reCreate(@NonNull final Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                activity.recreate();
                Intent intent = activity.getIntent();
                activity.overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.finish();
                activity.overridePendingTransition(0, 0);
                activity.startActivity(intent);
            }
        });

    }
}
