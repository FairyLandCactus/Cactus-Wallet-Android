package com.qianlihu.solanawallet.solanadapp.bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/7/1310:46
 * desc   :
 * version: 1.0.0
 */
public class SwapBean {

    private String methods;
    private String amount;
    private String slip;//最高滑点
    private String time;//最大交易时间
    private String amm;
    private String pool;
    private String poolAuthority;
    private String vaultSrc;
    private String vaultDst;
    private String userSrc;
    private String userDst;
    private String owner;
    private String tokenProgram;
    private String coin1_address;
    private String coin2_address;
    private String coin1_logo;
    private String coin2_logo;
    private String coin1_number;
    private String coin2_number;
    private SolanaBean solana;
    private TradeBean trade;

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSlip() {
        return slip;
    }

    public void setSlip(String slip) {
        this.slip = slip;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getVaultSrc() {
        return vaultSrc;
    }

    public void setVaultSrc(String vaultSrc) {
        this.vaultSrc = vaultSrc;
    }

    public String getVaultDst() {
        return vaultDst;
    }

    public void setVaultDst(String vaultDst) {
        this.vaultDst = vaultDst;
    }

    public String getUserSrc() {
        return userSrc;
    }

    public void setUserSrc(String userSrc) {
        this.userSrc = userSrc;
    }

    public String getUserDst() {
        return userDst;
    }

    public void setUserDst(String userDst) {
        this.userDst = userDst;
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

    public String getCoin1_address() {
        return coin1_address;
    }

    public void setCoin1_address(String coin1_address) {
        this.coin1_address = coin1_address;
    }

    public String getCoin2_address() {
        return coin2_address;
    }

    public void setCoin2_address(String coin2_address) {
        this.coin2_address = coin2_address;
    }

    public String getCoin1_logo() {
        return coin1_logo;
    }

    public void setCoin1_logo(String coin1_logo) {
        this.coin1_logo = coin1_logo;
    }

    public String getCoin2_logo() {
        return coin2_logo;
    }

    public void setCoin2_logo(String coin2_logo) {
        this.coin2_logo = coin2_logo;
    }

    public String getCoin1_number() {
        return coin1_number;
    }

    public void setCoin1_number(String coin1_number) {
        this.coin1_number = coin1_number;
    }

    public String getCoin2_number() {
        return coin2_number;
    }

    public void setCoin2_number(String coin2_number) {
        this.coin2_number = coin2_number;
    }

    public static class SolanaBean {
        private String lood_mint_ata;
        private long swap_amount;

        public String getLood_mint_ata() {
            return lood_mint_ata;
        }

        public void setLood_mint_ata(String lood_mint_ata) {
            this.lood_mint_ata = lood_mint_ata;
        }

        public long getSwap_amount() {
            return swap_amount;
        }

        public void setSwap_amount(long swap_amount) {
            this.swap_amount = swap_amount;
        }

        @Override
        public String toString() {
            return "SolanaBean{" +
                    "lood_mint_ata='" + lood_mint_ata + '\'' +
                    ", swap_amount=" + swap_amount +
                    '}';
        }
    }

    public SolanaBean getSolana() {
        return solana;
    }

    public void setSolana(SolanaBean solana) {
        this.solana = solana;
    }

    public static class TradeBean{
        private String fee_address;
        private long fee_number;
        private String fee_coin;

        public String getFee_address() {
            return fee_address;
        }

        public void setFee_address(String fee_address) {
            this.fee_address = fee_address;
        }

        public long getFee_number() {
            return fee_number;
        }

        public void setFee_number(long fee_number) {
            this.fee_number = fee_number;
        }

        public String getFee_coin() {
            return fee_coin;
        }

        public void setFee_coin(String fee_coin) {
            this.fee_coin = fee_coin;
        }
    }

    public TradeBean getTrade() {
        return trade;
    }

    public void setTrade(TradeBean trade) {
        this.trade = trade;
    }

    @Override
    public String toString() {
        return "SwapBean{" +
                "methods='" + methods + '\'' +
                ", amount='" + amount + '\'' +
                ", slip='" + slip + '\'' +
                ", time='" + time + '\'' +
                ", amm='" + amm + '\'' +
                ", pool='" + pool + '\'' +
                ", poolAuthority='" + poolAuthority + '\'' +
                ", vaultSrc='" + vaultSrc + '\'' +
                ", vaultDst='" + vaultDst + '\'' +
                ", userSrc='" + userSrc + '\'' +
                ", userDst='" + userDst + '\'' +
                ", owner='" + owner + '\'' +
                ", tokenProgram='" + tokenProgram + '\'' +
                ", coin1_address='" + coin1_address + '\'' +
                ", coin2_address='" + coin2_address + '\'' +
                ", coin1_logo='" + coin1_logo + '\'' +
                ", coin2_logo='" + coin2_logo + '\'' +
                ", coin1_number='" + coin1_number + '\'' +
                ", coin2_number='" + coin2_number + '\'' +
                ", solana=" + solana +
                '}';
    }
}
