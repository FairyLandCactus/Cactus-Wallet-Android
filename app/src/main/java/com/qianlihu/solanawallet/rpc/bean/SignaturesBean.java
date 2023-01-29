package com.qianlihu.solanawallet.rpc.bean;

import com.squareup.moshi.Json;

/**
 * author : Duan
 * date   : 2021/12/1715:12
 * desc   :
 * version: 1.0.0
 */
public class SignaturesBean{

    private String err;
    private String memo;
    private String signature;
    private long slot;
    private String blockTime;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public long getSlot() {
        return slot;
    }

    public void setSlot(long slot) {
        this.slot = slot;
    }

    public String getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(String blockTime) {
        this.blockTime = blockTime;
    }

    @Override
    public String toString() {
        return "SignaturesForAddressBean{" +
                "err='" + err + '\'' +
                ", memo='" + memo + '\'' +
                ", signature='" + signature + '\'' +
                ", slot=" + slot +
                ", blockTime='" + blockTime + '\'' +
                '}';
    }
}
