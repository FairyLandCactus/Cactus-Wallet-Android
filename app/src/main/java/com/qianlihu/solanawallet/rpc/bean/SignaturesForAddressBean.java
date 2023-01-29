package com.qianlihu.solanawallet.rpc.bean;

import com.squareup.moshi.Json;

/**
 * author : Duan
 * date   : 2021/12/1715:12
 * desc   :
 * version: 1.0.0
 */
public class SignaturesForAddressBean extends RpcResultObject {

    @Json(name = "err")
    private String err;
    @Json(name = "memo")
    private String memo;
    @Json(name = "signature")
    private String signature;
    @Json(name = "slot")
    private long slot;
    @Json(name = "blockTime")
    private String blockTime;

    public String getErr() {
        return err;
    }

    public String getMemo() {
        return memo;
    }

    public String getSignature() {
        return signature;
    }

    public long getSlot() {
        return slot;
    }

    public String getBlockTime() {
        return blockTime;
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
