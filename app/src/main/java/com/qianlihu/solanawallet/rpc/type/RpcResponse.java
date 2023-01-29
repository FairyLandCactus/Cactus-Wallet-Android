package com.qianlihu.solanawallet.rpc.type;

import com.squareup.moshi.Json;

import java.util.List;

public class RpcResponse<T> {

    public static class Error {
        @Json(name = "code")
        private long code;
        @Json(name = "message")
        private String message;
        @Json(name = "data")
        private DataBean data;

        public long getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public DataBean getData() {
            return data;
        }

        public static class DataBean {
            @Json(name = "logs")
            private List<String> logs;

            public List<String> getLogs() {
                return logs;
            }

            public void setLogs(List<String> logs) {
                this.logs = logs;
            }
        }

    }

    @Json(name = "jsonrpc")
    private String jsonrpc;
    @Json(name = "result")
    private T result;
    @Json(name = "error")
    private Error error;
    @Json(name = "id")
    private String id;

    public Error getError() {
        return error;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getId() {
        return id;
    }

}
