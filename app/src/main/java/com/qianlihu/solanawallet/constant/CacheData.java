package com.qianlihu.solanawallet.constant;


import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.bean.UpdateBalanceDataBean;
import com.qianlihu.solanawallet.bean.WalletBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author : Duan
 * date   : 2021/4/810:04
 * desc   :
 * version: 1.0.0
 */
public class CacheData {

    private static volatile CacheData data = new CacheData();

    private CacheData() {
    }

    public static CacheData shared() {
        return data;
    }
    //收款地址页面密码
    public String collectionPwd = "";
    //更新钱包信息标示
    public int updateWalletInfoFlag = 0;
    public int applyFlag = 0;
    public int saveFlag = 0;
    //钱包类型
    public String walletType = "0";
    //是否连网
    public boolean isConnect = false;
    //usdt
    public List<Map<String, String>> tickerList = new ArrayList<>();
    //语言类型
    public String languageType;
    //是否是转账页面跳转到首页兑换
    public boolean isExchange = false;
    //行情页面是否显示
    public boolean isSwapQShow = false;
    //转账地址
    public String address = "";
    //转账更新余额数据
    public UpdateBalanceDataBean ubdBean = null;
    //转账记录请求是否完成
    public boolean isRecordRequst = false;
    public boolean isRecordRequst2 = false;
    //代币信息实体类
    public AddTokenDB addTokenDB;
    //转账金额
    public boolean isTransfer = false;

    //是否打开麦克风
    public boolean isOpenMike = false;
    //是否打开视频
    public boolean isVideo = false;
    //是否打开扬声器
    public boolean isSpeaker = false;
    //是否悬浮窗
    public boolean isFloat = false;
    //悬浮窗跳转数据
    public String roomNo = "";
    public String userName = "";
    public String userHeader = "";
    public WalletBean walletBean = null;
}
