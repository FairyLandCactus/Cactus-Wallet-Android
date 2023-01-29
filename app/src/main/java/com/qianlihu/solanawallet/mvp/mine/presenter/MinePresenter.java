package com.qianlihu.solanawallet.mvp.mine.presenter;

import com.qianlihu.solanawallet.api.retrofit.ApiResponse;
import com.qianlihu.solanawallet.api.retrofit.ApiServer;
import com.qianlihu.solanawallet.base.BaseObserver;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.mvp.mine.bean.BannerBean;
import com.qianlihu.solanawallet.mvp.mine.view.MineView;
import com.qianlihu.solanawallet.wallet_bean.ArticlesListBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/11/1617:50
 * desc   :
 * version: 1.0.0
 */
public class MinePresenter extends BasePresenter<MineView> {
    public MinePresenter(MineView baseView) {
        super(baseView);
    }

    public void getBanner(String languge) {
        addDisposable(apiServer.bannerImge(languge), new BaseObserver<ApiResponse<List<BannerBean>>>(baseView) {
            @Override
            public void onSuccess(ApiResponse<List<BannerBean>> data) {
                baseView.adBannerCallback(data.getData());
            }

            @Override
            public void onError(String msg, int code) {

            }
        });
    }

    public void getNewsList(String languge, int page, int pageSize) {
        addDisposable(apiServer.getNewsList(languge, page, pageSize), new BaseObserver<ApiResponse<ArticlesListBean>>(baseView) {
            @Override
            public void onSuccess(ApiResponse<ArticlesListBean> data) {
                baseView.newListCallback(data.getData());
            }

            @Override
            public void onError(String msg, int code) {

            }
        });
    }
}
