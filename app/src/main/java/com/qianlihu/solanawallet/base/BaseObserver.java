package com.qianlihu.solanawallet.base;

import com.google.gson.JsonParseException;
import com.orhanobut.logger.Logger;
import com.qianlihu.solanawallet.api.retrofit.BaseException;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.observers.DisposableObserver;
import retrofit2.HttpException;

/**
 * author : Duan
 * date   : 2021/4/89:14
 * desc   :
 * version: 1.0.0
 */
public abstract class BaseObserver<T> extends DisposableObserver<T> {

    protected BaseView view;

    private boolean isShowDialog;


    public BaseObserver(BaseView view) {
        this.view = view;
    }

    public BaseObserver(BaseView view, boolean isShowDialog) {
        this.view = view;
        this.isShowDialog = isShowDialog;
    }

    @Override
    protected void onStart() {
        if (view != null && isShowDialog) {
            view.showLoading();
        }
    }

    @Override
    public void onNext(T o) {
        onSuccess(o);
    }

    @Override
    public void onError(Throwable e) {
        if (view != null && isShowDialog) {
            view.hideLoading();
        }
        BaseException be = null;
        Logger.t("Exception:").e(e.toString());
        if (e != null) {
            if (e instanceof BaseException) {
                be = (BaseException) e;

                //回调到view层 处理 或者根据项目情况处理
                if (view != null) {
                    view.onErrorCode(be.getErrorCode(), be.getErrorMsg());
                } else {
                    onError(be.getErrorMsg(), be.getErrorCode());
                }

            } else {
                if (e instanceof HttpException) {
                    //   HTTP错误
                    be = new BaseException(BaseException.BAD_NETWORK_MSG, e, BaseException.BAD_NETWORK);

                } else if (e instanceof ConnectException
                        || e instanceof UnknownHostException) {
                    //   连接错误
                    be = new BaseException(BaseException.CONNECT_ERROR_MSG, e, BaseException.CONNECT_ERROR);
                } else if (e instanceof InterruptedIOException) {
                    //  连接超时
                    be = new BaseException(BaseException.CONNECT_TIMEOUT_MSG, e, BaseException.CONNECT_TIMEOUT);
                } else if (e instanceof JsonParseException
                        || e instanceof JSONException
                        || e instanceof ParseException) {
                    //  解析错误
                    be = new BaseException(BaseException.PARSE_ERROR_MSG, e, BaseException.PARSE_ERROR);
                } else {
                    be = new BaseException(BaseException.OTHER_MSG, e, BaseException.OTHER);
                }
                view.onErrorCode(be.getErrorCode(), be.getErrorMsg());
            }
        } else {
            be = new BaseException(BaseException.OTHER_MSG, e, BaseException.OTHER);
        }
        onError(be.getErrorMsg(), be.getErrorCode());
    }

    @Override
    public void onComplete() {
        if (view != null && isShowDialog) {
            view.hideLoading();
        }
    }

    public abstract void onSuccess(T o);

    public abstract void onError(String msg, int code);
}
