package com.qianlihu.solanawallet.rpc.bean;

import android.view.View;

import com.squareup.moshi.Json;

/**
 * author : Duan
 * date   : 2021/12/2516:12
 * desc   :
 * version: 1.0.0
 */
public class TokenAccountBalanceBean extends RpcResultObject {

    @Json(name = "value")
    private Value value;

    public static class Value{
        @Json(name = "amount")
        private String amount;
        @Json(name = "decimals")
        private Integer decimals;
        @Json(name = "uiAmount")
        private double uiAmount;
        @Json(name = "uiAmountString")
        private String uiAmountString;

        public String getAmount() {
            return amount;
        }

        public Integer getDecimals() {
            return decimals;
        }

        public double getUiAmount() {
            return uiAmount;
        }

        public String getUiAmountString() {
            return uiAmountString;
        }

        @Override
        public String toString() {
            return "Value{" +
                    "amount='" + amount + '\'' +
                    ", decimals=" + decimals +
                    ", uiAmount=" + uiAmount +
                    ", uiAmountString='" + uiAmountString + '\'' +
                    '}';
        }
    }

    public Value getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TokenAccountBalanceBean{" +
                "value=" + value +
                '}';
    }
}
