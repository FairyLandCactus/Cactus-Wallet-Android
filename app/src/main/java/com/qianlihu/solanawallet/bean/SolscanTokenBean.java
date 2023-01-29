package com.qianlihu.solanawallet.bean;

/**
 * author : Duan
 * date   : 2023/1/618:13
 * desc   :
 * version: 1.0.0
 */
public class SolscanTokenBean {
    private boolean succcess;
    private DataBeanX data;

    public boolean isSucccess() {
        return succcess;
    }

    public void setSucccess(boolean succcess) {
        this.succcess = succcess;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {

        private TokenInfoBean tokenInfo;

        public TokenInfoBean getTokenInfo() {
            return tokenInfo;
        }

        public void setTokenInfo(TokenInfoBean tokenInfo) {
            this.tokenInfo = tokenInfo;
        }

        public static class TokenInfoBean {
            private String name;
            private String symbol;
            private int decimals;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSymbol() {
                return symbol;
            }

            public void setSymbol(String symbol) {
                this.symbol = symbol;
            }

            public int getDecimals() {
                return decimals;
            }

            public void setDecimals(int decimals) {
                this.decimals = decimals;
            }
        }
    }
}
