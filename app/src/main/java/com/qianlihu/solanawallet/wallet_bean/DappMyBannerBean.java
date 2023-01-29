package com.qianlihu.solanawallet.wallet_bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/10/2614:33
 * desc   : dapp 我的里的轮播图实体类
 * version: 1.0.0
 */
public class DappMyBannerBean {


    /**
     * code : 1
     * msg : 获取成功
     * time : 1666765910
     * data : [{"title":"","image":"http://walletadmin.ius.finance/uploads/20221024/ec8e3e9fd567fc4317d6412224b5c42f.jpg","link":""}]
     */

    private int code;
    private String msg;
    private String time;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private String title;
        private String image;
        private String link;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
    }
}
