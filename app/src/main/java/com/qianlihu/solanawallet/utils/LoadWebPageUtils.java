package com.qianlihu.solanawallet.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.qianlihu.solanawallet.activity.LoadWebPage2Activity;
import com.qianlihu.solanawallet.activity.LoadWebPage3Activity;
import com.qianlihu.solanawallet.activity.LoadWebPageActivity;
import com.qianlihu.solanawallet.activity.SolscanWebActivity;
import com.qianlihu.solanawallet.activity.WalletConnect2Activity;
import com.qianlihu.solanawallet.activity.WalletConnect3Activity;
import com.qianlihu.solanawallet.activity.WalletConnect4Activity;
import com.qianlihu.solanawallet.activity.WalletConnectActivity;
import com.qianlihu.solanawallet.constant.Constant;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * author : Duan
 * date   : 2021/4/1310:24
 * desc   :
 * version: 1.0.0
 */
public class LoadWebPageUtils {

    public static android.webkit.ValueCallback<Uri[]> mUploadCallbackAboveL;
    public static ValueCallback<Uri> mUploadCallbackBelow;
    private static Uri imageUri;
    private static int REQUEST_CODE = 1234;
    private static int RECORD_CODE = 12345;

    /**
     * 设置跳转页面
     *
     * @param context
     * @param title
     */
    public static void activityIntent(Context context, int flag, String title, String url) {
        Intent intent = new Intent();
        intent.putExtra(Constant.WEB_TITLE, title);
        intent.putExtra(Constant.WEB_URL, url);
        if (flag == 1) {
            if (url.contains("www.wivyswap.com/#/index")) {
                intent.setClass(context, WalletConnectActivity.class);
            } else if (url.contains("web.wivyswap.com")) {
                intent.setClass(context, WalletConnect2Activity.class);
            } else if (url.contains("ive.wivyswap.com")) {
                intent.setClass(context, WalletConnect3Activity.class);
            } else if (url.contains("web.massageton.com")) {
                intent.setClass(context, WalletConnect4Activity.class);
            } else if (url.contains("142.4.7.241:3001")) {
                intent.setClass(context, WalletConnect2Activity.class);
            } else {
//                intent.setClass(context, LoadWebPage3Activity.class);
                intent.setClass(context, SolscanWebActivity.class);
            }
            intent.putExtra(Constant.MAIN_CHAIN, Constant.SOL_CHAIN);
        } else if (flag == 2 || flag == 3) {
            if (flag == 2) {
                intent.putExtra(Constant.MAIN_CHAIN, Constant.BNB_CHAIN);
            } else {
                intent.putExtra(Constant.MAIN_CHAIN, Constant.ETH_CHAIN);
            }
            intent.setClass(context, LoadWebPage2Activity.class);
        }
//        else if (flag == 4) {
//            intent.putExtra(Constant.MAIN_CHAIN, Constant.SOL_CHAIN);
//            intent.setClass(context, LoadWebPage3Activity.class);
//        }
        context.startActivity(intent);
    }

    public static void activityIntent2(Context context, String title, String url) {
        Intent intent = new Intent();
        intent.putExtra(Constant.WEB_TITLE, title);
        intent.putExtra(Constant.WEB_URL, url);
        intent.setClass(context, SolscanWebActivity.class);
        context.startActivity(intent);
    }

    /**
     * 加载网页
     *
     * @param webView
     * @param url
     */
    public static void webViewPage(Activity activity, WebView webView, String url, boolean isCache) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(isCache); //启用应用缓存
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webView.getSettings().setUseWideViewPort(true);//将图片调整到适合webview的大小
//        webView.getSettings().setTextZoom(80);
        webView.getSettings().setLoadWithOverviewMode(true);//宽度是否可超过屏幕尺寸
        webView.getSettings().setSupportMultipleWindows(false);//设置WebView是否支持多窗口
        webView.getSettings().setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        webView.getSettings().setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setGeolocationEnabled(true);
        webView.setWebContentsDebuggingEnabled(true);

        webView.loadUrl(url);
    }

    public static void webViewPageData(Activity activity, WebView webView, String data, boolean isdark) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setTextZoom(220);
        webView.getSettings().setLoadWithOverviewMode(true);//宽度是否可超过屏幕尺寸
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        webView.getSettings().setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                takePhoto(activity);
                return true;
            }
        });

        String cssStyle = "";
        if (isdark) {
            cssStyle = "<style>* {color:#FFFFFF !important;}</style>";
        } else {
            cssStyle = "<style>* {color:#333333 !important;}</style>";
        }
        webView.loadDataWithBaseURL(null, cssStyle + data, "text/html", "utf-8", null);
