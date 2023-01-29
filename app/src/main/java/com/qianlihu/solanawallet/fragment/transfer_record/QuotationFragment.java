package com.qianlihu.solanawallet.fragment.transfer_record;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

/**
 * author : Duan
 * date   : 2022/3/3015:01
 * desc   : 交易记录下的行情
 * version: 1.0.0
 */
public class QuotationFragment extends BaseFragment {
    @BindView(R.id.rv_record)
    RecyclerView rvRecord;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    public static Fragment newInstance() {
        QuotationFragment fragment = new QuotationFragment();
        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_transfer_record;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }
}
