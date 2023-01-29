package com.qianlihu.solanawallet.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.LinkAddress;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Patterns;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;

import androidx.annotation.RawRes;
import androidx.annotation.RequiresApi;
import androidx.collection.SimpleArrayMap;

import com.alphawallet.token.entity.ProviderTypedData;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.gyf.immersionbar.ImmersionBar;
import com.qianlihu.solanawallet.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import es.dmoral.toasty.Toasty;
import io.reactivex.annotations.NonNull;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * author : Duan
 * date   : 2021/4/89:27
 * desc   : 我的工具类
 * version: 1.0.0
 */
public class MyUtils {

    /**
     * unicode 转换成 utf-8
     *
     * @param theString
     * @return
     * @author fanhui
     * 2007-3-15
     */
    public static String unicodeToUtf8(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    /**
     * 配置状态栏
     */
    public static void configStatusBar(Activity context) {
        int barColor = 0;
        int barColow1 = R.color.black;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            barColor = context.getWindow().getStatusBarColor();
        }

        if (barColor == barColow1) {
            barColow1 = R.color.white;
        }
        ImmersionBar.with(context)
                .navigationBarColor(barColow1)
                .statusBarDarkFont(false)
                .navigationBarDarkIcon(true)
                .init();
    }

    public static String unicodeToUTF_8(String src) {
        if (null == src) {
            return null;
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < src.length(); ) {
            char c = src.charAt(i);
            if (i + 6 < src.length() && c == '\\' && src.charAt(i + 1) == 'u') {
                String hex = src.substring(i + 2, i + 6);
                try {
                    out.append((char) Integer.parseInt(hex, 16));
                } catch (NumberFormatException nfe) {
                    nfe.fillInStackTrace();
                }
                i = i + 6;
            } else {
                out.append(src.charAt(i));
                ++i;
            }
        }
        return out.toString();
    }


    /**
     * 将assets中的识别库复制到SD卡中
     *
     * @param path 要存放在SD卡中的 完整的文件名。这里是"/storage/emulated/0//tessdata/chi_sim.traineddata"
     * @param name assets中的文件名 这里是 "chi_sim.traineddata"
     */
    public static void copyToSD(String path, String name, Context context) {
        Log.i("TAG", "copyToSD: " + path);
        Log.i("TAG", "copyToSD: " + name);

        //如果存在就删掉
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        if (!f.exists()) {
            File p = new File(f.getParent());
            if (!p.exists()) {
                p.mkdirs();
            }
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = context.getAssets().open(name);
            File file = new File(path);
            os = new FileOutputStream(file);
            byte[] bytes = new byte[2048];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
                if (os != null)
                    os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @NonNull
    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将手机号码中间四位转换为 ****
     *
     * @param phoneNumber
     * @return
     */
    public static String changPhoneNumber(String phoneNumber) {
        StringBuffer sb = new StringBuffer();
        if (phoneNumber.length() > 10) {
            String frontThreeString = phoneNumber.substring(0, 3);
            sb.append(frontThreeString);
            String substring = phoneNumber.substring(3, 7);
            String replace = substring.replace(substring, "****");
            sb.append(replace);
            String lastFourString = phoneNumber.substring(7, 11);
            sb.append(lastFourString);
            return sb.toString();
        } else {
            return "";
        }
    }

    /**
     * 重启Activity
     *
     * @param activity
     */
    public static void resumeActividy(Activity activity) {
        Intent intent = activity.getIntent();
        activity.overridePendingTransition(0, 0);
        activity.finish();
        activity.overridePendingTransition(0, 0);
        startActivity(intent);
    }

    /**
     * 隐藏软键盘
     *
     * @param view :一般为EditText
     */
    public static void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 获取文件储存路径
     *
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }

    public static void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }

