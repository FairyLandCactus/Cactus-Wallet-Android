package com.qianlihu.solanawallet.bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/6/2817:32
 * desc   :
 * version: 1.0.0
 */
public class BnbTokenBEP20BalanceBean {


    /**
     * status : 1
     * message : OK
     * result : [{"TokenAddress":"0xfcb5DF42e06A39E233dc707bb3a80311eFD11576","TokenName":"www.METH.co.in","TokenSymbol":"METH","TokenQuantity":"1337000","TokenDivisor":"18"},{"TokenAddress":"0xdf1f0026374d4bcc490be5e316963cf6df2fff19","TokenName":"InnovativeBioresearchCoin","TokenSymbol":"INNBC","TokenQuantity":"2000000000","TokenDivisor":"6"},{"TokenAddress":"0x78e1936f065fd4082387622878c7d11c9f05ecf4","TokenName":"JNTR/b","TokenSymbol":"JNTR/b","TokenQuantity":"163000000000000000000","TokenDivisor":"18"},{"TokenAddress":"0x12e34cdf6a031a10fe241864c32fb03a4fdad739","TokenName":"FREE coin BSC","TokenSymbol":"FREE","TokenQuantity":"21144000000000000000000","TokenDivisor":"18"},{"TokenAddress":"0xef5ceb00ae3eb617773016b4ca886b61fa4e027e","TokenName":"BURNING","TokenSymbol":"BURN","TokenQuantity":"50000000000000000","TokenDivisor":"18"}]
     */

    private String status;
    private String message;
    private List<ResultBean> result;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * TokenAddress : 0xfcb5DF42e06A39E233dc707bb3a80311eFD11576
         * TokenName : www.METH.co.in
         * TokenSymbol : METH
         * TokenQuantity : 1337000
         * TokenDivisor : 18
         */

        private String TokenAddress;
        private String TokenName;
        private String TokenSymbol;
        private String TokenQuantity;
        private String TokenDivisor;

        public String getTokenAddress() {
            return TokenAddress;
        }

        public void setTokenAddress(String TokenAddress) {
            this.TokenAddress = TokenAddress;
        }

        public String getTokenName() {
            return TokenName;
        }

        public void setTokenName(String TokenName) {
            this.TokenName = TokenName;
        }

        public String getTokenSymbol() {
            return TokenSymbol;
        }

        public void setTokenSymbol(String TokenSymbol) {
            this.TokenSymbol = TokenSymbol;
        }

        public String getTokenQuantity() {
            return TokenQuantity;
        }

        public void setTokenQuantity(String TokenQuantity) {
            this.TokenQuantity = TokenQuantity;
        }

        public String getTokenDivisor() {
            return TokenDivisor;
        }

        public void setTokenDivisor(String TokenDivisor) {
            this.TokenDivisor = TokenDivisor;
        }
    }
}
