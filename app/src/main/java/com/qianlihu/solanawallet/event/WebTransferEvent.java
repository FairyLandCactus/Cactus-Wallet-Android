package com.qianlihu.solanawallet.event;

/**
 * author : Duan
 * date   : 2022/5/417:38
 * desc   :
 * version: 1.0.0
 */
public class WebTransferEvent {

    private String jsonData;

    public WebTransferEvent(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
