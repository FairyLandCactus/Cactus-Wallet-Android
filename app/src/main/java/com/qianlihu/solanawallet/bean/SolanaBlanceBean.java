package com.qianlihu.solanawallet.bean;

/**
 * author : Duan
 * date   : 2022/6/810:11
 * desc   :
 * version: 1.0.0
 */
public class SolanaBlanceBean {


    /**
     * succcess : true
     * data : {"lamports":28897880,"ownerProgram":"11111111111111111111111111111111","type":"system_account","rentEpoch":316,"executable":false,"account":"2Ev1Fgck6JgiEzWWKUkaVLrwm7CjX7MJNdeSrCwqBsLz"}
     */

    private boolean succcess;
    private DataBean data;

    public boolean isSucccess() {
        return succcess;
    }

    public void setSucccess(boolean succcess) {
        this.succcess = succcess;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * lamports : 28897880
         * ownerProgram : 11111111111111111111111111111111
         * type : system_account
         * rentEpoch : 316
         * executable : false
         * account : 2Ev1Fgck6JgiEzWWKUkaVLrwm7CjX7MJNdeSrCwqBsLz
         */

        private String lamports;
        private String ownerProgram;
        private String type;
        private String account;

        public String getLamports() {
            return lamports;
        }

        public void setLamports(String lamports) {
            this.lamports = lamports;
        }

        public String getOwnerProgram() {
            return ownerProgram;
        }

        public void setOwnerProgram(String ownerProgram) {
            this.ownerProgram = ownerProgram;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}
