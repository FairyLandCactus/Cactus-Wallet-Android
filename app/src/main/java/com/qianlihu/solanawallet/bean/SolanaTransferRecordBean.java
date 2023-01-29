package com.qianlihu.solanawallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author : Duan
 * date   : 2022/6/818:21
 * desc   :
 * version: 1.0.0
 */
public class SolanaTransferRecordBean implements Serializable {


    /**
     * succcess : true
     * data : {"tx":{"transactions":[{"_id":"62624371315d971ac70abd4e","src":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","dst":"2Ev1Fgck6JgiEzWWKUkaVLrwm7CjX7MJNdeSrCwqBsLz","lamport":2000000,"blockTime":1650606933,"slot":130807705,"txHash":"ESrfdbQw4DfUrU6B7psbtii9d7frcJy8dLfTzgLPQoKCwNU7kihwJwxKs2yzH6ophtfjJEwkPsTPXu8eBWQAfHM","fee":5000,"status":"Success","decimals":9,"txNumberSolTransfer":1}],"hasNext":true,"total":73},"begin":1625627714}
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

    public static class DataBean implements Serializable {
        /**
         * tx : {"transactions":[{"_id":"62624371315d971ac70abd4e","src":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","dst":"2Ev1Fgck6JgiEzWWKUkaVLrwm7CjX7MJNdeSrCwqBsLz","lamport":2000000,"blockTime":1650606933,"slot":130807705,"txHash":"ESrfdbQw4DfUrU6B7psbtii9d7frcJy8dLfTzgLPQoKCwNU7kihwJwxKs2yzH6ophtfjJEwkPsTPXu8eBWQAfHM","fee":5000,"status":"Success","decimals":9,"txNumberSolTransfer":1}],"hasNext":true,"total":73}
         * begin : 1625627714
         */

        private TxBean tx;
        private long begin;

        public TxBean getTx() {
            return tx;
        }

        public void setTx(TxBean tx) {
            this.tx = tx;
        }

        public long getBegin() {
            return begin;
        }

        public void setBegin(long begin) {
            this.begin = begin;
        }

        public static class TxBean implements Serializable{
            /**
             * transactions : [{"_id":"62624371315d971ac70abd4e","src":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","dst":"2Ev1Fgck6JgiEzWWKUkaVLrwm7CjX7MJNdeSrCwqBsLz","lamport":2000000,"blockTime":1650606933,"slot":130807705,"txHash":"ESrfdbQw4DfUrU6B7psbtii9d7frcJy8dLfTzgLPQoKCwNU7kihwJwxKs2yzH6ophtfjJEwkPsTPXu8eBWQAfHM","fee":5000,"status":"Success","decimals":9,"txNumberSolTransfer":1}]
             * hasNext : true
             * total : 73
             */

            private boolean hasNext;
            private int total;
            private List<TransactionsBean> transactions;

            public boolean isHasNext() {
                return hasNext;
            }

            public void setHasNext(boolean hasNext) {
                this.hasNext = hasNext;
            }

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<TransactionsBean> getTransactions() {
                return transactions;
            }

            public void setTransactions(List<TransactionsBean> transactions) {
                this.transactions = transactions;
            }

            public static class TransactionsBean implements Serializable{
                /**
                 * _id : 62624371315d971ac70abd4e
                 * src : HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq
                 * dst : 2Ev1Fgck6JgiEzWWKUkaVLrwm7CjX7MJNdeSrCwqBsLz
                 * lamport : 2000000
                 * blockTime : 1650606933
                 * slot : 130807705
                 * txHash : ESrfdbQw4DfUrU6B7psbtii9d7frcJy8dLfTzgLPQoKCwNU7kihwJwxKs2yzH6ophtfjJEwkPsTPXu8eBWQAfHM
                 * fee : 5000
                 * status : Success
                 * decimals : 9
                 * txNumberSolTransfer : 1
                 */

                private String _id;
                private String src;
                private String dst;
                private String lamport;
                private long blockTime;
                private String slot;
                private String txHash;
                private String fee;
                private String status;
                private int decimals;
                private int txNumberSolTransfer;

                public String get_id() {
                    return _id;
                }

                public void set_id(String _id) {
                    this._id = _id;
                }

                public String getSrc() {
                    return src;
                }

                public void setSrc(String src) {
                    this.src = src;
                }

                public String getDst() {
                    return dst;
                }

                public void setDst(String dst) {
                    this.dst = dst;
                }

                public String getLamport() {
                    return lamport;
                }

                public void setLamport(String lamport) {
                    this.lamport = lamport;
                }

                public long getBlockTime() {
                    return blockTime;
                }

                public void setBlockTime(long blockTime) {
                    this.blockTime = blockTime;
                }

                public String getSlot() {
                    return slot;
                }

                public void setSlot(String slot) {
                    this.slot = slot;
                }

                public String getTxHash() {
                    return txHash;
                }

                public void setTxHash(String txHash) {
                    this.txHash = txHash;
                }

                public String getFee() {
                    return fee;
                }

                public void setFee(String fee) {
                    this.fee = fee;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public int getDecimals() {
                    return decimals;
                }

                public void setDecimals(int decimals) {
                    this.decimals = decimals;
                }

                public int getTxNumberSolTransfer() {
                    return txNumberSolTransfer;
                }

                public void setTxNumberSolTransfer(int txNumberSolTransfer) {
                    this.txNumberSolTransfer = txNumberSolTransfer;
                }
            }
        }
    }
}
