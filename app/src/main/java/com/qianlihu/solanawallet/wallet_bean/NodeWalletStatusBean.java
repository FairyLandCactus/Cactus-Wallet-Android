package com.qianlihu.solanawallet.wallet_bean;

/**
 * author : Duan
 * date   : 2022/10/2818:06
 * desc   : 获取钱包节点状态实体类
 * version: 1.0.0
 */
public class NodeWalletStatusBean {

    private int code;
    private String msg;
    private String time;
    private int data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
