package com.qianlihu.solanawallet.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.SPUtils;
import com.qianlihu.solanawallet.constant.Constant;

import java.util.Locale;

/**
 * author : Duan
 * date   : 2021/4/2916:25
 * desc   : 语言切换工具
 * version: 1.0.0
 */
public class LanguageUtil {

    public static void setLanguage(Context context, Locale locale) {
        Configuration config = context.getResources().getConfiguration();// 获得设置对象
        Resources resources = context.getResources();// 获得res资源对象
        DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。
        config.locale = locale; // 简体中文
        resources.updateConfiguration(config, dm);
        String type = Constant.LANGUAGE_CN;
        String cnOen = "zh-Hans";
        if (locale.getLanguage().equals("en")) {
            type = Constant.LANGUAGE_EN;
            cnOen = "en";
        } else if (locale.getLanguage().equals("ru")) {
            type = Constant.LANGUAGE_RU;
            cnOen = "ru";
        }
        SPUtils.getInstance().put(Constant.LANGUAGE_TYPE, type);
        SPUtils.getInstance().put(Constant.LANGUAGE_CN_EN, cnOen);
    }

    public static void languageUpdate(Context context, Locale locale) {
        String type = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
        if (type.isEmpty()) {
            setLanguage(context, locale);
        } else {
            if (type.equals("en")) {
                setLanguage(context, Locale.ENGLISH);
            } else if (type.equals("ru")) {
                Locale l = new Locale("ru");
                setLanguage(context, l);
            } else {
                setLanguage(context, locale);
            }
        }
    }
}
