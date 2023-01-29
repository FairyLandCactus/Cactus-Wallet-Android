package com.qianlihu.solanawallet.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

/**
 * author : Duan
 * date   : 2021/12/811:42
 * desc   :
 * version: 1.0.0
 */
public class WalletBean extends LitePalSupport implements Serializable {

    private String pvaKey;
    private String name;
    private String pubKey;
    private String mnemonics;
    private String password;
    private Integer select;
    private String walletType;
    private String mainChain;

    public String getPvaKey() {
        return pvaKey;
    }

    public void setPvaKey(String pvaKey) {
        this.pvaKey = pvaKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public String getMnemonics() {
        return mnemonics;
    }

    public void setMnemonics(String mnemonics) {
        this.mnemonics = mnemonics;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public void setSelect(Integer select) {
        this.select = select;
    }

    public String getWalletType() {
        return walletType;
    }

    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    public String getMainChain() {
        return mainChain;
    }

    public void setMainChain(String mainChain) {
        this.mainChain = mainChain;
    }

    @Override
    public String toString() {
        return "WalletBean{" +
                "pvaKey='" + pvaKey + '\'' +
                ", name='" + name + '\'' +
                ", pubKey='" + pubKey + '\'' +
                ", mnemonics='" + mnemonics + '\'' +
                ", password='" + password + '\'' +
                ", select=" + select +
                ", walletType='" + walletType + '\'' +
                ", mainChain='" + mainChain + '\'' +
                '}';
    }
}
