package com.qianlihu.solanawallet.rpc.bean;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/12/1718:18
 * desc   :
 * version: 1.0.0
 */
public class TransactionBean extends RpcResultObject {

    public static class Meta {
        @Json(name = "err")
        private String err;
        @Json(name = "fee")
        private long fee;
        @Json(name = "innerInstructions")
        private List<InnerInstructions> innerInstructions;
        @Json(name = "postBalances")
        private List<Long> postBalances;
        @Json(name = "postTokenBalances")
        private List<PostTokenBalances> postTokenBalances;
        @Json(name = "preBalances")
        private List<Long> preBalances;
        @Json(name = "preTokenBalances")
        private List<PreTokenBalances> preTokenBalances;

        public String getErr() {
            return err;
        }

        public long getFee() {
            return fee;
        }

        public List<InnerInstructions> getInnerInstructions() {
            return innerInstructions;
        }

        public List<Long> getPostBalances() {
            return postBalances;
        }

        public List<Long> getPreBalances() {
            return preBalances;
        }

        public List<PreTokenBalances> getPreTokenBalances() {
            return preTokenBalances;
        }
        public List<PostTokenBalances> getPostTokenBalances() {
            return postTokenBalances;
        }

        public static class PreTokenBalances{

            @Json(name = "accountIndex")
            private int accountIndex;
            @Json(name = "mint")
            private String mint;
            @Json(name = "owner")
            private String owner;
            @Json(name = "uiTokenAmount")
            private UiTokenAmountBean uiTokenAmount;

            public int getAccountIndex() {
                return accountIndex;
            }

            public String getMint() {
                return mint;
            }

            public String getOwner() {
                return owner;
            }

            public UiTokenAmountBean getUiTokenAmount() {
                return uiTokenAmount;
            }

            @Override
            public String toString() {
                return "PreTokenBalances{" +
                        "accountIndex=" + accountIndex +
                        ", mint='" + mint + '\'' +
                        ", owner='" + owner + '\'' +
                        ", uiTokenAmount=" + uiTokenAmount +
                        '}';
            }

            public static class UiTokenAmountBean {
                /**
                 * amount : 1000000000
                 * decimals : 9
                 * uiAmount : 1
                 * uiAmountString : 1
                 */

                @Json(name = "amount")
                private String amount;
                @Json(name = "decimals")
                private int decimals;
//                @Json(name = "uiAmount")
//                private double uiAmount;
                @Json(name = "uiAmountString")
                private String uiAmountString;

                public String getAmount() {
                    return amount;
                }

                public int getDecimals() {
                    return decimals;
                }

//                public double getUiAmount() {
//                    return uiAmount;
//                }

                public String getUiAmountString() {
                    return uiAmountString;
                }

                @Override
                public String toString() {
                    return "UiTokenAmountBean{" +
                            "amount='" + amount + '\'' +
                            ", decimals=" + decimals +
//                            ", uiAmount=" + uiAmount +
                            ", uiAmountString='" + uiAmountString + '\'' +
                            '}';
                }
            }
        }

        public static class PostTokenBalances{

            @Json(name = "accountIndex")
            private int accountIndex;
            @Json(name = "mint")
            private String mint;
            @Json(name = "owner")
            private String owner;
            @Json(name = "uiTokenAmount")
            private UiTokenAmountBean uiTokenAmount;

            public int getAccountIndex() {
                return accountIndex;
            }

            public String getMint() {
                return mint;
            }

            public String getOwner() {
                return owner;
            }

            public UiTokenAmountBean getUiTokenAmount() {
                return uiTokenAmount;
            }

            @Override
            public String toString() {
                return "PostTokenBalances{" +
                        "accountIndex=" + accountIndex +
                        ", mint='" + mint + '\'' +
                        ", owner='" + owner + '\'' +
                        ", uiTokenAmount=" + uiTokenAmount +
                        '}';
            }

            public static class UiTokenAmountBean {
                /**
                 * amount : 1000000000
                 * decimals : 9
                 * uiAmount : 1
                 * uiAmountString : 1
                 */

                @Json(name = "amount")
                private String amount;
                @Json(name = "decimals")
                private int decimals;
//                @Json(name = "uiAmount")
//                private double uiAmount;
                @Json(name = "uiAmountString")
                private String uiAmountString;

                public String getAmount() {
                    return amount;
                }

                public int getDecimals() {
                    return decimals;
                }

//                public double getUiAmount() {
//                    return uiAmount;
//                }

                public String getUiAmountString() {
                    return uiAmountString;
                }

