package com.qianlihu.solanawallet.bean;

import org.litepal.crud.LitePalSupport;

/**
 * author : Duan
 * date   : 2022/5/511:22
 * desc   :
 * version: 1.0.0
 */
public class SearchHistoryDB extends LitePalSupport {

    private String content;
    private long dateTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
