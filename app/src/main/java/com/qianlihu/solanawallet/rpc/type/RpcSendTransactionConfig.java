package com.qianlihu.solanawallet.rpc.type;

import com.squareup.moshi.Json;

public class RpcSendTransactionConfig {

    public static enum Encoding {
        base58("base58");
        private String enc;
        Encoding(String enc) {
            this.enc = enc;
        }
        public String getEncoding() {
            return enc;
        }

        @Override
        public String toString() {
            return "Encoding{" +
                    "enc='" + enc + '\'' +
                    '}';
        }
    }

    @Json(name = "encoding")
    private Encoding encoding = Encoding.base58;

    @Override
    public String toString() {
        return "RpcSendTransactionConfig{" +
                "encoding=" + encoding +
                '}';
    }
}
