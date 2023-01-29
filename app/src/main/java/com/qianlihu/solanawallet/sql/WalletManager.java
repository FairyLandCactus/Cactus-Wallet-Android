package com.qianlihu.solanawallet.sql;

import com.qianlihu.solanawallet.bean.TransactionDB;
import com.qianlihu.solanawallet.bean.WalletBean;

import org.litepal.LitePal;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/913:52
 * desc   :
 * version: 1.0.0
 */
public class WalletManager {

    private static final WalletManager ourInstance = new WalletManager();

    public static WalletManager getInstance() {
        return ourInstance;
    }

    //获取钱包类型相关的信息
    public List<WalletBean> getWalletTypeInfo(String type) {
        List<WalletBean> beanList = LitePal.where("walletType = ?", type).find(WalletBean.class);
        return beanList;
    }

    //通过钱包地址查询钱包信息
    public WalletBean getWalletInfo(String puk) {
        WalletBean bean = null;
        List<WalletBean> beanList = LitePal.where("pubKey = ?", puk).find(WalletBean.class);
        if (beanList.size() > 0) {
            bean = beanList.get(0);
        }
        return bean;
    }

    //Solana交易记录
    public List<TransactionDB> solTransferRecord(String puk, String walletType, String mint, int limit) {
        List<TransactionDB> list = LitePal.where("myPuk = ? and walletType = ? and mint = ?", puk, walletType, mint)
                .order("dateTime desc")
                .limit(limit)
                .find(TransactionDB.class);
        return list;
    }

    public List<TransactionDB> solSiginTransferRecord(String puk, String walletType, String mint, String sigin, int limit) {
        List<TransactionDB> list = LitePal.where("myPuk = ? and walletType = ? and mint = ? and signatures = ?", puk, walletType, mint, sigin)
                .order("dateTime desc")
                .limit(limit)
                .find(TransactionDB.class);
        return list;
    }
}