//        imgReset(webView);
    }

    /**
     * 调用相机
     */
    public static void takePhoto(Activity activity) {
        // 指定拍照存储位置的方式调起相机
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator;
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        imageUri = Uri.fromFile(new File(filePath + fileName));

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        activity.startActivityForResult(intent, REQUEST_CODE);
//
//
//        // 选择图片（不包括相机拍照）,则不用成功后发刷新图库的广播
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
//        activity.startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent Photo = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

        activity.startActivityForResult(chooserIntent, REQUEST_CODE);
    }

    /**
     * Android API < 21(Android 5.0)版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    public static void chooseBelow(Activity activity, int resultCode, Intent data) {
        Log.e("WangJ", "返回调用方法--chooseBelow");

        if (RESULT_OK == resultCode) {
            updatePhotos(activity);

            if (data != null) {
                // 这里是针对文件路径处理
                Uri uri = data.getData();
                if (uri != null) {
                    Log.e("WangJ", "系统返回URI：" + uri.toString());
                    mUploadCallbackBelow.onReceiveValue(uri);
                } else {
                    mUploadCallbackBelow.onReceiveValue(null);
                }
            } else {
                // 以指定图像存储路径的方式调起相机，成功后返回data为空
                Log.e("WangJ", "自定义结果：" + imageUri.toString());
                mUploadCallbackBelow.onReceiveValue(imageUri);
            }
        } else {
            mUploadCallbackBelow.onReceiveValue(null);
        }
        mUploadCallbackBelow = null;
    }

    /**
     * Android API >= 21(Android 5.0) 版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    public static void chooseAbove(Activity activity, int resultCode, Intent data) {
        Log.e("WangJ", "返回调用方法--chooseAbove");

        if (RESULT_OK == resultCode) {
            updatePhotos(activity);
            if (data != null) {
                // 这里是针对从文件中选图片的处理
                Uri[] results;
                Uri uriData = data.getData();
                if (uriData != null) {
                    results = new Uri[]{uriData};
                    for (Uri uri : results) {
                        Log.e("WangJ", "系统返回URI：" + uri.toString());
                    }
                    mUploadCallbackAboveL.onReceiveValue(results);
                } else {
                    mUploadCallbackAboveL.onReceiveValue(null);
                }
            } else {
                Log.e("WangJ", "自定义结果：" + imageUri.toString());
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{imageUri});
            }
        } else {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
        mUploadCallbackAboveL = null;
    }

    private static void updatePhotos(Activity activity) {
        // 该广播即使多发（即选取照片成功时也发送）也没有关系，只是唤醒系统刷新媒体文件
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
        activity.sendBroadcast(intent);
    }

    @NotNull
    public static String determineMimeType(@NotNull WebChromeClient.FileChooserParams fileChooserParams) {
        Log.i("duan==dapp", "determineMimeType");
        if (fileChooserParams == null || fileChooserParams.getAcceptTypes().length == 0)
            return "*/*"; // Allow anything
        String mime;
        String firstType = fileChooserParams.getAcceptTypes()[0];

        if (fileChooserParams.getAcceptTypes().length == 1) {
            mime = firstType;
            if (TextUtils.isEmpty(firstType)) {
                mime = "file/*";
            }
        } else {
            //TODO: Resolve types
            switch (firstType) {
                case "png":
                case "gif":
                case "svg":
                case "jpg":
                case "jpeg":
                case "image/*":
                    mime = "image/*";
                    break;

                case "mp4":
                case "x-msvideo":
                case "x-ms-wmv":
                case "mpeg4-generic":
                case "video/*":
                    mime = "video/*";
                    break;

                case "mpeg":
                case "aac":
                case "wav":
                case "ogg":
                case "midi":
                case "x-ms-wma":
                case "audio/*":
                    mime = "audio/*";
                    break;

                case "pdf":
                    mime = "application/*";
                    break;

                case "xml":
                case "csv":
                    mime = "text/*";
                    break;
                default:
                    mime = "*/*";
            }
        }
        return mime;
    }
}
