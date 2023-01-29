package com.qianlihu.solanawallet.bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/6/1017:36
 * desc   :
 * version: 1.0.0
 */
public class SolanaTokenTransferRecordDetailBean {

    private int blockTime;
    private int slot;
    private String txHash;
    private int fee;
    private String status;
    private int lamport;
    private String recentBlockhash;
    private String txStatus;
    private Object confirmations;
    private List<String> signer;
    private List<String> logMessage;
    private List<MainActionsBean> mainActions;

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

    public String getRecentBlockhash() {
        return recentBlockhash;
    }

    public void setRecentBlockhash(String recentBlockhash) {
        this.recentBlockhash = recentBlockhash;
    }

    public String getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(String txStatus) {
        this.txStatus = txStatus;
    }

    public Object getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Object confirmations) {
        this.confirmations = confirmations;
    }

    public List<String> getSigner() {
        return signer;
    }

    public void setSigner(List<String> signer) {
        this.signer = signer;
    }

    public List<String> getLogMessage() {
        return logMessage;
    }

    public void setLogMessage(List<String> logMessage) {
        this.logMessage = logMessage;
    }

    public List<MainActionsBean> getMainActions() {
        return mainActions;
    }

    public void setMainActions(List<MainActionsBean> mainActions) {
        this.mainActions = mainActions;
    }


    public static class MainActionsBean {
        /**
         * action : unknown-transfer
         * data : {"programId":"DKx3LkKkcBNA619n5ZDAcphXtE6GhrNM4kxzSRRJ7fbV","event":[{"source":"4v5hnZKMLoSTQ5wqFaVTNVAwkNMNXpSAS3Y2bLEN89cd","destination":"HNAyzMu9TzRPXSZgTPM85K3hBtXeLD2Xk5JTvutSTtX2","amount":"1000000000000","type":"transfer","decimals":9,"tokenAddress":"AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU","symbol":"IUX","icon":"https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU/logo.png","destinationOwner":"4wsGXb8G1DWAgrgXQTqhkCHQKm4Vgs9RSuovyd5KQx6Y","sourceOwner":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq"},{"source":"4mAcmk1bVQYx42EMjVTea4a4t2pueR8MNgKc66btay6U","destination":"4v5hnZKMLoSTQ5wqFaVTNVAwkNMNXpSAS3Y2bLEN89cd","amount":"0","type":"transfer","decimals":9,"tokenAddress":"AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU","symbol":"IUX","icon":"https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU/logo.png","destinationOwner":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","sourceOwner":"9kAfEXg6n8skZiXPpkxYkVYqQhGaesz9En4c8V8WRazE"}]}
         * method : Instruction 0
         * index : 0
         */

        private String action;
        private DataBean data;
        private String method;
        private int index;

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public static class DataBean {
            /**
             * programId : DKx3LkKkcBNA619n5ZDAcphXtE6GhrNM4kxzSRRJ7fbV
             * event : [{"source":"4v5hnZKMLoSTQ5wqFaVTNVAwkNMNXpSAS3Y2bLEN89cd","destination":"HNAyzMu9TzRPXSZgTPM85K3hBtXeLD2Xk5JTvutSTtX2","amount":"1000000000000","type":"transfer","decimals":9,"tokenAddress":"AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU","symbol":"IUX","icon":"https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU/logo.png","destinationOwner":"4wsGXb8G1DWAgrgXQTqhkCHQKm4Vgs9RSuovyd5KQx6Y","sourceOwner":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq"},{"source":"4mAcmk1bVQYx42EMjVTea4a4t2pueR8MNgKc66btay6U","destination":"4v5hnZKMLoSTQ5wqFaVTNVAwkNMNXpSAS3Y2bLEN89cd","amount":"0","type":"transfer","decimals":9,"tokenAddress":"AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU","symbol":"IUX","icon":"https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU/logo.png","destinationOwner":"HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq","sourceOwner":"9kAfEXg6n8skZiXPpkxYkVYqQhGaesz9En4c8V8WRazE"}]
             */

            private String programId;
            private List<EventBean> event;
            private String destination_owner;

            public String getDestination_owner() {
                return destination_owner;
            }

            public void setDestination_owner(String destination_owner) {
                this.destination_owner = destination_owner;
            }

            public String getProgramId() {
                return programId;
            }

            public void setProgramId(String programId) {
                this.programId = programId;
            }

            public List<EventBean> getEvent() {
                return event;
            }

            public void setEvent(List<EventBean> event) {
                this.event = event;
            }

            public static class EventBean {
                /**
                 * source : 4v5hnZKMLoSTQ5wqFaVTNVAwkNMNXpSAS3Y2bLEN89cd
                 * destination : HNAyzMu9TzRPXSZgTPM85K3hBtXeLD2Xk5JTvutSTtX2
                 * amount : 1000000000000
                 * type : transfer
                 * decimals : 9
                 * tokenAddress : AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU
                 * symbol : IUX
                 * icon : https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU/logo.png
                 * destinationOwner : 4wsGXb8G1DWAgrgXQTqhkCHQKm4Vgs9RSuovyd5KQx6Y
                 * sourceOwner : HieoAdW4WgMWi1ncNFhxsH4AFMEKahJ34jvNf2WFNtdq
                 */

                private String source;
                private String destination;
                private String amount;
                private String type;
                private int decimals;
                private String tokenAddress;
                private String symbol;
                private String icon;
                private String destinationOwner;
                private String sourceOwner;

                public String getSource() {
                    return source;
                }

                public void setSource(String source) {
                    this.source = source;
                }

                public String getDestination() {
                    return destination;
                }

                public void setDestination(String destination) {
                    this.destination = destination;
                }

                public String getAmount() {
                    return amount;
                }

                public void setAmount(String amount) {
                    this.amount = amount;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public int getDecimals() {
                    return decimals;
                }

                public void setDecimals(int decimals) {
                    this.decimals = decimals;
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

                public String getIcon() {
                    return icon;
                }

                public void setIcon(String icon) {
                    this.icon = icon;
                }

                public String getDestinationOwner() {
                    return destinationOwner;
                }

                public void setDestinationOwner(String destinationOwner) {
                    this.destinationOwner = destinationOwner;
                }

                public String getSourceOwner() {
                    return sourceOwner;
                }

                public void setSourceOwner(String sourceOwner) {
                    this.sourceOwner = sourceOwner;
                }
            }
        }
    }

}
