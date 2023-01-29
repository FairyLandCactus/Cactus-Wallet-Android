package com.qianlihu.solanawallet.rpc.bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2021/12/2316:21
 * desc   :
 * version: 1.0.0
 */
public class ProgramAccountsBean {

    private String jsonrpc;
    private String id;
    private List<ResultBean> result;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * account : {"data":["g/mXw/0pdEjWg/bFionEIXw/N3X3kt7pnwITJ8nzwRASbCtviLdNnzmvDwdaPuLqTNEH0XQMRXYBW4J5d9goHwB4QcsCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","base64"],"executable":false,"lamports":2039280,"owner":"TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA","rentEpoch":238}
         * pubkey : EeuA1bXnFUMho7d3K3GHCqWx88SpQepCmgD72cDbYpg5
         */

        private AccountBean account;
        private String pubkey;

        public AccountBean getAccount() {
            return account;
        }

        public void setAccount(AccountBean account) {
            this.account = account;
        }

        public String getPubkey() {
            return pubkey;
        }

        public void setPubkey(String pubkey) {
            this.pubkey = pubkey;
        }

        public static class AccountBean {
            /**
             * data : ["g/mXw/0pdEjWg/bFionEIXw/N3X3kt7pnwITJ8nzwRASbCtviLdNnzmvDwdaPuLqTNEH0XQMRXYBW4J5d9goHwB4QcsCAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA","base64"]
             * executable : false
             * lamports : 2039280
             * owner : TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA
             * rentEpoch : 238
             */

            private boolean executable;
            private long lamports;
            private String owner;
            private long rentEpoch;
            private List<String> data;

            public boolean isExecutable() {
                return executable;
            }

            public void setExecutable(boolean executable) {
                this.executable = executable;
            }

            public long getLamports() {
                return lamports;
            }

            public void setLamports(long lamports) {
                this.lamports = lamports;
            }

            public String getOwner() {
                return owner;
            }

            public void setOwner(String owner) {
                this.owner = owner;
            }

            public long getRentEpoch() {
                return rentEpoch;
            }

            public void setRentEpoch(long rentEpoch) {
                this.rentEpoch = rentEpoch;
            }

            public List<String> getData() {
                return data;
            }

            public void setData(List<String> data) {
                this.data = data;
            }
        }
    }
}
