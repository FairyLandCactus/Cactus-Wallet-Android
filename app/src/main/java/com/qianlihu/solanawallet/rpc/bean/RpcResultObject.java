package com.qianlihu.solanawallet.rpc.bean;

import com.squareup.moshi.Json;

public class RpcResultObject {
    public static class Context {
        @Json(name = "slot")
        private long slot;

        public long getSlot() {
            return slot;
        }

    }

    @Json(name = "context")
    protected Context context;

    public Context gContext() {
        return context;
    }


}
