package com.qianlihu.solanawallet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.AirdropDetailActivity;
import com.qianlihu.solanawallet.adapter.AirdropChildAdapter;
import com.qianlihu.solanawallet.adapter.DappChildAdapter;
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
public class AirdropChildFagment extends BaseFragment {

    @BindView(R.id.rv_airdrop)
    RecyclerView rvAirdrop;

    public static Fragment newInstance(int id, int status) {
        AirdropChildFagment fragment = new AirdropChildFagment();
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
        return R.layout.fragment_airdrop_child;
    }

    @Override
    public void initView() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            list.add("1" + i);
        }
        rvAirdrop.setLayoutManager(new LinearLayoutManager(getContext()));
        AirdropChildAdapter childAdapter = new AirdropChildAdapter(list);
//        rvAirdrop.setAdapter(childAdapter);
        rvAirdrop.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_data))));
        childAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(getActivity(), AirdropDetailActivity.class));
            }
        });
    }

    @Override
    public void initData() {

    }
}
