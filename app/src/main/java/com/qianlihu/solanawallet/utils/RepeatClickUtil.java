package com.qianlihu.solanawallet.utils;

/**
 * author : Duan
 * date   : 2021/8/717:34
 * desc   : 处理按钮重复点击
 * version: 1.0.0
 */
public class RepeatClickUtil {

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 2000;
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < MIN_CLICK_DELAY_TIME) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
