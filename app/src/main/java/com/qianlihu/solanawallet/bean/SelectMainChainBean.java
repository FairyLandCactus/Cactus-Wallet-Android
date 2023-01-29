package com.qianlihu.solanawallet.bean;

/**
 * author : Duan
 * date   : 2022/3/1215:15
 * desc   : 自定义添加带币的选择主链实体类
 * version: 1.0.0
 */
public class SelectMainChainBean {

    private String name;
    private String symbol;
    private int logo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }
}
