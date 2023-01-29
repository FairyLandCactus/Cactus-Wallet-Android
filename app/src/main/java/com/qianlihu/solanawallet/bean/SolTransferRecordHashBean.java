package com.qianlihu.solanawallet.bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/4/2513:38
 * desc   : solana代币转账记录hash
 * version: 1.0.0
 */
public class SolTransferRecordHashBean {


    /**
     * succcess : true
     * data : {"tx":{"transactions":[{"blockTime":1650852032,"slot":131213555,"txHash":"5xWdtodjCLkFEsPpb3K8CGykfp4H639VZeZxqWgXfo68TL8Q81kYAP55RiFeAv7MMXSuxgM3rkPezU2yK7F6fYF3","fee":5000,"status":"Success","lamport":0,"signer":["HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq"],"includeSPLTransfer":true,"parsedInstruction":[{"programId":"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA","program":"spl-token","type":"spl-transfer"}],"change":{"_id":"626600dfa2c9b32ebcdbf560","address":"CYFkc3wUgh8qASW2UaC7ZRGUcY7bGJn2FseDsPJrxgcx","signature":["5xWdtodjCLkFEsPpb3K8CGykfp4H639VZeZxqWgXfo68TL8Q81kYAP55RiFeAv7MMXSuxgM3rkPezU2yK7F6fYF3"],"changeType":"dec","changeAmount":-10000000000,"decimals":9,"postBalance":"137096100000","preBalance":"147096100000","tokenAddress":"5QjajzuSfugQqqL8NSdkXdC9jDKHwf83yFHfWimhKXBa","symbol":"IUS","blockTime":1650852032,"slot":131213555,"fee":5000,"owner":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","balance":{"amount":"137096100000","decimals":9},"tokenName":"IUS","tokenIcon":"https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/5QjajzuSfugQqqL8NSdkXdC9jDKHwf83yFHfWimhKXBa/logo.png"}}]}}
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
         * tx : {"transactions":[{"blockTime":1650852032,"slot":131213555,"txHash":"5xWdtodjCLkFEsPpb3K8CGykfp4H639VZeZxqWgXfo68TL8Q81kYAP55RiFeAv7MMXSuxgM3rkPezU2yK7F6fYF3","fee":5000,"status":"Success","lamport":0,"signer":["HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq"],"includeSPLTransfer":true,"parsedInstruction":[{"programId":"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA","program":"spl-token","type":"spl-transfer"}],"change":{"_id":"626600dfa2c9b32ebcdbf560","address":"CYFkc3wUgh8qASW2UaC7ZRGUcY7bGJn2FseDsPJrxgcx","signature":["5xWdtodjCLkFEsPpb3K8CGykfp4H639VZeZxqWgXfo68TL8Q81kYAP55RiFeAv7MMXSuxgM3rkPezU2yK7F6fYF3"],"changeType":"dec","changeAmount":-10000000000,"decimals":9,"postBalance":"137096100000","preBalance":"147096100000","tokenAddress":"5QjajzuSfugQqqL8NSdkXdC9jDKHwf83yFHfWimhKXBa","symbol":"IUS","blockTime":1650852032,"slot":131213555,"fee":5000,"owner":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","balance":{"amount":"137096100000","decimals":9},"tokenName":"IUS","tokenIcon":"https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/5QjajzuSfugQqqL8NSdkXdC9jDKHwf83yFHfWimhKXBa/logo.png"}}]}
         */

        private TxBean tx;

        public TxBean getTx() {
            return tx;
        }

        public void setTx(TxBean tx) {
            this.tx = tx;
        }

        public static class TxBean {
            private List<TransactionsBean> transactions;

            public List<TransactionsBean> getTransactions() {
                return transactions;
            }

            public void setTransactions(List<TransactionsBean> transactions) {
                this.transactions = transactions;
            }

            public static class TransactionsBean {
                /**
                 * blockTime : 1650852032
                 * slot : 131213555
                 * txHash : 5xWdtodjCLkFEsPpb3K8CGykfp4H639VZeZxqWgXfo68TL8Q81kYAP55RiFeAv7MMXSuxgM3rkPezU2yK7F6fYF3
                 * fee : 5000
                 * status : Success
                 * lamport : 0
                 * signer : ["HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq"]
                 * includeSPLTransfer : true
                 * parsedInstruction : [{"programId":"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA","program":"spl-token","type":"spl-transfer"}]
                 * change : {"_id":"626600dfa2c9b32ebcdbf560","address":"CYFkc3wUgh8qASW2UaC7ZRGUcY7bGJn2FseDsPJrxgcx","signature":["5xWdtodjCLkFEsPpb3K8CGykfp4H639VZeZxqWgXfo68TL8Q81kYAP55RiFeAv7MMXSuxgM3rkPezU2yK7F6fYF3"],"changeType":"dec","changeAmount":-10000000000,"decimals":9,"postBalance":"137096100000","preBalance":"147096100000","tokenAddress":"5QjajzuSfugQqqL8NSdkXdC9jDKHwf83yFHfWimhKXBa","symbol":"IUS","blockTime":1650852032,"slot":131213555,"fee":5000,"owner":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","balance":{"amount":"137096100000","decimals":9},"tokenName":"IUS","tokenIcon":"https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/5QjajzuSfugQqqL8NSdkXdC9jDKHwf83yFHfWimhKXBa/logo.png"}
                 */

                private int blockTime;
                private int slot;
                private String txHash;

                public int getBlockTime() {
                    return blockTime;
                }

                public void setBlockTime(int blockTime) {
                    this.blockTime = blockTime;
                }

                public int getSlot() {
                    return slot;
                }

                public void setSlot(int slot) {
                    this.slot = slot;
                }

                public String getTxHash() {
                    return txHash;
                }

                public void setTxHash(String txHash) {
                    this.txHash = txHash;
                }
            }
        }
    }
}
