package com.qianlihu.solanawallet.fragment.transfer_record;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.MyMoreText;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author : Duan
 * date   : 2022/3/3015:01
 * desc   : 资产介绍
 * version: 1.0.0
 */
public class AssetIntroductionFragment extends BaseFragment {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_project)
    TextView tvProject;
    @BindView(R.id.tv_total_circulation)
    TextView tvTotalCirculation;
    @BindView(R.id.tv_about)
    TextView tvAbout;
    @BindView(R.id.tv_introduction)
    TextView tvIntroduction;
    @BindView(R.id.tv_put_all)
    TextView tvPutAll;
    @BindView(R.id.tv_token_address)
    TextView tvTokenAddress;

    private AddTokenDB db;

    private String dec;

    public static Fragment newInstance(AddTokenDB db, String mainChain) {
        AssetIntroductionFragment fragment = new AssetIntroductionFragment();
        Bundle bundle = new Bundle();
        bundle.putString("mainChain", mainChain);
        bundle.putSerializable("addToken", db);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_asset_introduction;
    }

    @Override
    public void initView() {
        String mainChain = getArguments().getString("mainChain");
        if (mainChain.equals(Constant.SOL_CHAIN)) {
            dec = getString(R.string.solana_dec);
        } else {
            dec = getString(R.string.bnb_dec);
        }

        MyMoreText.getInstance().getLastIndexForLimit(tvIntroduction, 4, dec);
    }

    @Override
    public void initData() {
        db = (AddTokenDB) getArguments().getSerializable("addToken");
        tvName.setText(db.getSymbol());
        tvProject.setText(db.getName());
        tvTokenAddress.setText(db.getTokenAddress());
    }

    @OnClick({R.id.tv_token_address, R.id.tv_put_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_token_address:
                String address = tvTokenAddress.getText().toString();
                ClickCopyUtils.copy(getContext(), address);
                break;
            case R.id.tv_put_all:
                MyMoreText.getInstance().getLastIndexForLimit(tvIntroduction, 2, dec);
                break;
        }
    }
}
