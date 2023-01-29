package com.qianlihu.solanawallet.bean;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Duan
 * date   : 2022/1/1018:02
 * desc   :
 * version: 1.0.0
 */
public class TransactionWalletPukDB extends LitePalSupport {

    private String pubKey;

    private List<TransactionDB> dbList = new ArrayList<>();

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    public List<TransactionDB> getDbList() {
        return dbList;
    }

    public void setDbList(List<TransactionDB> dbList) {
        this.dbList = dbList;
    }
}
