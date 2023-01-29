package com.qianlihu.solanawallet.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qianlihu.solanawallet.adapter.ChainAdapter;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;

import org.litepal.LitePal;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/11/211:22
 * desc   :
 * version: 1.0.0
 */
public class WalletUtils {

    public static String usdPrice(String walletType) {
        String usd = SPUtils.getInstance().getString(Constant.USD_APPLY);
        if (TextUtils.isEmpty(usd)) {
            usd = "0";
        }
        if (walletType.equals(1)) {
            usd = SPUtils.getInstance().getString(Constant.USD_SAVE);
        }
        return usd;
    }

    public static double getUsd(String walletType, String puk, String symbol) {
        double usd = 0.0;
        List<AddTokenDB> dbList = LitePal.where("walletType = ? and walletAddress = ? and symbol = ?", walletType, puk, symbol).find(AddTokenDB.class);
        if (dbList.size() > 0) {
            usd = dbList.get(0).getUsd();
        }
        return usd;
    }

}
