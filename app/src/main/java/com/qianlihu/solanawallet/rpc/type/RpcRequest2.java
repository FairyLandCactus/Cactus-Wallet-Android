package com.qianlihu.solanawallet.rpc.type;

public class RpcRequest2 {

    /**
     * jsonrpc : 2.0
     * method : connected
     * params : {"publicKey":"EdWqEgu54Zezi4E6L72RxAMPr5SWAyt2vpZWgvPYQTLh"}
     */

    private String jsonrpc = "2.0";
    private String method;
    private ParamsBean params;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean {
        /**
         * publicKey : EdWqEgu54Zezi4E6L72RxAMPr5SWAyt2vpZWgvPYQTLh
         */

        private String publicKey;
        private String targetOrigin;

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getTargetOrigin() {
            return targetOrigin;
        }

        public void setTargetOrigin(String targetOrigin) {
            this.targetOrigin = targetOrigin;
        }

        @Override
        public String toString() {
            return "ParamsBean{" +
                    "publicKey='" + publicKey + '\'' +
                    ", targetOrigin='" + targetOrigin + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RpcRequest2{" +
                "jsonrpc='" + jsonrpc + '\'' +
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }
}
