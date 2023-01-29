package com.qianlihu.solanawallet.bean;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * author : Duan
 * date   : 2021/12/2110:02
 * desc   : 交易记录数据字段表实体类
 * version: 1.0.0
 */
public class TransactionDB extends LitePalSupport implements Serializable {

    private String myPuk;
    private String money;
    private String usdt;
    private String fee;
    private String signatures;
    private Integer flag;
    private Long dateTime;
    private String mint;
    private String walletType;
    private String remark;
    private String from;
    private String to;

    public String getMyPuk() {
        return myPuk;
    }

    public void setMyPuk(String myPuk) {
        this.myPuk = myPuk;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getUsdt() {
        return usdt;
    }

    public void setUsdt(String usdt) {
        this.usdt = usdt;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getSignatures() {
        return signatures;
    }

    public void setSignatures(String signatures) {
        this.signatures = signatures;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public String getMint() {
        return mint;
    }

    public void setMint(String mint) {
        this.mint = mint;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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

    @Override
    public String toString() {
        return "TransactionDB{" +
                "myPuk='" + myPuk + '\'' +
                ", money='" + money + '\'' +
                ", usdt='" + usdt + '\'' +
                ", fee='" + fee + '\'' +
                ", signatures='" + signatures + '\'' +
                ", flag=" + flag +
                ", dateTime=" + dateTime +
                ", mint='" + mint + '\'' +
                ", walletType='" + walletType + '\'' +
                ", remark='" + remark + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                '}';
    }
}
