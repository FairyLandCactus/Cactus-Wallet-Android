package com.qianlihu.solanawallet.fragment.dapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.ArticlesDetailWebActivity;
import com.qianlihu.solanawallet.adapter.DappInterestAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.mvp.mine.bean.BannerBean;
import com.qianlihu.solanawallet.mvp.mine.presenter.MinePresenter;
import com.qianlihu.solanawallet.mvp.mine.view.MineView;
import com.qianlihu.solanawallet.utils.LoadWebPageUtils;
import com.qianlihu.solanawallet.utils.MyImageLoader;
import com.qianlihu.solanawallet.wallet_bean.ArticlesListBean;
import com.qianlihu.solanawallet.wallet_bean.DappMyBannerBean;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import jnr.ffi.annotations.In;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * author : Duan
 * date   : 2021/11/311:43
 * desc   :
 * version: 1.0.0
 */
public class DappMyFagment extends BaseFragment<MinePresenter> implements MineView {

    @BindView(R.id.tv_hide)
    TextView tvHide;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rv_might)
    RecyclerView rvMight;
    @BindView(R.id.ll_interest)
    LinearLayout llInterest;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_foot_tip)
    TextView tvFootTip;

    private boolean isHide = false;
    private int page = 1, pageSize = 10, total = 0;

    public static Fragment newInstance(int id, int status) {
        DappMyFagment fragment = new DappMyFagment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", id);
        bundle.putInt("status", status);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public MinePresenter createPresenter() {
        return new MinePresenter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_dapp_my;
    }

    @Override
    public void initView() {
        rvMight.setLayoutManager(new LinearLayoutManager(getContext()));
        setupRefreshView();
    }

    @Override
    public void initData() {

        presenter.getBanner(languageType);
        presenter.getNewsList(languageType, page, pageSize);
    }

    @OnClick({R.id.tv_hide, R.id.tv_cross})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_hide:
                if (isHide) {
                    llInterest.setVisibility(View.GONE);
                    isHide = false;
                } else {
                    llInterest.setVisibility(View.VISIBLE);
                    isHide = true;
                }
                break;
            case R.id.tv_cross:
                String url = "http://jhweb.ius.finance/";
                LoadWebPageUtils.activityIntent2(getContext(), getString(R.string.Aggregate_transaction), url);
                break;
        }
    }

    @Override
    public void adBannerCallback(List<BannerBean> list) {
        if (list.size() > 0) {
            showBannerImage(list);
        }
    }

    @Override
    public void newListCallback(ArticlesListBean bean) {
        finishRefresh();
        total = bean.getTotal();
        if (total > 0) {
            showArticlesListUI(bean.getRows());
        } else {
            rvMight.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_data))));
        }
    }

    private void showBannerImage(List<BannerBean> beanList) {
        MyImageLoader myImageLoader = new MyImageLoader(20);
        List<String> bannerList = new ArrayList<>();
        for (int i = 0; i < beanList.size(); i++) {
            bannerList.add(beanList.get(i).getImage());
        }

        if (banner == null) {
            return;
        }
        //设置图片集合
        banner.setImages(bannerList);
        //设置图片加载器
        banner.setImageLoader(myImageLoader);
        banner.setDelayTime(3000);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.setOnBannerListener(position -> {
            String url = beanList.get(position).getLink();
            LoadWebPageUtils.activityIntent2(getContext(), "", url);
        });
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
            presenter.getNewsList(languageType, page, pageSize);
        });
    }

    //下拉刷新
    private void reloadAllData() {
        page = 1;
        pageSize = 10;
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setNoMoreData(false);
        tvFootTip.setVisibility(View.GONE);
        presenter.getNewsList(languageType, page, pageSize);
    }

    //结束刷新
    public void finishRefresh() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    }

    private void showArticlesListUI(List<ArticlesListBean.RowsBean> list) {
        DappInterestAdapter interestAdapter = new DappInterestAdapter(list);
        rvMight.setAdapter(interestAdapter);

        interestAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getContext(), ArticlesDetailWebActivity.class);
            intent.putExtra("id", list.get(position).getId());
            startActivity(intent);
        });
    }
}
