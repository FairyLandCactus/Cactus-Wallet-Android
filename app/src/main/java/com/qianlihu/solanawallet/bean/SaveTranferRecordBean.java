package com.qianlihu.solanawallet.bean;

/**
 * author : Duan
 * date   : 2022/3/717:28
 * desc   :
 * version: 1.0.0
 */
public class SaveTranferRecordBean {

    private String from;
    private String to;
    private String token;
    private double amount;
    private String fee;
    private String time;
    private String memo;
    private String signatures;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
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

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSignatures() {
        return signatures;
    }

    public void setSignatures(String signatures) {
        this.signatures = signatures;
    }

    @Override
    public String toString() {
        return "SaveTranferRecordBean{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", token='" + token + '\'' +
                ", amount=" + amount +
                ", fee=" + fee +
                ", time='" + time + '\'' +
                ", memo='" + memo + '\'' +
                ", signatures='" + signatures + '\'' +
                '}';
    }
}
