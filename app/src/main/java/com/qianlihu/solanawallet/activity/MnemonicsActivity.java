package com.qianlihu.solanawallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.MnemonicsAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.view.CommonDecoration;
import com.qianlihu.solanawallet.wallet.CreateMnemoics;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MnemonicsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_change_mnemonics_bit)
    TextView tvChangeMnemoicsBit;
    @BindView(R.id.rv_mnemonics)
    RecyclerView rvMnemonics;

    private int mBitType = 16;
    private MnemonicsAdapter mAdapter;
    private List<String> mnList;
    private String mWalletType = "0";
    private String mainChain = "";

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        return R.layout.activity_mnemonics;
    }

    @Override
    protected void initView() {
        tvTitle.setText("");
        mnList = new ArrayList<>();
    }

    @Override
    protected void initData() {
        mWalletType = getIntent().getStringExtra("walletType");
        mainChain = getIntent().getStringExtra(Constant.MAIN_CHAIN);
        rvMnemonics.setLayoutManager(new GridLayoutManager(this, 3));
        rvMnemonics.addItemDecoration(new CommonDecoration(this));
        mnemonics();
        screenTip();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CacheData.shared().applyFlag == 67 || CacheData.shared().saveFlag == 67) {
            finish();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_change_mnemonics_bit, R.id.tv_change_group, R.id.btn_verify_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_change_mnemonics_bit:
                mBitType = mBitType == 16 ? 32 : 16;
                mnemonics();
                break;
            case R.id.tv_change_group:
                mnemonics();
                break;
            case R.id.btn_verify_wallet:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("mnemonics1", (ArrayList<String>) mnList);
                bundle.putStringArrayList("mnemonics2", (ArrayList<String>) mnList);
                intent.putExtras(bundle);
                intent.putExtra("walletType", mWalletType);
                intent.putExtra(Constant.MAIN_CHAIN, mainChain);
                intent.setClass(this, VerifyMnemonicsActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void mnemonics() {
        if (mnList.size() > 0) {
            mnList.clear();
        }
        mnList.addAll(CreateMnemoics.generateMnemonics(mBitType));
        mAdapter = new MnemonicsAdapter(mnList);
        rvMnemonics.setAdapter(mAdapter);
        if (mnList.size() == 12) {
            tvChangeMnemoicsBit.setText(getString(R.string.toggle_24));
        } else {
            tvChangeMnemoicsBit.setText(getString(R.string.toggle_12));
        }
    }

    private void screenTip() {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_mnemoics, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        Button btnGotIt = view.findViewById(R.id.btn_got_it);
        btnGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }
}