                @Override
                public String toString() {
                    return "UiTokenAmountBean{" +
                            "amount='" + amount + '\'' +
                            ", decimals=" + decimals +
//                            ", uiAmount=" + uiAmount +
                            ", uiAmountString='" + uiAmountString + '\'' +
                            '}';
                }
            }
        }

        public static class InnerInstructions{

            @Json(name = "index")
            private int index;
            @Json(name = "instructions")
            private List<InstructionsBean> instructions;

            public int getIndex() {
                return index;
            }

            public List<InstructionsBean> getInstructions() {
                return instructions;
            }

            public static class InstructionsBean {

                @Json(name = "data")
                private String data;
                @Json(name = "programIdIndex")
                private int programIdIndex;
                @Json(name = "accounts")
                private List<Integer> accounts;

                public String getData() {
                    return data;
                }

                public int getProgramIdIndex() {
                    return programIdIndex;
                }

                public List<Integer> getAccounts() {
                    return accounts;
                }

            }
        }

        @Override
        public String toString() {
            return "Meta{" +
                    "err='" + err + '\'' +
                    ", fee=" + fee +
                    ", innerInstructions=" + innerInstructions +
                    ", postBalances=" + postBalances +
                    ", preBalances=" + preBalances +
                    ", preTokenBalances=" + preTokenBalances +
                    '}';
        }
    }

    public static class Transaction {
        public static class Message {

            @Json(name = "accountKeys")
            private List<String> accountKeys;
            @Json(name = "header")
            private Header header;
            @Json(name = "instructions")
            private List<Instructions> instructions;
            @Json(name = "recentBlockhash")
            private String recentBlockhash;

            public List<String> getAccountKeys() {
                return accountKeys;
            }

            public Header getHeader() {
                return header;
            }

            public List<Instructions> getInstructions() {
                return instructions;
            }

            public String getRecentBlockhash() {
                return recentBlockhash;
            }

            public static class Header {
                @Json(name = "numReadonlySignedAccounts")
                private Integer numReadonlySignedAccounts;
                @Json(name = "numReadonlyUnsignedAccounts")
                private Integer numReadonlyUnsignedAccounts;
                @Json(name = "numRequiredSignatures")
                private Integer numRequiredSignatures;

                public Integer getNumReadonlySignedAccounts() {
                    return numReadonlySignedAccounts;
                }

                public Integer getNumReadonlyUnsignedAccounts() {
                    return numReadonlyUnsignedAccounts;
                }

                public Integer getNumRequiredSignatures() {
                    return numRequiredSignatures;
                }

                @Override
                public String toString() {
                    return "Header{" +
                            "numReadonlySignedAccounts=" + numReadonlySignedAccounts +
                            ", numReadonlyUnsignedAccounts=" + numReadonlyUnsignedAccounts +
                            ", numRequiredSignatures=" + numRequiredSignatures +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "Message{" +
                        "accountKeys=" + accountKeys +
                        ", header=" + header +
                        ", instructions=" + instructions +
                        ", recentBlockhash='" + recentBlockhash + '\'' +
                        '}';
            }

            public static class Instructions {
                @Json(name = "accounts")
                private List<String> accounts;
                @Json(name = "data")
                private String data;
                @Json(name = "programIdIndex")
                private Integer programIdIndex;

                public List<String> getAccounts() {
                    return accounts;
                }

                public String getData() {
                    return data;
                }

                public Integer getProgramIdIndex() {
                    return programIdIndex;
                }

                @Override
                public String toString() {
                    return "Instructions{" +
                            "accounts=" + accounts +
                            ", data='" + data + '\'' +
                            ", programIdIndex=" + programIdIndex +
                            '}';
                }
            }
        }

        @Json(name = "message")
        private Message message;
        @Json(name = "signatures")
        private List<String> signatures;

        public Message getMessage() {
            return message;
        }

        public List<String> getSignatures() {
            return signatures;
        }

        @Override
        public String toString() {
            return "Transaction{" +
                    "message=" + message +
                    ", signatures=" + signatures +
                    '}';
        }
    }

    @Json(name = "meta")
    private Meta meta;
    @Json(name = "slot")
    private long slot;
    @Json(name = "transaction")
    private Transaction transaction;
    @Json(name = "blockTime")
    private long blockTime;

    public Meta getMeta() {
        return meta;
    }

    public long getSlot() {
        return slot;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public long getBlockTime() {
        return blockTime;
    }

    @Override
    public String toString() {
        return "TransactionBean{" +
                "meta=" + meta +
                ", slot=" + slot +
                ", transaction=" + transaction +
                ", blockTime=" + blockTime +
                '}';
    }
}
