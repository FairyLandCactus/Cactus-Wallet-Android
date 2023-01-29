package com.qianlihu.solanawallet.bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/6/811:32
 * desc   :
 * version: 1.0.0
 */
public class SolanaBalanceTokenBean {


    /**
     * succcess : true
     * data : [{"tokenAddress":"AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU","tokenAmount":{"amount":"10000000000","decimals":9,"uiAmount":10,"uiAmountString":"10"},"tokenAccount":"BxfWToH1en9M9bTSDt2cWyahVvYsGhecNfgidwx4zsav","tokenName":"IUX","tokenIcon":"https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU/logo.png","rentEpoch":316,"lamports":2039280,"tokenSymbol":"IUX"}]
     */

    private boolean succcess;
    private List<DataBean> data;

    public boolean isSucccess() {
        return succcess;
    }

    public void setSucccess(boolean succcess) {
        this.succcess = succcess;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * tokenAddress : AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU
         * tokenAmount : {"amount":"10000000000","decimals":9,"uiAmount":10,"uiAmountString":"10"}
         * tokenAccount : BxfWToH1en9M9bTSDt2cWyahVvYsGhecNfgidwx4zsav
         * tokenName : IUX
         * tokenIcon : https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU/logo.png
         * rentEpoch : 316
         * lamports : 2039280
         * tokenSymbol : IUX
         */

        private String tokenAddress;
        private TokenAmountBean tokenAmount;
        private String tokenAccount;
        private String tokenName;
        private String tokenIcon;
        private int rentEpoch;
        private String lamports;
        private String tokenSymbol;

        public String getTokenAddress() {
            return tokenAddress;
        }

        public void setTokenAddress(String tokenAddress) {
            this.tokenAddress = tokenAddress;
        }

        public TokenAmountBean getTokenAmount() {
            return tokenAmount;
        }

        public void setTokenAmount(TokenAmountBean tokenAmount) {
            this.tokenAmount = tokenAmount;
        }

        public String getTokenAccount() {
            return tokenAccount;
        }

        public void setTokenAccount(String tokenAccount) {
            this.tokenAccount = tokenAccount;
        }

        public String getTokenName() {
            return tokenName;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }

        public String getTokenIcon() {
            return tokenIcon;
        }

        public void setTokenIcon(String tokenIcon) {
            this.tokenIcon = tokenIcon;
        }

        public int getRentEpoch() {
            return rentEpoch;
        }

        public void setRentEpoch(int rentEpoch) {
            this.rentEpoch = rentEpoch;
        }

        public String getLamports() {
            return lamports;
        }

        public void setLamports(String lamports) {
            this.lamports = lamports;
        }

        public String getTokenSymbol() {
            return tokenSymbol;
        }

        public void setTokenSymbol(String tokenSymbol) {
            this.tokenSymbol = tokenSymbol;
        }

        public static class TokenAmountBean {
            /**
             * amount : 10000000000
             * decimals : 9
             * uiAmount : 10
             * uiAmountString : 10
             */

            private String amount;
            private int decimals;
            private double uiAmount;
            private String uiAmountString;

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public int getDecimals() {
                return decimals;
            }

            public void setDecimals(int decimals) {
                this.decimals = decimals;
            }

            public double getUiAmount() {
                return uiAmount;
            }

            public void setUiAmount(double uiAmount) {
                this.uiAmount = uiAmount;
            }

            public String getUiAmountString() {
                return uiAmountString;
            }

            public void setUiAmountString(String uiAmountString) {
                this.uiAmountString = uiAmountString;
            }
        }
    }
}
