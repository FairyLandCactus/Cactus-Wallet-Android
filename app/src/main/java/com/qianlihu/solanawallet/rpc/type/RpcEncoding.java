package com.qianlihu.solanawallet.rpc.type;

import com.squareup.moshi.Json;

/**
 * author : Duan
 * date   : 2021/12/2314:33
 * desc   :
 * version: 1.0.0
 */
public class RpcEncoding {
    @Json(name = "encoding")
    private String encoding;

    public RpcEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }
}
