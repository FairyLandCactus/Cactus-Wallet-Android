package com.qianlihu.solanawallet.ws;

/**
 * author : Duan
 * date   : 2022/4/1311:00
 * desc   :
 * version: 1.0.0
 */
public interface IWebsocketCallback {

    void onMessage(String msg);

    void onOpen();

    void onError(Exception ex);
}
