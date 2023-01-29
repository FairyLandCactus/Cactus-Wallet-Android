package com.qianlihu.solanawallet.bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/6/211:39
 * desc   :
 * version: 1.0.0
 */
public class DappSignDataBean {


    /**
     * accountAddress : HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq
     * associatedAddressY : 7unmQWWjVMHkjmhU8LswgMhmJABbcYwX4uny5ZsLCVwE
     * associatedAddressX : 4v5hnZKMLoSTQ5wqFaVTNVAwkNMNXpSAS3Y2bLEN89cd
     * XTokenTempAccountPubkey : vmjFMRAByKUM4XHuTvuocVjk29TcrizuaKWoxZ9YPtQ
     * initializerAccountPubkey : 4wsGXb8G1DWAgrgXQTqhkCHQKm4Vgs9RSuovyd5KQx6Y
     * initializerYTokenAccount : C7mGKkDWcCUKJNPePEWaBN168E2of6yGZewzuHzrREMJ
     * escrowAccountPubkey : 9u3vULF2p5AAZU8oyqUiK9RvTWeYcJhDh3ydp6d9MnDi
     * TOKEN_PROGRAM_ID : TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA
     * pad : 9kAfEXg6n8skZiXPpkxYkVYqQhGaesz9En4c8V8WRazE
     * programId : DKx3LkKkcBNA619n5ZDAcphXtE6GhrNM4kxzSRRJ7fbV
     * data : {"type":"Buffer","data":[1,0,45,49,1,0,0,0,0]}
     * to : DKx3LkKkcBNA619n5ZDAcphXtE6GhrNM4kxzSRRJ7fbV
     * from : HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq
     * paymentAmount : 20000000
     * getAmount : 22000000
     * takerX : AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU
     */

    private String accountAddress;
    private String associatedAddressY;
    private String associatedAddressX;
    private String XTokenTempAccountPubkey;
    private String initializerAccountPubkey;
    private String initializerYTokenAccount;
    private String escrowAccountPubkey;
    private String TOKEN_PROGRAM_ID;
    private String pad;
    private String programId;
    private DataBean data;
    private String to;
    private String from;
    private long paymentAmount;
    private long getAmount;
    private String takerX;
    private String takerY;
    private int accuracy;
    private String cionType;

    public String getAccountAddress() {
        return accountAddress;
    }

    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public String getAssociatedAddressY() {
        return associatedAddressY;
    }

    public void setAssociatedAddressY(String associatedAddressY) {
        this.associatedAddressY = associatedAddressY;
    }

    public String getAssociatedAddressX() {
        return associatedAddressX;
    }

    public void setAssociatedAddressX(String associatedAddressX) {
        this.associatedAddressX = associatedAddressX;
    }

    public String getXTokenTempAccountPubkey() {
        return XTokenTempAccountPubkey;
    }

    public void setXTokenTempAccountPubkey(String XTokenTempAccountPubkey) {
        this.XTokenTempAccountPubkey = XTokenTempAccountPubkey;
    }

    public String getInitializerAccountPubkey() {
        return initializerAccountPubkey;
    }

    public void setInitializerAccountPubkey(String initializerAccountPubkey) {
        this.initializerAccountPubkey = initializerAccountPubkey;
    }

    public String getInitializerYTokenAccount() {
        return initializerYTokenAccount;
    }

    public void setInitializerYTokenAccount(String initializerYTokenAccount) {
        this.initializerYTokenAccount = initializerYTokenAccount;
    }

    public String getEscrowAccountPubkey() {
        return escrowAccountPubkey;
    }

    public void setEscrowAccountPubkey(String escrowAccountPubkey) {
        this.escrowAccountPubkey = escrowAccountPubkey;
    }

    public String getTOKEN_PROGRAM_ID() {
        return TOKEN_PROGRAM_ID;
    }

    public void setTOKEN_PROGRAM_ID(String TOKEN_PROGRAM_ID) {
        this.TOKEN_PROGRAM_ID = TOKEN_PROGRAM_ID;
    }

    public String getPad() {
        return pad;
    }

    public void setPad(String pad) {
        this.pad = pad;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(long paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public long getGetAmount() {
        return getAmount;
    }

    public void setGetAmount(long getAmount) {
        this.getAmount = getAmount;
    }

    public String getTakerX() {
        return takerX;
    }

    public void setTakerX(String takerX) {
        this.takerX = takerX;
    }

    public static class DataBean {
        /**
         * type : Buffer
         * data : [1,0,45,49,1,0,0,0,0]
         */

        private String type;
        private List<Integer> data;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Integer> getData() {
            return data;
        }

        public void setData(List<Integer> data) {
            this.data = data;
        }
    }

    public String getTakerY() {
        return takerY;
    }

    public void setTakerY(String takerY) {
        this.takerY = takerY;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    public String getCionType() {
        return cionType;
    }

    public void setCionType(String cionType) {
        this.cionType = cionType;
    }
}
