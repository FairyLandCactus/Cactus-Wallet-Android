package com.qianlihu.solanawallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author : Duan
 * date   : 2022/6/914:16
 * desc   :
 * version: 1.0.0
 */
public class SolTokenTransferRecordBean implements Serializable {
    /**
     * succcess : true
     * data : {"tx":{"transactions":[{"blockTime":1654687240,"slot":136789706,"txHash":"2JAJqw72Ha4DfHn4i2YFhoFUVxiaisCkXbzUUifdCnRX5N6xu3buT3NQ4fTdZjxVMWB86C9WfQwkFMYo4vYm6EjF","fee":5000,"status":"Success","lamport":0,"signer":["HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq"],"includeSPLTransfer":true,"parsedInstruction":[{"programId":"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA","program":"spl-token","type":"spl-transfer"}],"change":{"_id":"62a08669a4ce33599d898284","address":"5U6VG4sDwtNkwTnUDN7ME3gfdy2xLHDoJt5UaiGAkMbK","signature":["2JAJqw72Ha4DfHn4i2YFhoFUVxiaisCkXbzUUifdCnRX5N6xu3buT3NQ4fTdZjxVMWB86C9WfQwkFMYo4vYm6EjF"],"changeType":"dec","changeAmount":-1000000,"decimals":6,"postBalance":"306919600","preBalance":"307919600","tokenAddress":"Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB","symbol":"USDT","blockTime":1654687240,"slot":136789706,"fee":5000,"owner":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","balance":{"amount":"306919600","decimals":6},"tokenName":"USDT","tokenIcon":"https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB/logo.svg"}}],"total":158,"hasNext":true},"begin":1625627714}
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
         * tx : {"transactions":[{"blockTime":1654687240,"slot":136789706,"txHash":"2JAJqw72Ha4DfHn4i2YFhoFUVxiaisCkXbzUUifdCnRX5N6xu3buT3NQ4fTdZjxVMWB86C9WfQwkFMYo4vYm6EjF","fee":5000,"status":"Success","lamport":0,"signer":["HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq"],"includeSPLTransfer":true,"parsedInstruction":[{"programId":"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA","program":"spl-token","type":"spl-transfer"}],"change":{"_id":"62a08669a4ce33599d898284","address":"5U6VG4sDwtNkwTnUDN7ME3gfdy2xLHDoJt5UaiGAkMbK","signature":["2JAJqw72Ha4DfHn4i2YFhoFUVxiaisCkXbzUUifdCnRX5N6xu3buT3NQ4fTdZjxVMWB86C9WfQwkFMYo4vYm6EjF"],"changeType":"dec","changeAmount":-1000000,"decimals":6,"postBalance":"306919600","preBalance":"307919600","tokenAddress":"Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB","symbol":"USDT","blockTime":1654687240,"slot":136789706,"fee":5000,"owner":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","balance":{"amount":"306919600","decimals":6},"tokenName":"USDT","tokenIcon":"https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB/logo.svg"}}],"total":158,"hasNext":true}
         * begin : 1625627714
         */

        private TxBean tx;

        public TxBean getTx() {
            return tx;
        }

        public void setTx(TxBean tx) {
            this.tx = tx;
        }

