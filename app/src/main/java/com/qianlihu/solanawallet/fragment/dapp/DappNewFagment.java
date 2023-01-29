package com.qianlihu.solanawallet.fragment.dapp;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.fragment.app.Fragment;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.LoadWebPageUtils;

import butterknife.BindView;

/**
 * author : Duan
 * date   : 2021/11/311:43
 * desc   :
 * version: 1.0.0
 */
public class DappNewFagment extends BaseFragment {

    @BindView(R.id.wv_h5_QA)
    WebView wvH5QA;

    public static Fragment newInstance(int id, int status) {
        DappNewFagment fragment = new DappNewFagment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dapp_new;
    }

    @Override
    public void initView() {
//        LoadWebPageUtils.webViewPage(getActivity(), wvH5QA,Constant.WEB_URL_IRS);
    }

    @Override
    public void initData() {

    }
}