    /**
     * 判断服务是否正在运行
     *
     * @param serviceName 服务类的全路径名称 例如： com.jaychan.demo.service.PushService
     * @param context     上下文对象
     * @return
     */
    public static boolean isServiceRunning(String serviceName, Context context) {
        //活动管理器
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100); //获取运行的服务,参数表示最多返回的数量
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            String className = runningServiceInfo.service.getClassName();
            if (className.equals(serviceName)) {
                return true; //判断服务是否运行
            }
        }
        return false;
    }

    /**
     * 判断activity是否运行在栈顶
     *
     * @param context
     * @return
     */
    public static boolean isForeground(Context context, String PackageName) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> task = myManager.getRunningTasks(1);
        ComponentName componentInfo = task.get(0).topActivity;
        if (componentInfo.getPackageName().equals(PackageName))
            return true;
        return false;
    }

    /**
     * 判断当前程序是否在前台运行
     *
     * @param context
     * @return
     */
    public static boolean isAppForeground(Context context) {
        if (context != null) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : processes) {
                if (processInfo.processName.equals(context.getPackageName())) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取本机IP地址
     *
     * @return
     */
    public static String getIpAddressString() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface
                    .getNetworkInterfaces(); enNetI.hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }

    public static File getParentFile(@NonNull Context context) {
        final File externalSaveDir = context.getExternalCacheDir();
        if (externalSaveDir == null) {
            return context.getCacheDir();
        } else {
            return externalSaveDir;
        }
    }


    public static String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            // 奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            // 偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }


    /**
     * Integer.toBinaryString 32位二进制表示
     * int一共8个字节即32位
     * 最高位是符号位 从2的0次开始
     * 正数范围:2^31-1  2147483647
     * 负数范围:-2^31  -2147483648
     * 本身toBinaryString函数,输出负数是32位的
     */
    public static String intToBinary32(int num) {
        String binaryStr = Integer.toBinaryString(num);
        if (num >= 0) { //负数直接返回就行
            while (binaryStr.length() < 32) {
                binaryStr = "0" + binaryStr;
            }
        }
        return binaryStr;
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        assert wm != null;
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    /**
     * 截屏
     *
     * @param activity
     */
    public static void drawingCache(Activity activity, View shareView) {
//        View dView = activity.getWindow().getDecorView();
        View dView = shareView;
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        if (bitmap != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { //android 11
                    saveImageToGallery2(activity, bitmap);
                } else {
                    // 获取内置SD卡路径
                    String sdCardPath = Environment.getExternalStorageDirectory().getPath();
                    // 图片文件路径
                    String filePath = sdCardPath + File.separator + "screenshot.png";
                    File file = new File(filePath);
                    FileOutputStream os = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                    os.flush();
                    os.close();
                    Toasty.success(activity, "保存成功,请到相册获取!").show();
                }

            } catch (Exception e) {
            }
        }
    }

    //android 11 保存图片方法
    public static void saveImageToGallery2(Context context, Bitmap bitmap) {
        Long mImageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date(mImageTime));
        String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.png";//图片名称，以"Screenshot"+时间戳命名
        String mImageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);

        final ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES
                + File.separator + "dh"); //Environment.DIRECTORY_SCREENSHOTS:截图,图库中显示的文件夹名。"dh"
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, mImageFileName);
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        values.put(MediaStore.MediaColumns.DATE_ADDED, mImageTime / 1000);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, mImageTime / 1000);
        values.put(MediaStore.MediaColumns.DATE_EXPIRES, (mImageTime + DateUtils.DAY_IN_MILLIS) / 1000);
        values.put(MediaStore.MediaColumns.IS_PENDING, 1);

        ContentResolver resolver = context.getContentResolver();
        final Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            // First, write the actual data for our screenshot
            try (OutputStream out = resolver.openOutputStream(uri)) {
                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                    throw new IOException("Failed to compress");
                }
            }
            // Everything went well above, publish it!
            values.clear();
            values.put(MediaStore.MediaColumns.IS_PENDING, 0);
            values.putNull(MediaStore.MediaColumns.DATE_EXPIRES);
            resolver.update(uri, values, null, null);
            Toasty.success(context, "保存成功，请到相册获取！").show();
        } catch (IOException e) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                resolver.delete(uri, null);
            }
            Log.d("Exception", e.toString());
        }
    }

    /**
     * 保留小数
     *
     * @param db
     * @return
     */
    public static String decimalFormat2(double db) {
        DecimalFormat df = new DecimalFormat("0.00");
//        String amount = df.format(db);
        String amount = String.format(Locale.ENGLISH, "%.2f", db);
        BigDecimal bigDecimal = new BigDecimal(amount);
        BigDecimal bigDecimal1 = bigDecimal.stripTrailingZeros();
        return bigDecimal1.toPlainString();
    }

    public static String decimalFormat(double db) {
        DecimalFormat df = new DecimalFormat("0.0000");
        String amount = String.format(Locale.ENGLISH, "%.4f", db);
        BigDecimal bigDecimal = new BigDecimal(amount);
        BigDecimal bigDecimal1 = bigDecimal.stripTrailingZeros();
        return bigDecimal1.toPlainString();
    }

    public static String decimalFormat6(double db) {
        DecimalFormat df = new DecimalFormat("0.000000");
//        String amount = df.format(db);
        String amount = String.format(Locale.ENGLISH, "%.6f", db);
        BigDecimal bigDecimal = new BigDecimal(amount);
        BigDecimal bigDecimal1 = bigDecimal.stripTrailingZeros();
        return bigDecimal1.toPlainString();
    }

    public static String decimalFormat7(double db) {
        DecimalFormat df = new DecimalFormat("0.0000000");
//        String amount = df.format(db);
        String amount = String.format(Locale.ENGLISH, "%.7f", db);
        BigDecimal bigDecimal = new BigDecimal(amount);
        BigDecimal bigDecimal1 = bigDecimal.stripTrailingZeros();
        return bigDecimal1.toPlainString();
    }

    public static String decimalFormat10(double db) {
        DecimalFormat df = new DecimalFormat("0.0000000000");
//        String amount = df.format(db);
        String amount = String.format(Locale.ENGLISH, "%.10f", db);
        BigDecimal bigDecimal = new BigDecimal(amount);
        BigDecimal bigDecimal1 = bigDecimal.stripTrailingZeros();
        return bigDecimal1.toPlainString();
    }

    public static long readUint32(byte[] bytes, int offset) {
        return (long) bytes[offset] & 255L |
                ((long) bytes[offset + 1] & 255L) << 8 |
                ((long) bytes[offset + 2] & 255L) << 16 |
                ((long) bytes[offset + 3] & 255L) << 24;
    }

    public static long readUint(byte[] bytes, int offset, int byteslength) {
        long val = (long) bytes[offset];
        int mul = 1;
        int i = 0;
        while (++i < byteslength && (mul *= 0x100) != 0) {
            val += (long) bytes[offset + i] * mul;
        }
        return val;
    }

    private static final String INST_ID = "instId";
    private static final String LAST = "last";

    public static String tickerUsdt(String instId, List<Map<String, String>> tickerList) {
        String ticker = "0.0";
        if (instId.equals("SOL")) {
            instId = instId + "-USDT";
        }
        if (tickerList != null) {
            if (tickerList.size() > 0) {
                for (int i = 0; i < tickerList.size(); i++) {
                    if (tickerList.get(i).get(INST_ID).equals(instId)) {
                        ticker = tickerList.get(i).get(LAST);
                    }
                }
            }
        }
        return ticker;
    }

    private static Map<String, String> tempMap = null;

    public static List<String> subList2(List<String> list1, List<String> list2) {
        List<String> list = new ArrayList<>();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            tempMap = list2.parallelStream().collect(Collectors.toMap(Function.identity(), Function.identity(), (oldData, newData) -> newData));
            list = list1.parallelStream().filter(str -> {
                return !tempMap.containsKey(str);
            }).collect(Collectors.toList());
        }
        return list;
    }

    /**
     * 判断是否是json字符串
     *
     * @param jsonStr
     * @return
     */
    public static boolean validate(String jsonStr) {
        JsonElement jsonElement;
        try {
            jsonElement = new JsonParser().parse(jsonStr);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        if (!jsonElement.isJsonObject()) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否为16进制
     *
     * @param cadena
     * @return
     */
    public static boolean isNumeric(String cadena) {
        if (cadena.length() == 0 ||
                (cadena.charAt(0) != '-' && Character.digit(cadena.charAt(0), 16) == -1))
            return false;
        if (cadena.length() == 1 && cadena.charAt(0) == '-')
            return false;
        for (int i = 1; i < cadena.length(); i++)
            if (Character.digit(cadena.charAt(i), 16) == -1)
                return false;
        return true;
    }

    public static String loadFile(Context context, @RawRes int rawRes) {
        byte[] buffer = new byte[0];
        try {
            InputStream in = context.getResources().openRawResource(rawRes);
            buffer = new byte[in.available()];
            int len = in.read(buffer);
            if (len < 1) {
                throw new IOException("Nothing is read.");
            }
        } catch (Exception ex) {
            Log.d("READ_JS_TAG", "Ex", ex);
        }
        return new String(buffer);
    }

    public static CharSequence formatEIP721Message(StructuredDataEncoder messageData) {
        HashMap<String, Object> messageMap = (HashMap<String, Object>) messageData.jsonMessageObject.getMessage();
        StyledStringBuilder sb = new StyledStringBuilder();
        for (String entry : messageMap.keySet()) {
            sb.startStyleGroup().append(entry).append(":").append("\n");
            sb.setStyle(new StyleSpan(Typeface.BOLD));
            Object v = messageMap.get(entry);
            if (v instanceof LinkedHashMap) {
                HashMap<String, Object> valueMap = (HashMap<String, Object>) messageMap.get(entry);
                for (String paramName : valueMap.keySet()) {
                    String value = valueMap.get(paramName).toString();
                    sb.startStyleGroup().append(" ").append(paramName).append(": ");
                    sb.setStyle(new StyleSpan(Typeface.BOLD));
                    sb.append(value).append("\n");
                }
            } else {
                sb.append(" ").append(v.toString()).append("\n");
            }
        }

        sb.applyStyles();

        return sb;
    }

    public static CharSequence formatTypedMessage(ProviderTypedData[] rawData) {
        //produce readable text to display in the signing prompt
        StyledStringBuilder sb = new StyledStringBuilder();
        boolean firstVal = true;
        for (ProviderTypedData data : rawData) {
            if (!firstVal) sb.append("\n");
            sb.startStyleGroup().append(data.name).append(":");
            sb.setStyle(new StyleSpan(Typeface.BOLD));
            sb.append("\n  ").append(data.value.toString());
            firstVal = false;
        }

        sb.applyStyles();

        return sb;
    }

    public static String getDomainName(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (Exception e) {
            return url != null ? url : "";
        }
    }

    public static String formatUrl(String url) {
        if (URLUtil.isHttpsUrl(url) || URLUtil.isHttpUrl(url)) {
            return url;
        } else {
            if (isValidUrl(url)) {
                return "https://" + url;
            } else {
                return "https://duckduckgo.com/?q="+ url;
            }
        }
    }

    public static boolean isValidUrl(String url) {
        Pattern p = Patterns.WEB_URL;
        Matcher m = p.matcher(url.toLowerCase());
        return m.matches();
    }

    /**
     * 判断对象是否为空
     * @param obj
     * @return
     */
    public static boolean isEmpty(final Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence && obj.toString().length() == 0) {
            return true;
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SimpleArrayMap && ((SimpleArrayMap) obj).isEmpty()) {
            return true;
        }
        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        if (obj instanceof LongSparseArray && ((LongSparseArray) obj).size() == 0) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (obj instanceof android.util.LongSparseArray
                    && ((android.util.LongSparseArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    //获取ip地址
    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {    //3G/4G网络
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                }
            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {     // wifi
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = int2Sip(wifiInfo.getIpAddress());
                return ipAddress;
            } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {    //有线
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                Network network = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    network = mConnectivityManager.getActiveNetwork();
                    LinkProperties linkProperties = mConnectivityManager.getLinkProperties(network);
                    for (LinkAddress linkAddress : linkProperties.getLinkAddresses()) {
                        InetAddress address = linkAddress.getAddress();
                        if (address instanceof Inet4Address) {
                            return address.getHostAddress();
                        }
                    }
                }
                return "0.0.0.0";
            }
        } else {
            return "0.0.0.0";
        }
        return null;
    }

    /**
     * 将ip的整数形式转换成ip形式
     * @param ip
     * @return
     */
    public static String int2Sip(int ip) {
        StringBuilder sb = new StringBuilder();
        sb.append(ip & 0xFF).append(".");
        sb.append((ip >> 8) & 0xFF).append(".");
        sb.append((ip >> 16) & 0xFF).append(".");
        sb.append((ip >> 24) & 0xFF);
        return sb.toString();
    }


    public static void showSoftInput(Context context, View view) {
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}
