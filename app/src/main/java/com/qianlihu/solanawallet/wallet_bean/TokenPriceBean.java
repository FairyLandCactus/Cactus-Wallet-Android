package com.qianlihu.solanawallet.wallet_bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/11/514:48
 * desc   :
 * version: 1.0.0
 */
public class TokenPriceBean {

    private String chain;
    private List<DataBean> data;

    public String getChain() {
        return chain;
    }

    public void setChain(String chain) {
        this.chain = chain;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * tokenAddress : 210x32874441dB5c8890841b7f10851fa0bc8354C816
         * slug : SAMO
         */

        private String tokenAddress;
        private String slug;

        public String getTokenAddress() {
            return tokenAddress;
        }

        public void setTokenAddress(String tokenAddress) {
            this.tokenAddress = tokenAddress;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }
    }
}
