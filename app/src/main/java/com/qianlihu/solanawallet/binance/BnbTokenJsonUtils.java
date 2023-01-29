package com.qianlihu.solanawallet.binance;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qianlihu.solanawallet.application.MyApplication;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.rpc.bean.mintTokenList.TokenMintBean;
import com.qianlihu.solanawallet.rpc.bean.mintTokenList.Tokens;
import com.qianlihu.solanawallet.utils.GetJsonDataUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Duan
 * date   : 2022/3/318:25
 * desc   :
 * version: 1.0.0
 */
public class BnbTokenJsonUtils {

    private static final String JsonData;
    private static final String JsonData2;

    static {
        //static load to improve the speed
        JsonData = new GetJsonDataUtil().getJson(MyApplication.getContexts(), "bnbToken.json");
        JsonData2 = new GetJsonDataUtil().getJson(MyApplication.getContexts(), "ethToken.json");
    }

    public static  List<BNBTokenBean> getTokenInfos(String chain) {
        Gson gson = new Gson();
        List<BNBTokenBean> list = null;
        if (chain.equals(Constant.BNB_CHAIN)) {
            list = gson.fromJson(JsonData, new TypeToken<List<BNBTokenBean>>() {
            }.getType());
        } else {
            list = gson.fromJson(JsonData2, new TypeToken<List<BNBTokenBean>>() {
            }.getType());
        }
        return list;
    }

    public static List<BNBTokenBean> searchTokens(String tokenName, String chain) {
        List<BNBTokenBean> tokenList = getTokenInfos(chain);
        List<BNBTokenBean> searchResult = new ArrayList<>();
        for (int i = 0; i < tokenList.size(); i++) {
            BNBTokenBean token = tokenList.get(i);
            if (token.getName().toLowerCase().contains(tokenName.toLowerCase())) {
                searchResult.add(token);
            } else if (token.getName().toLowerCase().contains(tokenName.toLowerCase())) {
                searchResult.add(token);
            } else if (token.getContractAdd().toLowerCase().contains(tokenName.toLowerCase())) {
                searchResult.add(token);
            }
        }
        return searchResult;
    }
}
