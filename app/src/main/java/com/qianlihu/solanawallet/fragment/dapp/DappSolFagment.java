package com.qianlihu.solanawallet.fragment.dapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.DappBscListAdapter;
import com.qianlihu.solanawallet.adapter.DappTypeListAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.DappInfoBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.mvp.dapp.bean.DappBean;
import com.qianlihu.solanawallet.mvp.dapp.presenter.DappPresenter;
import com.qianlihu.solanawallet.mvp.dapp.view.DappView;
import com.qianlihu.solanawallet.track.TrackUsdtTransferbean;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.DialogUtils;
import com.qianlihu.solanawallet.utils.LoadWebPageUtils;
import com.qianlihu.solanawallet.wallet_bean.DappTypeBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * author : Duan
 * date   : 2021/11/311:43
 * desc   :
 * version: 1.0.0
 */
public class DappSolFagment extends BaseFragment<DappPresenter> implements DappView {

    @BindView(R.id.rv_item)
    RecyclerView rvItem;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_foot_tip)
    TextView tvFootTip;

    private int page = 1, pageSize = 10, total = 0, id = 2;

    public static Fragment newInstance(int id, int status) {
        DappSolFagment fragment = new DappSolFagment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public DappPresenter createPresenter() {
        return new DappPresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dapp_sol;
    }

    @Override
    public void initView() {

        rvItem.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItem.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_data))));
        setupRefreshView();
    }

    @Override
    public void initData() {
        presenter.getDappList(languageType, id, page, pageSize);
    }

    private void setupRefreshView() {
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(refreshlayout -> reloadAllData());
        //上拉加载
        refreshLayout.setOnLoadMoreListener(refreshlayout -> {
            page = page + 1;
            pageSize = pageSize + 10;
            if (total < pageSize) {
                tvFootTip.setVisibility(View.VISIBLE);
                refreshlayout.finishLoadMore();
                return;
            }
            presenter.getDappList(languageType, id, page, pageSize);
        });
    }

    //下拉刷新
    private void reloadAllData() {
        page = 1;
        pageSize = 10;
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setNoMoreData(false);
        tvFootTip.setVisibility(View.GONE);
        presenter.getDappList(languageType, id, page, pageSize);
    }

    //结束刷新
    public void finishRefresh() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void dappListCallback(DappBean bean) {
        finishRefresh();
        total = bean.getTotal();
        if (total > 0) {
            showSolDappUI(bean.getRows());
        } else {
            rvItem.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_data))));
        }
    }

    private void showSolDappUI(List<DappTypeBean.DataBean.RowsBean> beanList) {
        DappTypeListAdapter listAdapter = new DappTypeListAdapter(beanList);
        rvItem.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener((adapter, view, position) -> {
            DialogUtils.junmpDappDialog(getContext(), 1, beanList.get(position));
            new Thread(() -> TrackUsdtTransferbean.transferUsdt()).start();
        });

        listAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.tv_more) {
                DialogUtils.collectDialog(getContext(), beanList.get(position));
            }
        });
    }
}
