package com.qianlihu.solanawallet.wallet_bean;

/**
 * author : Duan
 * date   : 2022/6/810:40
 * desc   :
 * version: 1.0.0
 */
public class PriceUsdtBean {

    /**
     * msg : 查询成功
     * code : 200
     * data : 309.575208014458
     */

    private String msg;
    private int code;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

    }
}
