package com.qianlihu.solanawallet.mvp.mine.view;

import com.qianlihu.solanawallet.base.BaseView;
import com.qianlihu.solanawallet.mvp.mine.bean.BannerBean;
import com.qianlihu.solanawallet.wallet_bean.ArticlesListBean;

import java.util.List;

/**
 * author : Duan
 * date   : 2022/11/1617:47
 * desc   :
 * version: 1.0.0
 */
public interface MineView extends BaseView {

    void adBannerCallback(List<BannerBean> list);

    void newListCallback(ArticlesListBean bean);
}
