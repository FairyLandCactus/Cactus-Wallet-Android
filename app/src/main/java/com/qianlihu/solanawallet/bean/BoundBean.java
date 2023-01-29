package com.qianlihu.solanawallet.bean;

/**
 * author : Duan
 * date   : 2022/8/1210:50
 * desc   :
 * version: 1.0.0
 */
public class BoundBean {

    private String msg;
    private int code;
    private boolean flag;
    private double data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public double getData() {
        return data;
    }

    public void setData(double data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BoundBean{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", flag=" + flag +
                ", data='" + data + '\'' +
                '}';
    }
}
