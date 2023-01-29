package com.qianlihu.solanawallet.rpc;

import android.util.Log;

import com.qianlihu.solanawallet.event.ErrorEvent;
import com.qianlihu.solanawallet.rpc.type.RpcRequest;
import com.qianlihu.solanawallet.rpc.type.RpcResponse;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RpcClient {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private String endpoint;
    private OkHttpClient httpClient = new OkHttpClient();
    private RpcApi rpcApi;

    public RpcClient(Cluster endpoint) {
        this(endpoint.getEndpoint());
    }

    public RpcClient(String endpoint) {
        this.endpoint = endpoint;
        rpcApi = new RpcApi(this);
    }

    public <T> T call(String method, List<Object> params, Class<T> clazz) throws RpcException {
        RpcRequest rpcRequest = new RpcRequest(method, params);
        JsonAdapter<RpcRequest> rpcRequestJsonAdapter = new Moshi.Builder().build().adapter(RpcRequest.class);
        JsonAdapter<RpcResponse<T>> resultAdapter = new Moshi.Builder().build()
                .adapter(Types.newParameterizedType(RpcResponse.class, Type.class.cast(clazz)));

        Request request = new Request.Builder().url(endpoint)
                .post(RequestBody.create(JSON, rpcRequestJsonAdapter.toJson(rpcRequest))).build();

        try {
            Response response = httpClient.newCall(request).execute();
            String bodys = response.body().string();
            Log.i("duan==bodys", method + "  返回数据=== " + bodys);
            RpcResponse<T> rpcResult = resultAdapter.fromJson(bodys);
            if (rpcResult.getError() != null) {
                if (rpcResult.getError().getData() != null) {
                    if (rpcResult.getError().getData().getLogs().size() > 0) {
                        String errData = rpcResult.getError().getData().getLogs().toString();
                        EventBus.getDefault().post(new ErrorEvent(401, rpcResult.getError().getMessage() + errData));
                    } else {
                        EventBus.getDefault().post(new ErrorEvent(401, rpcResult.getError().getMessage()));
                    }
                } else {
                    EventBus.getDefault().post(new ErrorEvent(401, rpcResult.getError().getMessage()));
                }
                Log.i("duan==wallet", "错误码==  " + rpcResult.getError().getCode() + "\t 错误信息==  " + rpcResult.getError().getMessage());
                throw new RpcException(rpcResult.getError().getMessage());
            }

            return (T) rpcResult.getResult();
        } catch (IOException e) {
            EventBus.getDefault().post(new ErrorEvent(401, e.getMessage()));
            throw new RpcException(e.getMessage());
        }
    }

    public RpcApi getApi() {
        return rpcApi;
    }

    public String getEndpoint() {
        return endpoint;
    }

}
