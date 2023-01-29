package com.qianlihu.solanawallet.bean;

import com.qianlihu.solanawallet.rpc.bean.TransactionBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/1011:30
 * desc   :
 * version: 1.0.0
 */
public class RpcResultBean {

    private String jsonrpc;
    private String id;
    private TransactionBean result;


    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TransactionBean getResult() {
        return result;
    }

    public void setResult(TransactionBean result) {
        this.result = result;
    }
}
