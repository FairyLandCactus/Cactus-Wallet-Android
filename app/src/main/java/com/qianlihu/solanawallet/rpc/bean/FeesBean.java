package com.qianlihu.solanawallet.rpc.bean;

import com.squareup.moshi.Json;

/**
 * author : Duan
 * date   : 2021/12/1714:01
 * desc   :
 * version: 1.0.0
 */
public class FeesBean extends RpcResultObject {

    public static class FeeCalculator {

        @Json(name = "lamportsPerSignature")
        private long lamportsPerSignature;

        public long getLamportsPerSignature() {
            return lamportsPerSignature;
        }

    }

    public static class Value {
        @Json(name = "blockhash")
        private String blockhash;
        @Json(name = "feeCalculator")
        private RecentBlockhash.FeeCalculator feeCalculator;

        public void setBlockhash(String blockhash) {
            this.blockhash = blockhash;
        }

        public void setFeeCalculator(RecentBlockhash.FeeCalculator feeCalculator) {
            this.feeCalculator = feeCalculator;
        }
    }

    @Json(name = "value")
    private Value value;
    @Json(name = "lastValidSlot")
    private String lastValidSlot;
    @Json(name = "lastValidBlockHeight")
    private String lastValidBlockHeight;

    public Value getValue() {
        return value;
    }

    public String getLastValidSlot() {
        return lastValidSlot;
    }

    public String getLastValidBlockHeight() {
        return lastValidBlockHeight;
    }
}
