package com.qianlihu.solanawallet.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.qianlihu.solanawallet.application.MyApplication;
import com.qianlihu.solanawallet.rpc.bean.mintTokenList.TokenMintBean;
import com.qianlihu.solanawallet.rpc.bean.mintTokenList.Tokens;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class TokenMintUtil {

//    private static final String JsonData;
//
//    static {
//        //static load to improve the speed
//        JsonData = new GetJsonDataUtil().getJson(MyApplication.getContexts(), "tokenlist.json");
//    }

    public static String getMintDecimal(String mint) {
        Gson gson = new Gson();
        TokenMintBean tokenMintBean = gson.fromJson("", TokenMintBean.class);
        List<Tokens> tokenList = tokenMintBean.getTokens();
        for (int i = 0; i < tokenList.size(); i++) {
            Tokens token = tokenList.get(i);
            if (mint.equals(token.getAddress())) {
                return token.getDecimals() + "";
            }
        }
        return "6"; // TODO update the decimal
    }

    public static String getJsonData() {
        return "";
    }

    public static void getTokenInfos(String jsondata) {
        Gson gson = new Gson();
        TokenMintBean tokenMintBean = gson.fromJson(jsondata, TokenMintBean.class);
        List<Tokens> tokenList = tokenMintBean.getTokens();
//        Log.i("duan==tokens", "加载完成111===   "+tokenList.size());
        Tokens tokens = new Tokens();
        tokens.setChainId(101);
        tokens.setName("ive");
        tokens.setSymbol("IVE");
        tokens.setAddress("4UWQ9oJduGa1z6dKHduqsUAtEfv5kBcCv7CuhgFoKjrm");
        tokens.setDecimals(9);
        tokens.setLogoURI("");
        tokenList.add(tokens);
        LitePal.deleteAll(Tokens.class);
        LitePal.saveAll(tokenList);
    }

    public static List<Tokens> showTokenInfos(List<Tokens> tokenList, String tokenAddress) {
        List<Tokens> result = new ArrayList<>();
        for (int i = 0; i < tokenList.size(); i++) {
            Tokens token = tokenList.get(i);
            if (token.getAddress().equals(tokenAddress)) {
                result.add(token);
            }
        }
        return result;
    }

    public static List<Tokens> searchTokens(String tokenName, List<Tokens> tokenList) {
        List<Tokens> searchResult = new ArrayList<>();
        for (int i = 0; i < tokenList.size(); i++) {
            Tokens token = tokenList.get(i);
            if (token.getName().toLowerCase().contains(tokenName.toLowerCase())) {
                searchResult.add(token);
            } else if (token.getSymbol().toLowerCase().contains(tokenName.toLowerCase())) {
                searchResult.add(token);
            } else if (token.getAddress().toLowerCase().contains(tokenName.toLowerCase())) {
                searchResult.add(token);
            }
        }
        return searchResult;
    }

    public static List<Tokens> searchTokenAddress(String tokenName, List<Tokens> tokenList) {
        List<Tokens> searchResult = new ArrayList<>();
        for (int i = 0; i < tokenList.size(); i++) {
            Tokens token = tokenList.get(i);
            if (token.getAddress().equals(tokenName)) {
                searchResult.add(token);
            }
        }
        return searchResult;
    }
}
