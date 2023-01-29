package com.qianlihu.solanawallet.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * author : Duan
 * date   : 2022/4/1410:30
 * desc   : 以太坊转账记录表
 * version: 1.0.0
 */
public class EthTransferRecordDB extends LitePalSupport implements Serializable {

    private String myPuk;
    private String walletType;
    private String timeStamp;
    private String hash;
    private String from;
    private String contractAddress;
    private String to;
    private String value;
    private String tokenDecimal;
    private String gasPrice;
    private String gasUsed;
    private String input;

    public String getMyPuk() {
        return myPuk;
    }

    public void setMyPuk(String myPuk) {
        this.myPuk = myPuk;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTokenDecimal() {
        return tokenDecimal;
    }

    public void setTokenDecimal(String tokenDecimal) {
        this.tokenDecimal = tokenDecimal;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    @Override
    public String toString() {
        return "BNBTransferRecordDB{" +
                "myPuk='" + myPuk + '\'' +
                ", walletType='" + walletType + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", hash='" + hash + '\'' +
                ", from='" + from + '\'' +
                ", contractAddress='" + contractAddress + '\'' +
                ", to='" + to + '\'' +
                ", value='" + value + '\'' +
                ", tokenDecimal='" + tokenDecimal + '\'' +
                ", gasPrice='" + gasPrice + '\'' +
                ", gasUsed='" + gasUsed + '\'' +
                ", input='" + input + '\'' +
                '}';
    }
}
