package com.qianlihu.solanawallet.api.retrofit;


import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.application.MyApplication;

import java.io.IOException;

/**
 * author : Duan
 * date   : 2021/4/89:12
 * desc   :
 * version: 1.0.0
 */
public class BaseException extends IOException {

    /**
     * 解析数据失败
     */
    public static final int PARSE_ERROR = 1001;
    public static final String PARSE_ERROR_MSG =  MyApplication.getContexts().getString(R.string.net_parsing);

    /**
     * 连接错误
     */
    public static final int BAD_NETWORK = 1002;
    public static final String BAD_NETWORK_MSG =  MyApplication.getContexts().getString(R.string.net_request);

    /**
     * 登录token失效
     */
//    public static final int BAD_TOKEN = 401;
//    public static final String BAD_TOKEN_MSG =  MyApplication.getContexts().getString(R.string.net_token);
    /**
     * 网络问题
     */
    public static final int CONNECT_ERROR = 1003;
    public static final String CONNECT_ERROR_MSG =  MyApplication.getContexts().getString(R.string.net_abnormal);
    /**
     * 连接超时
     */
    public static final int CONNECT_TIMEOUT = 1004;
    public static final String CONNECT_TIMEOUT_MSG =  MyApplication.getContexts().getString(R.string.net_connect);
    /**
     * 未知错误
     */
    public static final int OTHER = 1005;
    public static final String OTHER_MSG = MyApplication.getContexts().getString(R.string.net_unknown);


    private String errorMsg;
    private int errorCode;


    public String getErrorMsg() {
        return errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public BaseException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
        this.errorMsg = errorMsg;
    }

    public BaseException(String message, Throwable cause, int errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errorMsg = message;
    }

    public BaseException(String message, int errorCode) {
        this.errorCode = errorCode;
        this.errorMsg = message;
    }
}
