package com.qianlihu.solanawallet.event;

import com.qianlihu.solanawallet.bean.TransactionDB;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/1/189:24
 * desc   :
 * version: 1.0.0
 */
public class TransctionRecordEvent {

    private List<TransactionDB> dbList;

    public TransctionRecordEvent(List<TransactionDB> dbList) {
        this.dbList = dbList;
    }

    public List<TransactionDB> getDbList() {
        return dbList;
    }

    public void setDbList(List<TransactionDB> dbList) {
        this.dbList = dbList;
    }
}
