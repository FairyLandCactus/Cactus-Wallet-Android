package com.qianlihu.solanawallet.solanadapp.bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/7/1310:43
 * desc   :
 * version: 1.0.0
 */
public class InitializePoolBean {

    private String methods;
    private String slip_molecule;//滑点分子
    private String slip_denominator;//滑点分母
    private String mint0;
    private String mint1;
    private String amm;
    private String ammFeeTo;
    private String pool;
    private String poolAuthority;
    private String vault0;
    private String vault1;
    private String poolMint;
    private String liquidityLocker;
    private String poolFeeTo;
    private String payer;
    private String systemProgram;
    private String tokenProgram;
    private String associatedTokenProgram;
    private String rent;

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getSlip_molecule() {
        return slip_molecule;
    }

    public void setSlip_molecule(String slip_molecule) {
        this.slip_molecule = slip_molecule;
    }

    public String getSlip_denominator() {
        return slip_denominator;
    }

    public void setSlip_denominator(String slip_denominator) {
        this.slip_denominator = slip_denominator;
    }

    public String getMint0() {
        return mint0;
    }

    public void setMint0(String mint0) {
        this.mint0 = mint0;
    }

    public String getMint1() {
        return mint1;
    }

    public void setMint1(String mint1) {
        this.mint1 = mint1;
    }

    public String getAmm() {
        return amm;
    }

    public void setAmm(String amm) {
        this.amm = amm;
    }

    public String getAmmFeeTo() {
        return ammFeeTo;
    }

    public void setAmmFeeTo(String ammFeeTo) {
        this.ammFeeTo = ammFeeTo;
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

    public String getPoolFeeTo() {
        return poolFeeTo;
    }

    public void setPoolFeeTo(String poolFeeTo) {
        this.poolFeeTo = poolFeeTo;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getSystemProgram() {
        return systemProgram;
    }

    public void setSystemProgram(String systemProgram) {
        this.systemProgram = systemProgram;
    }

    public String getTokenProgram() {
        return tokenProgram;
    }

    public void setTokenProgram(String tokenProgram) {
        this.tokenProgram = tokenProgram;
    }

    public String getAssociatedTokenProgram() {
        return associatedTokenProgram;
    }

    public void setAssociatedTokenProgram(String associatedTokenProgram) {
        this.associatedTokenProgram = associatedTokenProgram;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    @Override
    public String toString() {
        return "InitializePoolBean{" +
                "methods='" + methods + '\'' +
                ", slip_molecule=" + slip_molecule +
                ", slip_denominator=" + slip_denominator +
                ", mint0='" + mint0 + '\'' +
                ", mint1='" + mint1 + '\'' +
                ", amm='" + amm + '\'' +
                ", ammFeeTo='" + ammFeeTo + '\'' +
                ", pool='" + pool + '\'' +
                ", poolAuthority='" + poolAuthority + '\'' +
                ", vault0='" + vault0 + '\'' +
                ", vault1='" + vault1 + '\'' +
                ", poolMint='" + poolMint + '\'' +
                ", liquidityLocker='" + liquidityLocker + '\'' +
                ", poolFeeTo='" + poolFeeTo + '\'' +
                ", payer='" + payer + '\'' +
                ", systemProgram='" + systemProgram + '\'' +
                ", tokenProgram='" + tokenProgram + '\'' +
                ", associatedTokenProgram='" + associatedTokenProgram + '\'' +
                ", rent='" + rent + '\'' +
                '}';
    }
}
