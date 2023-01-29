package com.qianlihu.solanawallet.rpc.bean;

import com.squareup.moshi.Json;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/12/2313:48
 * desc   :
 * version: 1.0.0
 */
public class TokenAccountsByDelegateBean extends RpcResultObject {

    @Json(name = "value")
    private List<Value> value;

    public List<Value> getValue() {
        return value;
    }

    public static class Value {
        @Json(name = "account")
        private Account account;
        @Json(name = "pubkey")
        private String pubkey;

        public Account getAccount() {
            return account;
        }

        public String getPubkey() {
            return pubkey;
        }

        public static class Account {
            @Json(name = "data")
            private Data data;
            @Json(name = "executable")
            private boolean executable;
            @Json(name = "lamports")
            private long lamports;
            @Json(name = "owner")
            private String owner;
            @Json(name = "rentEpoch")
            private int rentEpoch;

            public Data getData() {
                return data;
            }

            public boolean isExecutable() {
                return executable;
            }

            public long getLamports() {
                return lamports;
            }

            public String getOwner() {
                return owner;
            }

            public int getRentEpoch() {
                return rentEpoch;
            }

            @Override
            public String toString() {
                return "Account{" +
                        "data=" + data +
                        ", executable=" + executable +
                        ", lamports=" + lamports +
                        ", owner='" + owner + '\'' +
                        ", rentEpoch=" + rentEpoch +
                        '}';
            }

            public static class Data {
                @Json(name = "program")
                private String program;
                @Json(name = "parsed")
                private Parsed parsed;
                @Json(name = "space")
                private int space;

                public String getProgram() {
                    return program;
                }

                public Parsed getParsed() {
                    return parsed;
                }

                public int getSpace() {
                    return space;
                }

                @Override
                public String toString() {
                    return "Data{" +
                            "program='" + program + '\'' +
                            ", parsed=" + parsed +
                            ", space=" + space +
                            '}';
                }

                public static class Parsed {
                    @Json(name = "info")
                    private Info info;
                    @Json(name = "type")
                    private String type;

                    public Info getInfo() {
                        return info;
                    }

                    public String getType() {
                        return type;
                    }

                    @Override
                    public String toString() {
                        return "Parsed{" +
                                "info=" + info +
                                ", type='" + type + '\'' +
                                '}';
                    }

                    public static class Info {
                        @Json(name = "tokenAmount")
                        private TokenAmount tokenAmount;
                        @Json(name = "delegate")
                        private String delegate;
                        @Json(name = "delegatedAmount")
                        private DelegatedAmount delegatedAmount;
                        @Json(name = "state")
                        private String state;
                        @Json(name = "isNative")
                        private boolean isNative;
                        @Json(name = "mint")
                        private String mint;
                        @Json(name = "owner")
                        private String owner;

                        public TokenAmount getTokenAmount() {
                            return tokenAmount;
                        }

                        public String getDelegate() {
                            return delegate;
                        }

                        public DelegatedAmount getDelegatedAmount() {
                            return delegatedAmount;
                        }

                        public String getState() {
                            return state;
                        }

                        public boolean isNative() {
                            return isNative;
                        }

                        public String getMint() {
                            return mint;
                        }

                        public String getOwner() {
                            return owner;
                        }

                        @Override
                        public String toString() {
                            return "Info{" +
                                    "tokenAmount=" + tokenAmount +
                                    ", delegate='" + delegate + '\'' +
                                    ", delegatedAmount=" + delegatedAmount +
                                    ", state='" + state + '\'' +
                                    ", isNative=" + isNative +
                                    ", mint='" + mint + '\'' +
                                    ", owner='" + owner + '\'' +
                                    '}';
                        }

                        public static class TokenAmount {
                            @Json(name = "amount")
                            private String amount;
                            @Json(name = "decimals")
                            private int decimals;
                            @Json(name = "uiAmount")
                            private Double uiAmount;
                            @Json(name = "uiAmountString")
                            private String uiAmountString;

                            public String getAmount() {
                                return amount;
                            }

                            public int getDecimals() {
                                return decimals;
                            }

                            public Double getUiAmount() {
                                return uiAmount;
                            }

                            public String getUiAmountString() {
                                return uiAmountString;
                            }

                            @Override
                            public String toString() {
                                return "TokenAmount{" +
                                        "amount='" + amount + '\'' +
                                        ", decimals=" + decimals +
                                        ", uiAmount=" + uiAmount +
                                        ", uiAmountString='" + uiAmountString + '\'' +
                                        '}';
                            }
                        }

                        public static class DelegatedAmount{
                            @Json(name = "amount")
                            private String amount;
                            @Json(name = "decimals")
                            private int decimals;
                            @Json(name = "uiAmount")
                            private Double uiAmount;
                            @Json(name = "uiAmountString")
                            private String uiAmountString;

                            public String getAmount() {
                                return amount;
                            }

                            public int getDecimals() {
                                return decimals;
                            }

                            public Double getUiAmount() {
                                return uiAmount;
                            }

                            public String getUiAmountString() {
                                return uiAmountString;
                            }

                            @Override
                            public String toString() {
                                return "DelegatedAmount{" +
                                        "amount='" + amount + '\'' +
                                        ", decimals=" + decimals +
                                        ", uiAmount=" + uiAmount +
                                        ", uiAmountString='" + uiAmountString + '\'' +
                                        '}';
                            }
                        }
                    }

                }
            }
        }

        @Override
        public String toString() {
            return "Value{" +
                    "account=" + account +
                    ", pubkey='" + pubkey + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "TokenAccountsByOwnerBean{" +
                "value=" + value +
                '}';
    }
}
