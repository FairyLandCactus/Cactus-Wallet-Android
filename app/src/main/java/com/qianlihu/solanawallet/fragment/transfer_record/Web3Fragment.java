package com.qianlihu.solanawallet.fragment.transfer_record;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.SolscanWebActivity;
import com.qianlihu.solanawallet.adapter.ToolAdapter;
import com.qianlihu.solanawallet.adapter.Web3Adapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.TokenTypeBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author : Duan
 * date   : 2022/3/3015:01
 * desc   : web3
 * version: 1.0.0
 */
public class Web3Fragment extends BaseFragment {

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv_web3)
    RecyclerView rvWeb3;
    @BindView(R.id.tv_foot_tip)
    TextView tvFootTip;

    public static Fragment newInstance(String mainChain) {
        Web3Fragment fragment = new Web3Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("mainChain", mainChain);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_web3;
    }

    @Override
    public void initView() {
        String mainChain = getArguments().getString("mainChain");
        String url = "https://bscscan.com/";
        if (mainChain.equals(Constant.SOL_CHAIN)) {
            url = "https://solscan.io/";
        }
        List<TokenTypeBean> list = new ArrayList<>();
        TokenTypeBean bean = new TokenTypeBean();
        bean.setTokenName(getString(R.string.web3_spece));
        bean.setIcon(R.mipmap.icon_personal_space);
        bean.setWebUrl(url);
        list.add(bean);

        TokenTypeBean bean1 = new TokenTypeBean();
        bean1.setTokenName(getString(R.string.web3_friends_list));
        bean1.setIcon(R.mipmap.icon_friends_list);
        bean1.setWebUrl("");
        list.add(bean1);

        TokenTypeBean bean2 = new TokenTypeBean();
        bean2.setTokenName(getString(R.string.web3_world));
        bean2.setIcon(R.mipmap.icon_web3_word);
        bean2.setWebUrl("");
        list.add(bean2);

        TokenTypeBean bean3 = new TokenTypeBean();
        bean3.setTokenName(getString(R.string.web3_trajectory));
        bean3.setIcon(R.mipmap.icon_trajectory);
        bean3.setWebUrl("");
        list.add(bean3);

        Web3Adapter toolAdapter = new Web3Adapter(list);
        rvWeb3.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvWeb3.addItemDecoration(new GridSpaceItemDecoration(2, 40, 40));
        rvWeb3.setAdapter(toolAdapter);

        toolAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
    }

    @Override
    public void initData() {

    }
}
