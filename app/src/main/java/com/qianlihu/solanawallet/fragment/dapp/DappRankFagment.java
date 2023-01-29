package com.qianlihu.solanawallet.fragment.dapp;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.DappRankListAdapter;
import com.qianlihu.solanawallet.adapter.DappRankTypeAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * author : Duan
 * date   : 2021/11/311:43
 * desc   :
 * version: 1.0.0
 */
public class DappRankFagment extends BaseFragment {

    @BindView(R.id.rv_type)
    RecyclerView rvType;
    @BindView(R.id.rv_item)
    RecyclerView rvItem;

    public static Fragment newInstance(int id, int status) {
        DappRankFagment fragment = new DappRankFagment();
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
        return R.layout.fragment_dapp_rank;
    }

    @Override
    public void initView() {

        List<String> list = new ArrayList<>();
        list.add(getString(R.string.bsc));
        list.add(getString(R.string.GameFi));
        list.add(getString(R.string.dex));
        list.add(getString(R.string.nft));
        list.add(getString(R.string.to_loan));
        list.add(getString(R.string.derivative));
        list.add(getString(R.string.aggregation));
        list.add(getString(R.string.Asset_Bridge));
        list.add(getString(R.string.Oracle));
        DappRankTypeAdapter typeAdapter = new DappRankTypeAdapter(list);
        rvType.setLayoutManager(new LinearLayoutManager(getContext()));
        rvType.setAdapter(typeAdapter);
        typeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                typeAdapter.select(position);
            }
        });

        List<String> list1 = new ArrayList<>();
        for (int i = 1; i < 12; i++) {
            list1.add("BabySwap" + i);
        }

        DappRankListAdapter listAdapter = new DappRankListAdapter(list1);
        rvItem.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItem.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_data))));
//        rvItem.setAdapter(listAdapter);
    }

    @Override
    public void initData() {

    }
}
