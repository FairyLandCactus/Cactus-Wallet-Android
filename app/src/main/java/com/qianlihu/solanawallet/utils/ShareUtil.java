package com.qianlihu.solanawallet.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import com.qianlihu.solanawallet.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * author : Duan
 * date   : 2021/5/515:45
 * desc   : 分享工具类
 * version: 1.0.0
 */
public class ShareUtil {

    private static final String EMAIL_ADDRESS = "@qq.com.com";

    //分享文本
    public static void shareText(Context context, String text, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, title));
    }

    public static void shareWeixinQQText(Context context, String text, String title, String flag) {
        String pck = "com.tencent.mm";
        String csl = "com.tencent.mm.ui.tools.ShareImgUI";
        if (flag.equals("QQ")) {
            pck = "com.tencent.mobileqq";
            csl = "com.tencent.mobileqq.activity.JumpActivity";
        }
        Intent intent = new Intent();
        ComponentName comp = new ComponentName(pck, csl);
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, title));

    }

    //分享截图
    public static void shareImage(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        Uri u = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), takeScreenShot(activity), null,null));//将截图bitmap存系统相册
        intent.putExtra(Intent.EXTRA_STREAM, u);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(Intent.createChooser(intent, ""));
    }

    /**
     *
     * @param activity
     * @param shareView 分享的视图
     * @param flag 0保存图片，1分享图片
     */
    public static void shareImage2(Activity activity, View shareView, int flag) {
        String filePath = "";
        View dView = shareView;
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        Uri u = Uri.parse(MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, null,null));//将截图bitmap存系统相册
        intent.putExtra(Intent.EXTRA_STREAM, u);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (flag == 1) {
            activity.startActivity(Intent.createChooser(intent, ""));
        } else {
            Toasty.info(activity, activity.getString(R.string.share_save_camer_tip)).show();
        }
    }

    //分享邮箱
    public static void sendEmail(Context context, String title) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + EMAIL_ADDRESS));
        context.startActivity(Intent.createChooser(intent, title));
    }

    //多张图片分享
    public static void sendMoreImage(Context context, ArrayList<Uri> imageUris, String title) {
        Intent mulIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        mulIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        mulIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(mulIntent, "多图文件分享"));
    }

    /**
     * Activity截屏
     */
    public static Bitmap takeScreenShot(Activity pActivity) {
        View view = pActivity.getWindow().getDecorView();
        // 设置是否可以进行绘图缓存
        view.setDrawingCacheEnabled(true);
        // 如果绘图缓存无法，强制构建绘图缓存
        view.buildDrawingCache();
        // 返回这个缓存视图
        Bitmap bitmap = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        // 测量屏幕宽和高
        view.getWindowVisibleDisplayFrame(frame);
        int stautsHeight = frame.top;
        Point point = new Point();
        pActivity.getWindowManager().getDefaultDisplay().getSize(point);
        int width = point.x;
        int height = point.y;
        // 根据坐标点和需要的宽和高创建bitmap
        bitmap = Bitmap.createBitmap(bitmap, 0, stautsHeight, width, height - stautsHeight);
        return bitmap;
    }

}
