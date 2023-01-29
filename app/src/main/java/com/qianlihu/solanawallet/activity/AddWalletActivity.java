package com.qianlihu.solanawallet.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.WalletTypeAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class AddWalletActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rl_import_wallet)
    RelativeLayout rlImportWallet;

    private int[] mPic = {R.mipmap.transfer_accounts_sol, R.mipmap.icon_binance_logo, R.mipmap.icon_eth, R.mipmap.icon_bitcoin};
    private String[] mTypeName = {Constant.SOL_CHAIN, Constant.BNB_CHAIN, Constant.ETH_CHAIN, Constant.BTC_CHAIN};
    private String[] mTypeDec = {"Solana", "Binance", "Eth", "Btc"};

    private String mWalletType = "0";

    //    private int[] mPic = {R.mipmap.transfer_accounts_sol, R.mipmap.transfer_accounts_ius,R.mipmap.transfer_accounts_iusa,R.mipmap.transfer_accounts_iusb,R.mipmap.transfer_accounts_iusc,R.mipmap.transfer_accounts_iusd,R.mipmap.transfer_accounts_iuse,R.mipmap.transfer_accounts_iusf};
    //    private String[] mTypeName = {"SOL","IUS","IUSA","IUSB","IUSC","IUSD", "IUSE", "IUSF"};
    //    private String[] mTypeDec = {"Solana","Yuan universe","Yuan universe","Yuan universe","Yuan universe","Yuan universe","Yuan universe","Yuan universe"};
    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_wallet;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.add_wallet));
        mWalletType = getIntent().getStringExtra("walletType");
        if (mWalletType.equals("1")) {
            rlImportWallet.setVisibility(View.GONE);
        }
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CacheData.shared().applyFlag == 67 || CacheData.shared().saveFlag == 67) {
            finish();
        }
    }

    @OnClick({R.id.iv_back, R.id.rl_create_wallet, R.id.rl_import_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_create_wallet:
                selectChain(0);
                break;
            case R.id.rl_import_wallet:
                selectChain(1);
                break;
        }
    }

    //选择钱包类型
    private void selectWalletType(int flag) {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_wallet_type, null);
        BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvApply = view.findViewById(R.id.tv_apply_wallet);
        TextView tvStorage = view.findViewById(R.id.tv_storage_wallet);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnSure = view.findViewById(R.id.btn_sure);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        ImageView ivChoose1 = view.findViewById(R.id.iv_choose_1);
        ImageView ivChoose2 = view.findViewById(R.id.iv_choose_2);
        RelativeLayout rlApply = view.findViewById(R.id.rl_apply_wallet);
        RelativeLayout rlStorage = view.findViewById(R.id.rl_storage_wallet);
        if (flag == 1) {
            tvTitle.setText(getString(R.string.select_wallet_type));
        }

        if (mWalletType.equals("1")) {
            tvTitle.setText(getString(R.string.add_wallet));
            rlApply.setVisibility(View.GONE);
            ivChoose1.setVisibility(View.GONE);
            ivChoose2.setVisibility(View.VISIBLE);
        }

        rlApply.setOnClickListener(v -> {
            ivChoose1.setVisibility(View.VISIBLE);
            ivChoose2.setVisibility(View.GONE);
        });

        rlStorage.setOnClickListener(v -> {
            ivChoose1.setVisibility(View.GONE);
            ivChoose2.setVisibility(View.VISIBLE);
        });

        btnCancel.setOnClickListener(v -> sheetDialog.dismiss());

        tvConfirm.setOnClickListener(v -> {
            sheetDialog.dismiss();
            selectChain(flag);
        });

        sheetDialog.setContentView(view);
        sheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        sheetDialog.show();
    }

    //选择钱包主链
    private String mainChain = "";
    private void selectChain(int flag) {
        String PIC = "pic";
        String NAME = "name";
        String DEC = "dec";
        String SELECT = "select";
        mainChain = "";
        List<Map<String, Object>> mapList = new ArrayList<>();
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_create_wallet, null);
        BottomSheetDialog sheetDialog = new BottomSheetDialog(this);
        RecyclerView rvWalletType = view.findViewById(R.id.rv_wallet_type);
        Button btnNextStept = view.findViewById(R.id.btn_next_stept);

        rvWalletType.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i < mPic.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(PIC, mPic[i]);
            map.put(NAME, mTypeName[i]);
            map.put(DEC, mTypeDec[i]);
            map.put(SELECT, false);
            mapList.add(map);
        }
        WalletTypeAdapter typeAdapter = new WalletTypeAdapter(mapList);
        rvWalletType.setAdapter(typeAdapter);
        mainChain = (String) mapList.get(0).get(NAME);
        typeAdapter.setOnItemChildClickListener((adapter, view1, position) -> {
            if (view1.getId() == R.id.iv_select) {
                typeAdapter.selectItem(position);
                mainChain = (String) mapList.get(position).get("name");
//                boolean isSelect = (boolean) mapList.get(position).get(SELECT);
//                isSelect = isSelect ? false : true;
//                Map<String, Object> map = mapList.get(position);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    map.replace(SELECT, isSelect);
//                }
//                mapList.set(position, map);
//                typeAdapter.notifyDataSetChanged();
            }
        });

        btnNextStept.setOnClickListener(v -> {
            sheetDialog.dismiss();
            if (mainChain.equals(Constant.BTC_CHAIN)) {
                showInfo(getString(R.string.not_yet_open));
                return;
            }
            Intent intent = new Intent();
            intent.putExtra("walletType", mWalletType);
            intent.putExtra(Constant.MAIN_CHAIN, mainChain);
            if (flag == 0) {
                intent.setClass(AddWalletActivity.this, MnemonicsActivity.class);
            } else {
                intent.setClass(AddWalletActivity.this, ImportExistingWalletActivity.class);
            }
            startActivity(intent);

        });

        sheetDialog.setContentView(view);
        sheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        sheetDialog.show();
    }
}