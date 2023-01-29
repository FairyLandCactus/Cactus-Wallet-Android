package com.qianlihu.solanawallet.rpc.type;

import com.squareup.moshi.Json;

public class RpcSendTransactionConfig2 {

    public static enum Encoding {
        base64("base64");

        private String enc;

        Encoding(String enc) {
            this.enc = enc;
        }

        public String getEncoding() {
            return enc;
        }

    }

    @Json(name = "encoding")
    private Encoding encoding = Encoding.base64;

}
