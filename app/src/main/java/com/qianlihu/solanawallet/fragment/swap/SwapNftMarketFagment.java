package com.qianlihu.solanawallet.fragment.swap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;

/**
 * author : Duan
 * date   : 2021/11/311:43
 * desc   :
 * version: 1.0.0
 */
public class SwapNftMarketFagment extends BaseFragment {

    public static Fragment newInstance(int id, int status) {
        SwapNftMarketFagment fragment = new SwapNftMarketFagment();
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
        return R.layout.fragment_swap_nft_market;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
