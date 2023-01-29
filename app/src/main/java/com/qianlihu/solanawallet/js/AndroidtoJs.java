package com.qianlihu.solanawallet.js;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;
import com.qianlihu.solanawallet.application.MyApplication;
import com.qianlihu.solanawallet.event.ConfirmPwdEvent;
import com.qianlihu.solanawallet.event.WebInviteEvent;
import com.qianlihu.solanawallet.event.WebTransferEvent;
import com.qianlihu.solanawallet.solanadapp.bean.AddLiquidityBean;
import com.qianlihu.solanawallet.solanadapp.bean.InitializePoolBean;
import com.qianlihu.solanawallet.solanadapp.bean.RemoveLiquidityBean;
import com.qianlihu.solanawallet.solanadapp.bean.SwapBean;

import org.greenrobot.eventbus.EventBus;

import es.dmoral.toasty.Toasty;

/**
 * author : Duan
 * date   : 2022/5/414:20
 * desc   :
 * version: 1.0.0
 */
public class AndroidtoJs extends Object {

    private String tag = "duan==JsToAndroid";

    //邀请好友
    @JavascriptInterface
    public void toInvitation() {
        EventBus.getDefault().post(new WebInviteEvent(true));

    }

    //ivy兑换
    @JavascriptInterface
    public void sendTransferSign(String data) {
        EventBus.getDefault().post(new WebTransferEvent(data));
        Log.i(tag, "sendTransferSign ===  " + data);
    }

    //ivy签名输入密码
    @JavascriptInterface
    public void confirmPassword() {
        EventBus.getDefault().post(new ConfirmPwdEvent());
    }

    // 添加流动性
    @JavascriptInterface
    public void addLiquidity(String data) {
        Log.i(tag, "addLiquidity ===  " + data);
        if (!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            AddLiquidityBean bean = gson.fromJson(data, AddLiquidityBean.class);
            EventBus.getDefault().post(bean);
        }
    }

    // 移除流动性
    @JavascriptInterface
    public void removeLiquidity(String data) {
        Log.i(tag, "removeLiquidity ===  " + data);
        if (!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            RemoveLiquidityBean bean = gson.fromJson(data, RemoveLiquidityBean.class);
            EventBus.getDefault().post(bean);
        }
    }

    // 初始化流动池
    @JavascriptInterface
    public void initializePool(String data) {
        Log.i(tag, "initializePool ===  " + data);
        if (!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            InitializePoolBean bean = gson.fromJson(data, InitializePoolBean.class);
            EventBus.getDefault().post(bean);
        }
    }

    // swap交易
    @JavascriptInterface
    public void swap(String data) {
        Log.i(tag, "swap ===  " + data);
        if (!TextUtils.isEmpty(data)) {
            Gson gson = new Gson();
            SwapBean bean = gson.fromJson(data, SwapBean.class);
            EventBus.getDefault().post(bean);
        }
    }
}
