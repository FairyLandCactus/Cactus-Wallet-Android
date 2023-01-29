package com.qianlihu.solanawallet.solanadapp.bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/7/1310:39
 * desc   :
 * version: 1.0.0
 */
public class RemoveLiquidityBean {

    private String methods;
    private String number;
    private String amm;
    private String pool;
    private String poolAuthority;
    private String vault0;
    private String vault1;
    private String poolMint;
    private String feeTo;
    private String user0;
    private String user1;
    private String userPoolAta;
    private String owner;
    private String tokenProgram;

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    @Override
    public String toString() {
        return "RemoveLiquidityBean{" +
                "methods='" + methods + '\'' +
                ", number=" + number +
                ", amm='" + amm + '\'' +
                ", pool='" + pool + '\'' +
                ", poolAuthority='" + poolAuthority + '\'' +
                ", vault0='" + vault0 + '\'' +
                ", vault1='" + vault1 + '\'' +
                ", poolMint='" + poolMint + '\'' +
                ", feeTo='" + feeTo + '\'' +
                ", user0='" + user0 + '\'' +
                ", user1='" + user1 + '\'' +
                ", userPoolAta='" + userPoolAta + '\'' +
                ", owner='" + owner + '\'' +
                ", tokenProgram='" + tokenProgram + '\'' +
                '}';
    }
}
