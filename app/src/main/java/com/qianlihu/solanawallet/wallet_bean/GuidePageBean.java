package com.qianlihu.solanawallet.wallet_bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/10/2611:22
 * desc   : 引导页图片获取实体类
 * version: 1.0.0
 */
public class GuidePageBean {

    private int code;
    private String msg;
    private String time;
    private List<String> data;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
