package com.qianlihu.solanawallet.wallet_bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/10/2714:18
 * desc   :
 * version: 1.0.0
 */
public class DappTypeBean {

    private int code;
    private String msg;
    private String time;
    private DataBean data;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total : 1
         * rows : [{"name":"测试","image":"http://walletadmin.ius.finance/uploads/20221024/7290ed52436b197f22dec959794a77cf.png","tags":"测试,测试1","describe":"","introduce":"","website":"","telegram":"","twitter":"","tip":"1.测试\r\n2.测试\r\n3.测试"}]
         */

        private int total;
        private List<RowsBean> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * name : 测试
             * image : http://walletadmin.ius.finance/uploads/20221024/7290ed52436b197f22dec959794a77cf.png
             * tags : 测试,测试1
             * describe :
             * introduce :
             * website :
             * telegram :
             * twitter :
             * tip : 1.测试
             2.测试
             3.测试
             */

            private String name;
            private String image;
            private String tags;
            private String describe;
            private String introduce;
            private String website;
            private String telegram;
            private String twitter;
            private String tip;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public String getTags() {
                return tags;
            }

            public void setTags(String tags) {
                this.tags = tags;
            }

            public String getDescribe() {
                return describe;
            }

            public void setDescribe(String describe) {
                this.describe = describe;
            }

            public String getIntroduce() {
                return introduce;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }

            public String getWebsite() {
                return website;
            }

            public void setWebsite(String website) {
                this.website = website;
            }

            public String getTelegram() {
                return telegram;
            }

            public void setTelegram(String telegram) {
                this.telegram = telegram;
            }

            public String getTwitter() {
                return twitter;
            }

            public void setTwitter(String twitter) {
                this.twitter = twitter;
            }

            public String getTip() {
                return tip;
            }

            public void setTip(String tip) {
                this.tip = tip;
            }
        }
    }
}
