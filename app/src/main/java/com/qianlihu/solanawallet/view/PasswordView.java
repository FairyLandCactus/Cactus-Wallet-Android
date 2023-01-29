package com.qianlihu.solanawallet.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.blankj.utilcode.util.SPUtils;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.constant.Constant;

/**
 * author : Duan
 * date   : 2021/12/611:14
 * desc   : 自定义密码输入
 * version: 1.0.0
 */
@SuppressLint("AppCompatCustomView")
public class PasswordView extends EditText {
    // 画笔
    private Paint mPaint;
    // 一个密码所占的宽度
    private int mPasswordItemWidth;
    // 密码的个数默认为6位数
    private int mPasswordNumber = 6;
    // 背景圆颜色
    private int mBgColor = Color.parseColor("#d1d2d6");
    // 背景大小
    private int mBgSize = 1;
    // 背景边框圆角大小
    private int mBgCorner = 0;
    // 外圆的颜色
    private int outRingLineColor = mBgColor;
    // 外圆线条的大小
    private int outRingLineSize = 1;
    // 密码输入的颜色
    private int mPasswordColor = Color.parseColor("#333333");
    // 密码圆点的半径大小
    private int mPasswordRadius = 6;
    // 外圆半径大小
    private int mOutRadius = 25;
    public PasswordView(Context context) {
        this(context, null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        if (isDark) {
            mBgColor = Color.parseColor("#ffffff");
            outRingLineColor = mBgColor;
        }

        initPaint();
        initAttributeSet(context, attrs);
        // 设置输入模式是密码
        setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        // 不显示光标
        setCursorVisible(false);
    }

    /**
     * 初始化属性
     */
    private void initAttributeSet(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.PasswordView);
        // 获取大小
        outRingLineSize = (int) array.getDimension(R.styleable.PasswordView_outRingLineSize, dip2px(outRingLineSize));
        mPasswordRadius = (int) array.getDimension(R.styleable.PasswordView_passwordRadius, dip2px(mPasswordRadius));
        mBgSize = (int) array.getDimension(R.styleable.PasswordView_bgSize, dip2px(mBgSize));
        mBgCorner = (int) array.getDimension(R.styleable.PasswordView_bgCorner, 0);
        // 获取颜色
        mBgColor = array.getColor(R.styleable.PasswordView_bgColor, mBgColor);
        outRingLineColor = array.getColor(R.styleable.PasswordView_outRingColor, outRingLineColor);
        mPasswordColor = array.getColor(R.styleable.PasswordView_passwordColor, mPasswordColor);
        array.recycle();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }

    /**
     * dip 转 px
     */
    private int dip2px(int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dip, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int passwordWidth = getWidth() - (mPasswordNumber - 1) * outRingLineSize;
        mPasswordItemWidth = passwordWidth / mPasswordNumber;
        // 绘制背景外圆
        drawOutRing(canvas);
        // 绘制背景内圆
        drawInRing(canvas);
        // 绘制密码
        drawHidePassword(canvas);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }
    /**
     * 绘制背景外圆
     */
    private void drawOutRing(Canvas canvas) {
        mPaint.setColor(mBgColor);
        // 设置画笔为空心
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBgSize);
        RectF rectF = new RectF(mBgSize, mBgSize, getWidth() - mBgSize, getHeight() - mBgSize);
        // 画圆
        for (int i = 0; i < mPasswordNumber; i++) {
            int cx = i * outRingLineSize + i * mPasswordItemWidth + mPasswordItemWidth / 2 + mBgSize;
            canvas.drawCircle(cx, getHeight() / 2, mOutRadius, mPaint);
        }

    }

    /**
     * 绘制隐藏的密码
     */
    private void drawHidePassword(Canvas canvas) {
        int passwordLength = getText().length();
        if (passwordLength > 6) passwordLength = 6;
        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        if (isDark) {
            mPaint.setColor(Color.parseColor("#ffffff"));
        } else {
            mPaint.setColor(mPasswordColor);
        }
        // 设置画笔为实心
        mPaint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < passwordLength; i++) {
            int cx = i * outRingLineSize + i * mPasswordItemWidth + mPasswordItemWidth / 2 + mBgSize;
            canvas.drawCircle(cx, getHeight() / 2, mPasswordRadius, mPaint);
        }
        //外圆
        if (isDark) {
            mPaint.setColor(Color.parseColor("#ffffff"));
        } else {
            mPaint.setColor(mPasswordColor);
        }

        // 设置画笔为空心
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBgSize);
        RectF rectF = new RectF(mBgSize, mBgSize, getWidth() - mBgSize, getHeight() - mBgSize);
        // 如果没有设置圆角，就画矩形
        for (int i = 0; i < passwordLength; i++) {
            int cx = i * outRingLineSize + i * mPasswordItemWidth + mPasswordItemWidth / 2 + mBgSize;
            canvas.drawCircle(cx, getHeight() / 2, mOutRadius, mPaint);
        }

    }

    /**
     * 绘制背景内圆
     */
    private void drawInRing(Canvas canvas) {
        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        if (isDark) {
            mPaint.setColor(Color.parseColor("#333333"));
        } else {
            mPaint.setColor(outRingLineColor);
        }
        // 设置画笔为实心
        mPaint.setStyle(Paint.Style.FILL);
        // 画圈圈
        for (int i = 0; i < mPasswordNumber; i++) {
            int cx = i * outRingLineSize + i * mPasswordItemWidth + mPasswordItemWidth / 2 + mBgSize;
            canvas.drawCircle(cx, getHeight() / 2, mPasswordRadius, mPaint);
        }
    }
}
