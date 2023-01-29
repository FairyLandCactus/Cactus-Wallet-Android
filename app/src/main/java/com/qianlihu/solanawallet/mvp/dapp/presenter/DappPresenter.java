package com.qianlihu.solanawallet.mvp.dapp.presenter;

import com.qianlihu.solanawallet.api.retrofit.ApiResponse;
import com.qianlihu.solanawallet.api.retrofit.ApiServer;
import com.qianlihu.solanawallet.base.BaseObserver;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.mvp.dapp.bean.DappBean;
import com.qianlihu.solanawallet.mvp.dapp.view.DappView;

/**
 * author : Duan
 * date   : 2022/11/417:50
 * desc   :
 * version: 1.0.0
 */
public class DappPresenter extends BasePresenter<DappView> {
    public DappPresenter(DappView baseView) {
        super(baseView);
    }

    public void getDappList(String language, int id, int page, int pageSize){
        
        addDisposable(apiServer.getDapp(language, id, page, pageSize), new BaseObserver<ApiResponse<DappBean>>(baseView) {
            @Override
            public void onSuccess(ApiResponse<DappBean> data) {
                baseView.dappListCallback(data.getData());
            }
            @Override
            public void onError(String msg, int code) {

            }
        });
    }

}
