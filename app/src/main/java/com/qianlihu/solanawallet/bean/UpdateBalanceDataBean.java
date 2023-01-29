package com.qianlihu.solanawallet.bean;

/**
 * author : Duan
 * date   : 2022/4/209:57
 * desc   : 转账更新余额数据
 * version: 1.0.0
 */
public class UpdateBalanceDataBean {

    private String puk;
    private String token;
    private double amount;

    public String getPuk() {
        return puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
