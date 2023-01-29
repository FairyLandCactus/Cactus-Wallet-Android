package com.qianlihu.solanawallet.ws;

import android.util.Log;

import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.rpc.type.RpcRequest;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * author : Duan
 * date   : 2022/4/1310:54
 * desc   : websocket 管理类
 * version: 1.0.0
 */
public class WebsocketManager {

    private  JWebSocketClient client;

    public void connect(String url, IWebsocketCallback callback) {
        URI uri = URI.create(url);
        client = new JWebSocketClient(uri){
            @Override
            public void onMessage(String message) {
                super.onMessage(message);
                callback.onMessage(message);
            }

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                super.onOpen(handshakedata);
                callback.onOpen();
            }

            @Override
            public void onError(Exception ex) {
                super.onError(ex);
                callback.onError(ex);
            }
        };
//        client.setConnectionLostTimeout(30000);
        if (client.isOpen()) {
            return;
        }
        client.connect();
//        Log.i("duan==swap", "connect === 已连接 " );
    }

    public void reconnectBlocking() {
        if (client != null) {
            if (!client.isClosed()) {
                return;
            }
            try {
                client.reconnectBlocking();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public  void close() {
        if (client != null) {
            client.close();
//            Log.i("duan==swap", "connect === 断开连接 " );
        }
    }

    public void sendMsg(String method, String msg) {
        List<Object> params = new ArrayList<Object>();
        params.add(msg);
        RpcRequest rpcRequest = new RpcRequest(method, params);
        JsonAdapter<RpcRequest> rpcRequestJsonAdapter = new Moshi.Builder().build().adapter(RpcRequest.class);
        String jsonStr = rpcRequestJsonAdapter.toJson(rpcRequest);
        client.send(jsonStr);
    }
}
