package com.qianlihu.solanawallet.api.retrofit;

import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.qianlihu.solanawallet.constant.Constant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request.Builder requestBuilder = original.newBuilder()
                .header("Accept-Encoding", "identity");
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }

}
