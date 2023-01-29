package com.qianlihu.solanawallet.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.application.MyApplication;
import com.qianlihu.solanawallet.constant.Constant;

import java.util.Locale;

/**
 * author : Duan
 * date   : 2022/3/3017:56
 * desc   : 文本展开收起
 * version: 1.0.0
 */
public class MyMoreText implements View.OnClickListener {
    private SpannableString eclipseString;    //收起的文字
    private SpannableString notEclipseString; //展开的文字
    private TextView textView;

    private static class MoreTextHolder {
        @SuppressLint("StaticFieldLeak")
        private static final MyMoreText INSTANCE = new MyMoreText();
    }

    public static MyMoreText getInstance() {
        return MyMoreText.MoreTextHolder.INSTANCE;
    }

    public void getLastIndexForLimit(TextView tv, int maxLine, String content) {
        this.textView = tv;
        //获取TextView的画笔对象
        TextPaint paint = textView.getPaint();
        //每行文本的布局宽度
        int width = MyApplication.getContexts().getResources().getDisplayMetrics().widthPixels - dip2px(MyApplication.getContexts(), 40);
        //实例化StaticLayout 传入相应参数
        StaticLayout staticLayout = new StaticLayout(content, paint, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        //判断content是行数是否超过最大限制行数3行
        if (staticLayout.getLineCount() > maxLine) {
            String type = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);

            String putAway = MyApplication.getContexts().getString(R.string.put_away);
            String viewAll = MyApplication.getContexts().getString(R.string.view_all);
            if (type.equals("en")) {
                putAway = "    Put";
                viewAll = "View all";
            } else if (type.equals("ru")) {
                putAway = "    убирать";
                viewAll = "все.";
            }

            //定义展开后的文本内容
            String string1 = content + "    " + putAway;
            notEclipseString = new SpannableString(string1);
            //给收起两个字设成蓝色
            notEclipseString.setSpan(new ForegroundColorSpan(Color.parseColor("#396ff8")), string1.length() - putAway.length(), string1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //获取到第三行最后一个文字的下标
            int index = staticLayout.getLineStart(maxLine) - 1;
            //定义收起后的文本内容
            String substring = content.substring(0, index - viewAll.length()) + "..." + viewAll;
            eclipseString = new SpannableString(substring);
            //给查看全部设成蓝色
            eclipseString.setSpan(new ForegroundColorSpan(Color.parseColor("#396ff8")), substring.length() - viewAll.length(), substring.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置收起后的文本内容
            tv.setText(eclipseString);
            tv.setOnClickListener(this);
            //将textView设成选中状态 true用来表示文本未展示完全的状态,false表示完全展示状态，用于点击时的判断
            tv.setSelected(true);
        } else {
            //没有超过 直接设置文本
            tv.setText(content);
            tv.setOnClickListener(null);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private static int dip2px(Context mContext, float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void onClick(View view) {
        if (view.isSelected()) {
            //如果是收起的状态
            textView.setText(notEclipseString);
            textView.setSelected(false);
        } else {
            //如果是展开的状态
            textView.setText(eclipseString);
            textView.setSelected(true);
        }
    }
}
