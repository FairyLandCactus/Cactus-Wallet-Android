package com.qianlihu.solanawallet.api.retrofit;

import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.MyUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * author : Duan
 * date   : 2021/4/89:17
 * desc   :
 * version: 1.0.0
 */
public class ApiRetrofit {

    public final String BASE_SERVER_URL = Constant.WALLET_BASE_URL;

    private static ApiRetrofit apiRetrofit;
    private Retrofit retrofit;
    private OkHttpClient client;
    private ApiServer apiServer;

    private String TAG = "ApiRetrofit";

    private String urlStr() {
        String baseUrl = BASE_SERVER_URL;
        return baseUrl;
    }

    /**
     * 请求访问quest
     * response拦截器
     */
    private Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(chain.request());
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            Log.e(TAG, "----------Request Start----------------");
            Log.e(TAG, "| " + request.toString() + request.headers().toString());
            Log.e(TAG, "| Response:" + MyUtils.unicodeToUTF_8(content));
            Log.e(TAG, "----------Request End:" + duration + "毫秒----------");
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        }
    };


    public ApiRetrofit() {
        client = new OkHttpClient.Builder()
                //添加log拦截器
//                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new LoggingInterceptor())
//                .addInterceptor(new BaseUrlManagerInterceptor(baseUrl))
                .connectTimeout(Constant.READ_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(Constant.READ_TIME_OUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(urlStr())
                //添加自定义的解析器
                .addConverterFactory(BaseConverterFactory.create())
                //支持RxJava2
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();

        apiServer = retrofit.create(ApiServer.class);
    }

    public static ApiRetrofit getInstance() {
        if (apiRetrofit == null) {
            synchronized (Object.class) {
                if (apiRetrofit == null) {
                    apiRetrofit = new ApiRetrofit();
                }
            }
        }
        return apiRetrofit;
    }

    public ApiServer getApiService() {
        return apiServer;
    }
}
