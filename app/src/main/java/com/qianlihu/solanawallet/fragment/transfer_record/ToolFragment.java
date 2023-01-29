package com.qianlihu.solanawallet.fragment.transfer_record;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.SolscanWebActivity;
import com.qianlihu.solanawallet.adapter.ToolAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.TokenTypeBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jnr.ffi.annotations.In;

/**
 * author : Duan
 * date   : 2022/3/3015:01
 * desc   : 工具
 * version: 1.0.0
 */
public class ToolFragment extends BaseFragment {

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rv_tool)
    RecyclerView rvTool;
    @BindView(R.id.tv_foot_tip)
    TextView tvFootTip;

    public static Fragment newInstance(String mainChain) {
        ToolFragment fragment = new ToolFragment();
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
        return R.layout.fragment_tool;
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
        bean.setTokenName(getString(R.string.tool_block_browser));
        bean.setIcon(R.mipmap.icon_tool_qukuai);
        bean.setWebUrl(url);
        list.add(bean);

        TokenTypeBean bean1 = new TokenTypeBean();
        bean1.setTokenName(getString(R.string.tool_detection));
        bean1.setIcon(R.mipmap.icon_tool_shouquan);
        bean1.setWebUrl("");
        list.add(bean1);

        TokenTypeBean bean2 = new TokenTypeBean();
        bean2.setTokenName(getString(R.string.tool_defi));
        bean2.setIcon(R.mipmap.icon_tool_defi);
        bean2.setWebUrl("");
        list.add(bean2);

        ToolAdapter toolAdapter = new ToolAdapter(list);
        rvTool.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTool.setAdapter(toolAdapter);

        toolAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    Intent intent = new Intent(getActivity(), SolscanWebActivity.class);
                    intent.putExtra(Constant.WEB_TITLE, list.get(position).getTokenName());
                    intent.putExtra(Constant.WEB_URL, list.get(position).getWebUrl());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void initData() {

    }
}
