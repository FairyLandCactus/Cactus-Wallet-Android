package com.qianlihu.solanawallet.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * @Author: duan
 * @Date: 2021/8/30
 * @Description: 自定义Viewpage解决滑动冲突
 *
 */
public class NoScrollViewPager extends ViewPager {

    // 是否禁止 viewpager 左右滑动
    private boolean noScroll = false;

    public void setNoScroll(boolean noScroll){
        this.noScroll = noScroll;
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if (noScroll){
            return false;
        }else{
            return super.onTouchEvent(arg0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll){
            return false;
        }else{
            return super.onInterceptTouchEvent(arg0);
        }
    }
}
