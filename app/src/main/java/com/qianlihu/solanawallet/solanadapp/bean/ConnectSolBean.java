package com.qianlihu.solanawallet.solanadapp.bean;

import java.util.UUID;

/**
 * author : Duan
 * date   : 2022/7/1817:37
 * desc   :
 * version: 1.0.0
 */
public class ConnectSolBean {


    /**
     * jsonrpc : 2.0
     * id : 1
     * method : connected
     * params : {"publicKey":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","targetOrigin":"https://solflare.com/provider#origin=https%3A%2F%2Ftrade.mango.markets&network=mainnet-beta"}
     */

    private String jsonrpc = "2.0";
//    private String id = UUID.randomUUID().toString();
    private String method = "connected";
    private ParamsBean params;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }

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
         * publicKey : HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq
         * targetOrigin : https://solflare.com/provider#origin=https%3A%2F%2Ftrade.mango.markets&network=mainnet-beta
         */

        private String publicKey;
//        private String targetOrigin;

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

//        public String getTargetOrigin() {
//            return targetOrigin;
//        }
//
//        public void setTargetOrigin(String targetOrigin) {
//            this.targetOrigin = targetOrigin;
//        }

        @Override
        public String toString() {
            return "ParamsBean{" +
                    "publicKey='" + publicKey + '\'' +
//                    ", targetOrigin='" + targetOrigin + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ConnectSolBean{" +
                "jsonrpc='" + jsonrpc + '\'' +
//                ", id='" + id + '\'' +
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }
}
