package com.qianlihu.solanawallet.api;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.qianlihu.solanawallet.constant.Constant;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author : Duan
 * date   : 2021/6/39:56
 * desc   :
 * version: 1.0.0
 */
public class HttpService {

    public static int NOT_FOUND_ERROR_CODE = 404;
    public static int INTERNAL_SERVER_ERROR_CODE = 500;
    public static int GATEWAY_ERROR_CODE = 502;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static OkHttpClient client = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS).build();

    public static final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    public static OkHttpClient okHttpClient = new OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
            .cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                    cookieStore.put(httpUrl.host(), list);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                    List<Cookie> cookies = cookieStore.get(httpUrl.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            })
            .build();

    private OkHttpClient ok1HttpClient;

    public static OkHttpClient getInstance() {
        okHttpClient = new OkHttpClient.Builder().build(); // 闭环
        return okHttpClient;
    }

    public static void doGet(String url, LinkedHashMap<String, Object> map, Callback callback) {

        // 获取OkHttpClient对象
        OkHttpClient mHttpClient = getInstance();
        // 获取Request对象
        Request request = new Request.Builder()
                .url(attachHttpGetParams(url, map))
//                .header("Accept-Encoding","identity")
                .build();
        // 获取Call对象
        Call call = mHttpClient.newCall(request);
        // 执行异步请求
        call.enqueue(callback);
    }


    public static void post(String url, Object object, boolean isHeader, final Callback callback) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        RequestBody body = RequestBody.create(JSON, json);
        Log.i("duan==net", "提交数据是" + json);
        String language = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
        Request request = null;
        if (isHeader) {
            request = new Request.Builder()
                    .url(url)
                    .addHeader("language", language)
                    .post(body)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        }
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == NOT_FOUND_ERROR_CODE || response.code() == INTERNAL_SERVER_ERROR_CODE || response.code() == GATEWAY_ERROR_CODE) {
                    callback.onFailure(call, new IOException());
                } else {
                    callback.onResponse(call, response);
                }
            }
        });
    }

    public static void upload(Context context, String url, Map<String, String> map, MediaType mediaType, File file, Callback callback) {
        // form 表单形式上传
        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (file != null) {
            // MediaType.parse() 里面是上传的文件类型。
            RequestBody body = RequestBody.create(mediaType, file);
            // 参数分别为， 请求key ，文件名称 ， RequestBody
            requestBody.addFormDataPart("file", file.getName(), body);
        }
        if (map != null) {
            // map 里面是请求中所需要的 key 和 value
            for (Map.Entry entry : map.entrySet()) {
                requestBody.addFormDataPart(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody.build())
                .build();
        client.newCall(request).enqueue(callback);
    }


    /**
     * 为HttpGet 的 url 方便的添加多个name value 参数。
     *
     * @param url
     * @param params
     * @return
     */
    public static String attachHttpGetParams(String url, LinkedHashMap<String, Object> params) {

        Iterator<String> keys = params.keySet().iterator();
        Iterator<Object> values = params.values().iterator();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("?");

        for (int i = 0; i < params.size(); i++) {
            String value = null;
            try {
                value = URLEncoder.encode(values.next().toString(), "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }

            stringBuffer.append(keys.next() + "=" + value);
            if (i != params.size() - 1) {
                stringBuffer.append("&");
            }
        }
        Logger.i("url : { %s }", url + stringBuffer.toString());
        return url + stringBuffer.toString();
    }

}