        public static class TxBean implements Serializable{
            /**
             * transactions : [{"blockTime":1654687240,"slot":136789706,"txHash":"2JAJqw72Ha4DfHn4i2YFhoFUVxiaisCkXbzUUifdCnRX5N6xu3buT3NQ4fTdZjxVMWB86C9WfQwkFMYo4vYm6EjF","fee":5000,"status":"Success","lamport":0,"signer":["HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq"],"includeSPLTransfer":true,"parsedInstruction":[{"programId":"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA","program":"spl-token","type":"spl-transfer"}],"change":{"_id":"62a08669a4ce33599d898284","address":"5U6VG4sDwtNkwTnUDN7ME3gfdy2xLHDoJt5UaiGAkMbK","signature":["2JAJqw72Ha4DfHn4i2YFhoFUVxiaisCkXbzUUifdCnRX5N6xu3buT3NQ4fTdZjxVMWB86C9WfQwkFMYo4vYm6EjF"],"changeType":"dec","changeAmount":-1000000,"decimals":6,"postBalance":"306919600","preBalance":"307919600","tokenAddress":"Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB","symbol":"USDT","blockTime":1654687240,"slot":136789706,"fee":5000,"owner":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","balance":{"amount":"306919600","decimals":6},"tokenName":"USDT","tokenIcon":"https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB/logo.svg"}}]
             * total : 158
             * hasNext : true
             */

