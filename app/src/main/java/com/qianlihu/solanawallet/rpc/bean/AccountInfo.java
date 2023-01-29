package com.qianlihu.solanawallet.rpc.bean;

import com.squareup.moshi.Json;

import java.util.List;

public class AccountInfo extends RpcResultObject {


    /**
     * value : {"data":"DK9N3EFBHyxDqdvHrdmPi1MVvNJfeEJ4STu4AF6B9HyutLCwfScmbfkbUUGB3htXSkmzL5UDenujNfJ4SXXU1odm6s7jSYjaMeYCUa2NsCicmzK","executable":false,"lamports":1461600,"owner":"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA","rentEpoch":264}
     */

    @Json(name = "value")
    private ValueBean value;

    public ValueBean getValue() {
        return value;
    }

    public static class ValueBean {
        /**
         * data : DK9N3EFBHyxDqdvHrdmPi1MVvNJfeEJ4STu4AF6B9HyutLCwfScmbfkbUUGB3htXSkmzL5UDenujNfJ4SXXU1odm6s7jSYjaMeYCUa2NsCicmzK
         * executable : false
         * lamports : 1461600
         * owner : TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA
         * rentEpoch : 264
         */
        @Json(name = "data")
        private List<String> data;
        @Json(name = "executable")
        private boolean executable;
        @Json(name = "lamports")
        private long lamports;
        @Json(name = "owner")
        private String owner;
        @Json(name = "rentEpoch")
        private int rentEpoch;

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }

        public boolean isExecutable() {
            return executable;
        }

        public void setExecutable(boolean executable) {
            this.executable = executable;
        }

        public long getLamports() {
            return lamports;
        }

        public void setLamports(int lamports) {
            this.lamports = lamports;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }

        public int getRentEpoch() {
            return rentEpoch;
        }

        public void setRentEpoch(int rentEpoch) {
            this.rentEpoch = rentEpoch;
        }
    }
}
