package com.qianlihu.solanawallet.ethereum.bean;

/**
 * author : Duan
 * date   : 2022/7/2516:23
 * desc   :
 * version: 1.0.0
 */
public class EthRecodeResponse<T> {

    private String status;
    private String message;
    private T result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
