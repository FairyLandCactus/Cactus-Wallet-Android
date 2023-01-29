package com.qianlihu.solanawallet.bean;

import java.io.Serializable;
import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/817:23
 * desc   :
 * version: 1.0.0
 */
public class TransferRecordBodyBean implements Serializable {


    /**
     * code : 200
     * msg : 操作成功
     * data : {"data":[{"id":2,"to_address":"0x85ae45A24b5c173594Efd6f56f94c2309CFEe7c2","from_address":"0x0949aB678094f72570839F776F2E800C1A6b70f8","record":"","addtime":"2022-03-08 17:06:21","coin":"0x906018e906a334c8c6f6f4dc0ad52b64fa1a1051"}]}
     */

    private int code;
    private String msg;
    private DataBeanX data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX implements Serializable{
        private List<DataBean> data;
        private int count;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public static class DataBean implements Serializable{
            /**
             * id : 2
             * to_address : 0x85ae45A24b5c173594Efd6f56f94c2309CFEe7c2
             * from_address : 0x0949aB678094f72570839F776F2E800C1A6b70f8
             * record :
             * addtime : 2022-03-08 17:06:21
             * coin : 0x906018e906a334c8c6f6f4dc0ad52b64fa1a1051
             */

            private int id;
            private String to_address;
            private String from_address;
            private String record;
            private String addtime;
            private String coin;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTo_address() {
                return to_address;
            }

            public void setTo_address(String to_address) {
                this.to_address = to_address;
            }

            public String getFrom_address() {
                return from_address;
            }

            public void setFrom_address(String from_address) {
                this.from_address = from_address;
            }

            public String getRecord() {
                return record;
            }

            public void setRecord(String record) {
                this.record = record;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public String getCoin() {
                return coin;
            }

            public void setCoin(String coin) {
                this.coin = coin;
            }
        }
    }
}