            private int total;
            private List<TransactionsBean> transactions;

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
                 * blockTime : 1654687240
                 * slot : 136789706
                 * txHash : 2JAJqw72Ha4DfHn4i2YFhoFUVxiaisCkXbzUUifdCnRX5N6xu3buT3NQ4fTdZjxVMWB86C9WfQwkFMYo4vYm6EjF
                 * fee : 5000
                 * status : Success
                 * lamport : 0
                 * signer : ["HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq"]
                 * includeSPLTransfer : true
                 * parsedInstruction : [{"programId":"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA","program":"spl-token","type":"spl-transfer"}]
                 * change : {"_id":"62a08669a4ce33599d898284","address":"5U6VG4sDwtNkwTnUDN7ME3gfdy2xLHDoJt5UaiGAkMbK","signature":["2JAJqw72Ha4DfHn4i2YFhoFUVxiaisCkXbzUUifdCnRX5N6xu3buT3NQ4fTdZjxVMWB86C9WfQwkFMYo4vYm6EjF"],"changeType":"dec","changeAmount":-1000000,"decimals":6,"postBalance":"306919600","preBalance":"307919600","tokenAddress":"Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB","symbol":"USDT","blockTime":1654687240,"slot":136789706,"fee":5000,"owner":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","balance":{"amount":"306919600","decimals":6},"tokenName":"USDT","tokenIcon":"https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB/logo.svg"}
                 */

                private int blockTime;
                private String txHash;
                private int fee;
                private String status;
                private int lamport;
                private boolean includeSPLTransfer;
                private ChangeBean change;
                private List<String> signer;
                private List<ParsedInstructionBean> parsedInstruction;

                public int getBlockTime() {
                    return blockTime;
                }

                public void setBlockTime(int blockTime) {
                    this.blockTime = blockTime;
                }

                public String getTxHash() {
                    return txHash;
                }

                public void setTxHash(String txHash) {
                    this.txHash = txHash;
                }

                public int getFee() {
                    return fee;
                }

                public void setFee(int fee) {
                    this.fee = fee;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public int getLamport() {
                    return lamport;
                }

                public void setLamport(int lamport) {
                    this.lamport = lamport;
                }

                public boolean isIncludeSPLTransfer() {
                    return includeSPLTransfer;
                }

                public void setIncludeSPLTransfer(boolean includeSPLTransfer) {
                    this.includeSPLTransfer = includeSPLTransfer;
                }

                public ChangeBean getChange() {
                    return change;
                }

                public void setChange(ChangeBean change) {
                    this.change = change;
                }

                public List<String> getSigner() {
                    return signer;
                }

                public void setSigner(List<String> signer) {
                    this.signer = signer;
                }

                public List<ParsedInstructionBean> getParsedInstruction() {
                    return parsedInstruction;
                }

                public void setParsedInstruction(List<ParsedInstructionBean> parsedInstruction) {
                    this.parsedInstruction = parsedInstruction;
                }

                public static class ChangeBean implements Serializable{
                    /**
                     * _id : 62a08669a4ce33599d898284
                     * address : 5U6VG4sDwtNkwTnUDN7ME3gfdy2xLHDoJt5UaiGAkMbK
                     * signature : ["2JAJqw72Ha4DfHn4i2YFhoFUVxiaisCkXbzUUifdCnRX5N6xu3buT3NQ4fTdZjxVMWB86C9WfQwkFMYo4vYm6EjF"]
                     * changeType : dec
                     * changeAmount : -1000000
                     * decimals : 6
                     * postBalance : 306919600
                     * preBalance : 307919600
                     * tokenAddress : Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB
                     * symbol : USDT
                     * blockTime : 1654687240
                     * slot : 136789706
                     * fee : 5000
                     * owner : HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq
                     * balance : {"amount":"306919600","decimals":6}
                     * tokenName : USDT
                     * tokenIcon : https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB/logo.svg
                     */

                    private String _id;
                    private String address;
                    private String changeType;
                    private String changeAmount;
                    private int decimals;
                    private String postBalance;
                    private String preBalance;
                    private String tokenAddress;
                    private String symbol;
                    private long blockTime;
                    private int fee;
                    private String owner;
                    private BalanceBean balance;
                    private String tokenName;
                    private String tokenIcon;
                    private List<String> signature;

                    public String get_id() {
                        return _id;
                    }

                    public void set_id(String _id) {
                        this._id = _id;
                    }

                    public String getAddress() {
                        return address;
                    }

                    public void setAddress(String address) {
                        this.address = address;
                    }

                    public String getChangeType() {
                        return changeType;
                    }

                    public void setChangeType(String changeType) {
                        this.changeType = changeType;
                    }

                    public String getChangeAmount() {
                        return changeAmount;
                    }

                    public void setChangeAmount(String changeAmount) {
                        this.changeAmount = changeAmount;
                    }

                    public int getDecimals() {
                        return decimals;
                    }

                    public void setDecimals(int decimals) {
                        this.decimals = decimals;
                    }

                    public String getPostBalance() {
                        return postBalance;
                    }

                    public void setPostBalance(String postBalance) {
                        this.postBalance = postBalance;
                    }

                    public String getPreBalance() {
                        return preBalance;
                    }

                    public void setPreBalance(String preBalance) {
                        this.preBalance = preBalance;
                    }

                    public String getTokenAddress() {
                        return tokenAddress;
                    }

                    public void setTokenAddress(String tokenAddress) {
                        this.tokenAddress = tokenAddress;
                    }

                    public String getSymbol() {
                        return symbol;
                    }

                    public void setSymbol(String symbol) {
                        this.symbol = symbol;
                    }

                    public long getBlockTime() {
                        return blockTime;
                    }

                    public void setBlockTime(long blockTime) {
                        this.blockTime = blockTime;
                    }

                    public int getFee() {
                        return fee;
                    }

                    public void setFee(int fee) {
                        this.fee = fee;
                    }

                    public String getOwner() {
                        return owner;
                    }

                    public void setOwner(String owner) {
                        this.owner = owner;
                    }

                    public BalanceBean getBalance() {
                        return balance;
                    }

                    public void setBalance(BalanceBean balance) {
                        this.balance = balance;
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

                    public List<String> getSignature() {
                        return signature;
                    }

                    public void setSignature(List<String> signature) {
                        this.signature = signature;
                    }

                    public static class BalanceBean implements Serializable{
                        /**
                         * amount : 306919600
                         * decimals : 6
                         */

                        private String amount;
                        private int decimals;

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
                    }
                }

                public static class ParsedInstructionBean implements Serializable{
                    /**
                     * programId : TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA
                     * program : spl-token
                     * type : spl-transfer
                     */

                    private String programId;
                    private String program;
                    private String type;

                    public String getProgramId() {
                        return programId;
                    }

                    public void setProgramId(String programId) {
                        this.programId = programId;
                    }

                    public String getProgram() {
                        return program;
                    }

                    public void setProgram(String program) {
                        this.program = program;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }
                }
            }
        }
    }
}
