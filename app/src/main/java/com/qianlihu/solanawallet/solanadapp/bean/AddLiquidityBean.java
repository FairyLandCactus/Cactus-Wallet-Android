package com.qianlihu.solanawallet.solanadapp.bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/7/1310:32
 * desc   :
 * version: 1.0.0
 */
public class AddLiquidityBean {

    private String methods;
    private String number1;
    private String number2;
    private String amm;
    private String pool;
    private String poolAuthority;
    private String vault0;
    private String vault1;
    private String poolMint;
    private String liquidityLocker;
    private String feeTo;
    private String user0;
    private String user1;
    private String userPoolAta;
    private String owner;
    private String tokenProgram;
    private String coin1;
    private String coin2;
    private String outputNumber1;
    private String outputNumber2;
    private String coinLogo1;
    private String coinLogo2;
    private SolanaBean solana;

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getNumber1() {
        return number1;
    }

    public void setNumber1(String number1) {
        this.number1 = number1;
    }

    public String getNumber2() {
        return number2;
    }

    public void setNumber2(String number2) {
        this.number2 = number2;
    }

    public String getAmm() {
        return amm;
    }

    public void setAmm(String amm) {
        this.amm = amm;
    }

    public String getPool() {
        return pool;
    }

    public void setPool(String pool) {
        this.pool = pool;
    }

    public String getPoolAuthority() {
        return poolAuthority;
    }

    public void setPoolAuthority(String poolAuthority) {
        this.poolAuthority = poolAuthority;
    }

    public String getVault0() {
        return vault0;
    }

    public void setVault0(String vault0) {
        this.vault0 = vault0;
    }

    public String getVault1() {
        return vault1;
    }

    public void setVault1(String vault1) {
        this.vault1 = vault1;
    }

    public String getPoolMint() {
        return poolMint;
    }

    public void setPoolMint(String poolMint) {
        this.poolMint = poolMint;
    }

    public String getLiquidityLocker() {
        return liquidityLocker;
    }

    public void setLiquidityLocker(String liquidityLocker) {
        this.liquidityLocker = liquidityLocker;
    }

    public String getFeeTo() {
        return feeTo;
    }

    public void setFeeTo(String feeTo) {
        this.feeTo = feeTo;
    }

    public String getUser0() {
        return user0;
    }

    public void setUser0(String user0) {
        this.user0 = user0;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUserPoolAta() {
        return userPoolAta;
    }

    public void setUserPoolAta(String userPoolAta) {
        this.userPoolAta = userPoolAta;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTokenProgram() {
        return tokenProgram;
    }

    public void setTokenProgram(String tokenProgram) {
        this.tokenProgram = tokenProgram;
    }

    public String getCoin1() {
        return coin1;
    }

    public void setCoin1(String coin1) {
        this.coin1 = coin1;
    }

    public String getCoin2() {
        return coin2;
    }

    public void setCoin2(String coin2) {
        this.coin2 = coin2;
    }

    public String getOutputNumber1() {
        return outputNumber1;
    }

    public void setOutputNumber1(String outputNumber1) {
        this.outputNumber1 = outputNumber1;
    }

    public String getOutputNumber2() {
        return outputNumber2;
    }

    public void setOutputNumber2(String outputNumber2) {
        this.outputNumber2 = outputNumber2;
    }

    public String getCoinLogo1() {
        return coinLogo1;
    }

    public void setCoinLogo1(String coinLogo1) {
        this.coinLogo1 = coinLogo1;
    }

    public String getCoinLogo2() {
        return coinLogo2;
    }

    public void setCoinLogo2(String coinLogo2) {
        this.coinLogo2 = coinLogo2;
    }

    public SolanaBean getSolana() {
        return solana;
    }

    public void setSolana(SolanaBean solana) {
        this.solana = solana;
    }

    public static class SolanaBean {
        private String user;
        private long number;

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public long getNumber() {
            return number;
        }

        public void setNumber(long number) {
            this.number = number;
        }
    }

    @Override
    public String toString() {
        return "AddLiquidityBean{" +
                "methods='" + methods + '\'' +
                ", number1='" + number1 + '\'' +
                ", number2='" + number2 + '\'' +
                ", amm='" + amm + '\'' +
                ", pool='" + pool + '\'' +
                ", poolAuthority='" + poolAuthority + '\'' +
                ", vault0='" + vault0 + '\'' +
                ", vault1='" + vault1 + '\'' +
                ", poolMint='" + poolMint + '\'' +
                ", liquidityLocker='" + liquidityLocker + '\'' +
                ", feeTo='" + feeTo + '\'' +
                ", user0='" + user0 + '\'' +
                ", user1='" + user1 + '\'' +
                ", userPoolAta='" + userPoolAta + '\'' +
                ", owner='" + owner + '\'' +
                ", tokenProgram='" + tokenProgram + '\'' +
                ", coin1='" + coin1 + '\'' +
                ", coin2='" + coin2 + '\'' +
                ", outputNumber1='" + outputNumber1 + '\'' +
                ", outputNumber2='" + outputNumber2 + '\'' +
                ", coinLogo1='" + coinLogo1 + '\'' +
                ", coinLogo2='" + coinLogo2 + '\'' +
                ", solana=" + solana +
                '}';
    }
}
