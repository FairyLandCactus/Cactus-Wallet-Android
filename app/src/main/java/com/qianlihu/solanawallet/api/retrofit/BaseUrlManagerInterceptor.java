package com.qianlihu.solanawallet.api.retrofit;

import android.text.TextUtils;
import android.util.Log;

import com.qianlihu.solanawallet.constant.Constant;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author : Duan
 * date   : 2022/12/2317:34
 * desc   :
 * version: 1.0.0
 */
//拦截器
public class BaseUrlManagerInterceptor implements Interceptor {

    private String baseUrl;

    public BaseUrlManagerInterceptor(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取原始的originalRequest
        Request originalRequest = chain.request();
        //获取当前的url
        HttpUrl oldUrl = originalRequest.url();
        Log.e("aaa", "intercept:------------oldUrl---------->" + oldUrl);
        //获取originalRequest的创建者builder
        Request.Builder builder = originalRequest.newBuilder();
        //获取头信息的集合如：jeapp ,njeapp ,mall
        HttpUrl baseURL = HttpUrl.parse(baseUrl);
        //重建新的HttpUrl，需要重新设置的url部分
        HttpUrl newHttpUrl = oldUrl.newBuilder()
                .scheme(baseURL.scheme())//http协议如：http或者https
                .host(baseURL.host())//主机地址
                .port(baseURL.port())//端口
                .build();
        Log.e("aaa", "intercept:------scheme---- " + baseURL.scheme());
        Log.e("aaa", "intercept:-----host-----" + baseURL.host());
        Log.e("aaa", "intercept:-----port----- " + baseURL.port());
        //获取处理后的新newRequest
        Request newRequest = builder.url(newHttpUrl).build();
        return chain.proceed(newRequest);
    }
}

