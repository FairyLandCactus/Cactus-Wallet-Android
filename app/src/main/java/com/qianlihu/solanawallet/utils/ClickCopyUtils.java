package com.qianlihu.solanawallet.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.qianlihu.solanawallet.R;

import es.dmoral.toasty.Toasty;

/**
 * author : Duan
 * date   : 2021/12/813:55
 * desc   : 点击复制
 * version: 1.0.0
 */
public class ClickCopyUtils {

    public static void copy(Context context, String content) {
        if (TextUtils.isEmpty(content)) {
            return;
        }
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", content);
        cmb.setPrimaryClip(clipData);
        Toasty.success(context,context.getString(R.string.copied_text)).show();
    }
}
