package com.qianlihu.solanawallet.activity;

import android.os.Bundle;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.BaseTabAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.fragment.manage_wallet.AddressBookFragment;
import com.qianlihu.solanawallet.fragment.manage_wallet.MyWalletFragment;
import com.qianlihu.solanawallet.fragment.transfer_record.AssetIntroductionFragment;
import com.qianlihu.solanawallet.fragment.transfer_record.ToolFragment;
import com.qianlihu.solanawallet.fragment.transfer_record.TransferRecordFragment;
import com.qianlihu.solanawallet.utils.MagicTitleUtils;
import com.qianlihu.solanawallet.view.NoScrollViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressManagerActivity extends BaseActivity {

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.vp_manager_wallet)
    NoScrollViewPager vpManagerWallet;

    private List<String> mDataList = new ArrayList<>();

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_address_manager;
    }

    @Override
    protected void initView() {
        mDataList.add(getString(R.string.address_book));
        mDataList.add(getString(R.string.my_wallet));
        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        int tabTextColor = R.color.white;
        if (!isDark) {
            tabTextColor = R.color.txt_333;
        }
        MagicTitleUtils.magicTitleView(this, mDataList, magicIndicator, vpManagerWallet, R.color.txt_5D6A84, tabTextColor, tabTextColor, 18f, false);
        initFragment();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void initFragment() {
        boolean isAddressSelect = getIntent().getBooleanExtra("isSelectAddress", false);
        List<BaseFragment> list = new ArrayList<>();
        list.add((BaseFragment) AddressBookFragment.newInstance(isAddressSelect));
        list.add((BaseFragment) MyWalletFragment.newInstance(isAddressSelect));

        BaseTabAdapter tabAdapter = new BaseTabAdapter(getSupportFragmentManager(), list);
        vpManagerWallet.setAdapter(tabAdapter);
        vpManagerWallet.setOffscreenPageLimit(mDataList.size());
    }
}