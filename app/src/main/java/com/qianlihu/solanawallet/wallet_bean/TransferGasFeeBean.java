package com.qianlihu.solanawallet.wallet_bean;

/**
 * author : Duan
 * date   : 2022/11/89:55
 * desc   :
 * version: 1.0.0
 */
public class TransferGasFeeBean {

    private String speed;
    private double usdFee;
    private double gasFee;
    private String time;
    private double gas;
    private Long limit;

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public double getUsdFee() {
        return usdFee;
    }

    public void setUsdFee(double usdFee) {
        this.usdFee = usdFee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getGas() {
        return gas;
    }

    public void setGas(double gas) {
        this.gas = gas;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public double getGasFee() {
        return gasFee;
    }

    public void setGasFee(double gasFee) {
        this.gasFee = gasFee;
    }
}
