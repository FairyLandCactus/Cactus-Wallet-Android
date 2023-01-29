package com.qianlihu.solanawallet.mvp.dapp.bean;

import com.qianlihu.solanawallet.wallet_bean.DappTypeBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/11/417:59
 * desc   :
 * version: 1.0.0
 */
public class DappBean {

    private int total;
    private List<DappTypeBean.DataBean.RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DappTypeBean.DataBean.RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<DappTypeBean.DataBean.RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {

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
