package com.qianlihu.solanawallet.bean;

/**
 * author : Duan
 * date   : 2022/3/2211:12
 * desc   :
 * version: 1.0.0
 */
public class TokenTypeBean {

    private int icon;
    private int unIcon;
    private String tokenName;
    private String webUrl;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getUnIcon() {
        return unIcon;
    }

    public void setUnIcon(int unIcon) {
        this.unIcon = unIcon;
    }

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
