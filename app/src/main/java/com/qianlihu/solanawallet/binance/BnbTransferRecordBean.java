package com.qianlihu.solanawallet.binance;

import java.io.Serializable;
import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/2417:20
 * desc   :
 * version: 1.0.0
 */
public class BnbTransferRecordBean implements Serializable {


    /**
     * status : 1
     * message : OK
     * result : [{"blockNumber":"16333315","timeStamp":"1648112039","hash":"0x1201d161fa6192e7a08a93c98f71bbf7656725c2262182bbe216e1158c6ee826","nonce":"66","blockHash":"0x325c9f15decb12f38625024145c28041f7c0f1bee937dd60df6380b056b64f94","from":"0x0949ab678094f72570839f776f2e800c1a6b70f8","contractAddress":"0x55d398326f99059ff775485246999027b3197955","to":"0x85ae45a24b5c173594efd6f56f94c2309cfee7c2","value":"1000000000000000","tokenName":"Binance-Peg BSC-USD","tokenSymbol":"BSC-USD","tokenDecimal":"18","transactionIndex":"223","gas":"300000","gasPrice":"5000000000","gasUsed":"51103","cumulativeGasUsed":"26405069","input":"deprecated","confirmations":"601"}]
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

    public static class ResultBean implements Serializable{
        /**
         * blockNumber : 16333315
         * timeStamp : 1648112039
         * hash : 0x1201d161fa6192e7a08a93c98f71bbf7656725c2262182bbe216e1158c6ee826
         * nonce : 66
         * blockHash : 0x325c9f15decb12f38625024145c28041f7c0f1bee937dd60df6380b056b64f94
         * from : 0x0949ab678094f72570839f776f2e800c1a6b70f8
         * contractAddress : 0x55d398326f99059ff775485246999027b3197955
         * to : 0x85ae45a24b5c173594efd6f56f94c2309cfee7c2
         * value : 1000000000000000
         * tokenName : Binance-Peg BSC-USD
         * tokenSymbol : BSC-USD
         * tokenDecimal : 18
         * transactionIndex : 223
         * gas : 300000
         * gasPrice : 5000000000
         * gasUsed : 51103
         * cumulativeGasUsed : 26405069
         * input : deprecated
         * confirmations : 601
         */

        private String blockNumber;
        private String timeStamp;
        private String hash;
        private String nonce;
        private String blockHash;
        private String from;
        private String contractAddress;
        private String to;
        private String value;
        private String tokenName;
        private String tokenSymbol;
        private String tokenDecimal;
        private String transactionIndex;
        private String gas;
        private String gasPrice;
        private String gasUsed;
        private String cumulativeGasUsed;
        private String input;
        private String confirmations;

        public String getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getNonce() {
            return nonce;
        }

        public void setNonce(String nonce) {
            this.nonce = nonce;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getContractAddress() {
            return contractAddress;
        }

        public void setContractAddress(String contractAddress) {
            this.contractAddress = contractAddress;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getTokenName() {
            return tokenName;
        }

        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }

        public String getTokenSymbol() {
            return tokenSymbol;
        }

        public void setTokenSymbol(String tokenSymbol) {
            this.tokenSymbol = tokenSymbol;
        }

        public String getTokenDecimal() {
            return tokenDecimal;
        }

        public void setTokenDecimal(String tokenDecimal) {
            this.tokenDecimal = tokenDecimal;
        }

        public String getTransactionIndex() {
            return transactionIndex;
        }

        public void setTransactionIndex(String transactionIndex) {
            this.transactionIndex = transactionIndex;
        }

        public String getGas() {
            return gas;
        }

        public void setGas(String gas) {
            this.gas = gas;
        }

        public String getGasPrice() {
            return gasPrice;
        }

        public void setGasPrice(String gasPrice) {
            this.gasPrice = gasPrice;
        }

        public String getGasUsed() {
            return gasUsed;
        }

        public void setGasUsed(String gasUsed) {
            this.gasUsed = gasUsed;
        }

        public String getCumulativeGasUsed() {
            return cumulativeGasUsed;
        }

        public void setCumulativeGasUsed(String cumulativeGasUsed) {
            this.cumulativeGasUsed = cumulativeGasUsed;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }

        public String getConfirmations() {
            return confirmations;
        }

        public void setConfirmations(String confirmations) {
            this.confirmations = confirmations;
        }
    }
}
