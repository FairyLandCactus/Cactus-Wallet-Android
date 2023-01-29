package com.qianlihu.solanawallet.mvp.dapp.view;

import com.qianlihu.solanawallet.base.BaseView;
import com.qianlihu.solanawallet.mvp.dapp.bean.DappBean;

/**
 * author : Duan
 * date   : 2022/11/417:53
 * desc   :
 * version: 1.0.0
 */
public interface DappView extends BaseView {

    void dappListCallback(DappBean bean);
}
