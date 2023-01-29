package com.qianlihu.solanawallet.wallet_bean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/10/2615:14
 * desc   : 文章列表实体类
 * version: 1.0.0
 */
public class ArticlesListBean {
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
         * id : 2
         * title : 测试文章1
         * author : 测试文章1
         * tags : 测试文章1
         * image :
         * createtime : 1666692565
         */

        private int id;
        private String title;
        private String author;
        private String tags;
        private String image;
        private Long createtime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Long getCreatetime() {
            return createtime;
        }

        public void setCreatetime(Long createtime) {
            this.createtime = createtime;
        }
    }
}
