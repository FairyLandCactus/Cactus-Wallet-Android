package com.qianlihu.solanawallet.bean;

import java.io.Serializable;

/**
 * author : Duan
 * date   : 2022/2/12 14:41
 * desc   : app
 * version: 1.0.0
 */
public class VersionBean implements Serializable {

    private int versionCode;
    private String versionName;
    private String content;
    private String url;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
