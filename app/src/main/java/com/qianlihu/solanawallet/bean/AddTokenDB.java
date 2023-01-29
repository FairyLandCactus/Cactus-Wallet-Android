package com.qianlihu.solanawallet.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * author : Duan
 * date   : 2022/1/415:20
 * desc   :
 * version: 1.0.0
 */
public class AddTokenDB extends LitePalSupport implements Serializable {

    private String walletAddress;
    private String tokenAddress;
    private String tokenAccount;
    private String symbol;
    private String name;
    private Integer decimals;
    private String logoURI;
    private double amount;
    private double usdt;
    private String walletType;
    private String shield; //隐藏代币标示
    private double usd;
    private double usdTotal;

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getTokenAddress() {
        return tokenAddress;
    }

    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public String getLogoURI() {
        return logoURI;
    }

    public void setLogoURI(String logoURI) {
        this.logoURI = logoURI;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getUsdt() {
        return usdt;
    }

    public void setUsdt(double usdt) {
        this.usdt = usdt;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getTokenAccount() {
        return tokenAccount;
    }

    public void setTokenAccount(String tokenAccount) {
        this.tokenAccount = tokenAccount;
    }

    public String getShield() {
        return shield;
    }

    public void setShield(String shield) {
        this.shield = shield;
    }

    public double getUsd() {
        return usd;
    }

    public void setUsd(double usd) {
        this.usd = usd;
    }

    public double getUsdTotal() {
        return usdTotal;
    }

    public void setUsdTotal(double usdTotal) {
        this.usdTotal = usdTotal;
    }

    @Override
    public String toString() {
        return "AddTokenDB{" +
                "walletAddress='" + walletAddress + '\'' +
                ", tokenAddress='" + tokenAddress + '\'' +
                ", tokenAccount='" + tokenAccount + '\'' +
                ", symbol='" + symbol + '\'' +
                ", name='" + name + '\'' +
                ", decimals=" + decimals +
                ", logoURI='" + logoURI + '\'' +
                ", amount=" + amount +
                ", usdt=" + usdt +
                ", walletType='" + walletType + '\'' +
                ", shield='" + shield + '\'' +
                ", usd=" + usd +
                ", usdTotal=" + usdTotal +
                '}';
    }
}
