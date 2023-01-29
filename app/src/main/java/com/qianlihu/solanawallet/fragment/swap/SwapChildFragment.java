package com.qianlihu.solanawallet.fragment.swap;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.BaseTabAdapter;
import com.qianlihu.solanawallet.adapter.YuanUniverseAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.fragment.KLineFragment;
import com.qianlihu.solanawallet.utils.MagicTitleUtils;
import com.qianlihu.solanawallet.view.NoScrollViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author : Duan
 * date   : 2021/11/213:47
 * desc   :
 * version: 1.0.0
 */
public class SwapChildFragment extends BaseFragment {

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.vp_swap)
    NoScrollViewPager vpSwap;
    @BindView(R.id.rv_revenue_record)
    RecyclerView rvRevenueRecord;

    private String[] mTabTitleArray = {"Daily", "Weekly", "Quarerly", "Sundy"};
    private List<String> mDataList = new ArrayList<>();

    public static Fragment newInstance(int id, int status) {
        SwapChildFragment fragment = new SwapChildFragment();
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
        return R.layout.fragment_swap_child;
    }

    @Override
    public void initView() {
        mDataList.add(getString(R.string.Daily));
        mDataList.add(getString(R.string.Weekly));
        mDataList.add(getString(R.string.Quarerly));
        mDataList.add(getString(R.string.Sundy));
        MagicTitleUtils.magicTitleView(getActivity(), mDataList, magicIndicator, vpSwap, R.color.txt_666, R.color.white, R.color.txt_09C8A1, 16f, false);
        initFragment();
    }

    @Override
    public void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("IUS" + i);
        }
        rvRevenueRecord.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRevenueRecord.setAdapter(new YuanUniverseAdapter(list));
    }

    private void initFragment() {
        List<BaseFragment> list = new ArrayList<>();
        for (int i = 0; i < mDataList.size(); i++) {
            list.add((BaseFragment) KLineFragment.newInstance(1, 1));
        }
        BaseTabAdapter tabAdapter = new BaseTabAdapter(getChildFragmentManager(), list);
        vpSwap.setAdapter(tabAdapter);
        vpSwap.setOffscreenPageLimit(mDataList.size());
    }
}
