package com.qianlihu.solanawallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.ChainNameAdapter;
import com.qianlihu.solanawallet.adapter.MyWallet2Adapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.ChainNameBean;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.WalletResource;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ManageWalletActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right_more)
    TextView tvRightMore;
    @BindView(R.id.rv_my_wallet)
    RecyclerView rvMyWallet;
    @BindView(R.id.rv_main_chain)
    RecyclerView rvMainChain;

    private String mWalletType = "0";
    private String mMainChain = Constant.SOL_CHAIN;
    private List<WalletBean> lists;
    private ChainNameAdapter mTypeAdapter;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manage_wallet;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.my_wallet));
        tvRightMore.setVisibility(View.VISIBLE);
        mWalletType = getIntent().getStringExtra("walletType");
        mMainChain = getIntent().getStringExtra("mainChain");
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();

        lists = LitePal.where("walletType = ? and mainChain = ?", mWalletType, mMainChain).find(WalletBean.class);

        rvMyWallet.setLayoutManager(new LinearLayoutManager(this));
        rvMainChain.setLayoutManager(new LinearLayoutManager(this));
        String[] mChianName = WalletResource.mChainName;
        int[] mTypeUnIcon = WalletResource.mTokenTypeUnIcon;
        int[] mTypeIcon = WalletResource.mTokenTypeIcon;
        List<ChainNameBean> beanList = new ArrayList<>();
        for (int i = 0; i < mTypeUnIcon.length; i++) {
            ChainNameBean typeBean = new ChainNameBean();
            typeBean.setIcon(mTypeUnIcon[i]);
            typeBean.setUnIcon(mTypeIcon[i]);
            typeBean.setChain(mChianName[i]);
            beanList.add(typeBean);
        }
        mTypeAdapter = new ChainNameAdapter(beanList);
        rvMainChain.setAdapter(mTypeAdapter);

        for (int i = 0; i < mChianName.length; i++) {
            if (mMainChain.equals(mChianName[i])) {
                mTypeAdapter.select(i);
            }
        }
        itemCheckWallet(lists);
        mTypeAdapter.setOnItemClickListener((adapter, view12, position) -> {
            lists = LitePal.where("walletType = ? and mainChain = ?", mWalletType, mChianName[position]).find(WalletBean.class);
            mTypeAdapter.select(position);
            if (lists.size() != 0) {
                itemCheckWallet(lists);
            } else {
                if (position > 2) {
                    rvMyWallet.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.not_yet_open))));
                    return;
                }
                rvMyWallet.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_wallet_data))));
            }
        });
    }

    private void itemCheckWallet(List<WalletBean> lists) {
        MyWallet2Adapter myWallet2Adapter = new MyWallet2Adapter(lists);
        rvMyWallet.setAdapter(myWallet2Adapter);
        myWallet2Adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("wallet", lists.get(position));
                intent.putExtras(bundle);
                intent.setClass(ManageWalletActivity.this, MyWalletActivity.class);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_right_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right_more:
                Intent intent = new Intent(this, AddWalletActivity.class);
                intent.putExtra("walletType", mWalletType);
                startActivity(intent);
                break;
        }
    }
}