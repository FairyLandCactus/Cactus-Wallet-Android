package com.qianlihu.solanawallet.fragment.swap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.BaseTabAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.utils.MagicTitleUtils;
import com.qianlihu.solanawallet.view.NoScrollViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author : Duan
 * date   : 2021/11/311:43
 * desc   :
 * version: 1.0.0
 */
public class SwapExchangeFagment extends BaseFragment {

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.vp_swap_exchange)
    NoScrollViewPager vpSwapExchange;

    private List<String> mDataList = new ArrayList<>();

    public static Fragment newInstance(int id, int status) {
        SwapExchangeFagment fragment = new SwapExchangeFagment();
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
        return R.layout.fragment_swap_exchange;
    }

    @Override
    public void initView() {
        mDataList.add(getString(R.string.swap_exchange));
        mDataList.add(getString(R.string.swap_exchange_mobility));
        MagicTitleUtils.magicTitleView(getActivity(), mDataList, magicIndicator, vpSwapExchange, R.color.txt_CAC0DF, R.color.txt_815BEC, R.color.txt_21D8FB, 18f, false);
        initFragment();
    }

    @Override
    public void initData() {

    }

    private void initFragment() {
        List<BaseFragment> list = new ArrayList<>();
        list.add((BaseFragment) SwapExchangeChildFragment.newInstance(1, 1));
        list.add((BaseFragment) SwapExchangeLiquidityFragment.newInstance(1, 1));

        BaseTabAdapter tabAdapter = new BaseTabAdapter(getChildFragmentManager(), list);
        vpSwapExchange.setAdapter(tabAdapter);
        vpSwapExchange.setOffscreenPageLimit(mDataList.size());
    }
}
