package com.qianlihu.solanawallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.BaseTabAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.event.ErrorEvent;
import com.qianlihu.solanawallet.fragment.transfer_record.AssetIntroductionFragment;
import com.qianlihu.solanawallet.fragment.transfer_record.ToolFragment;
import com.qianlihu.solanawallet.fragment.transfer_record.TransferRecordFragment;
import com.qianlihu.solanawallet.fragment.transfer_record.Web3Fragment;
import com.qianlihu.solanawallet.rpc.RpcException;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.MagicTitleUtils;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.SolanaUtil;
import com.qianlihu.solanawallet.utils.TokenIconUtils;
import com.qianlihu.solanawallet.view.NoScrollViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class TransactionRecordActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_logo)
    CircleImageView ivLogo;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_usdt)
    TextView tvUsdt;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.vp_transfer)
    NoScrollViewPager vpTransfer;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_currercy)
    ImageView ivCurrercy;

    private double mUsdt = 0;
    private String mPuk = "";
    private double amount = 0;
    private String name = "";
    private String mMint = "";
    private String mWalletType = "0";
    private boolean isConnect = false;
    private String logoUrl = "";
    private AddTokenDB db = null;
    private String mMainChain = "";
    private String mPik = "";
    private String mTokenAccount = "";
    private double mUsdTotal = 0.0;
    private double musd = 0.0;

    private boolean isTransfer = false;

    private int decimals = 0;

    private List<String> mDataList = new ArrayList<>();

    private String[] myPulishAllToken = {
            Constant.SOL_TOKEN_ADDRESS,
            "0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c",
            "0x55d398326f99059ff775485246999027b3197955",
            "0x2170Ed0880ac9A755fd29B2688956BD959F933F8",
            "AoqPs243Hh5LrzVmRamz9pEyiemzTBi5N1b4uiVsfrgU",
            "3yVqA5Grz3F4cjm3hNPLGLUeYV9nNdK5m7nAyYG7hu6d",
            "Es9vMFrzaCERmJfrF4H2FYD4KCoNkY11McCe8BenwNYB",
            "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v",
            "So11111111111111111111111111111111111111112",
            "AL1KoU6BLTuGM6hKgdezDqqmgM84yALFFF4oc3sZiWwT",
            "4UWQ9oJduGa1z6dKHduqsUAtEfv5kBcCv7CuhgFoKjrm"};

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transaction_record;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mWalletType = getIntent().getStringExtra(Constant.WALLET_TYPE);
        isConnect = SPUtils.getInstance().getBoolean(Constant.SOL_IS_NET);
        mPuk = getIntent().getStringExtra(Constant.SOL_PUK);
        mMainChain = getIntent().getStringExtra(Constant.MAIN_CHAIN);
        db = (AddTokenDB) getIntent().getSerializableExtra("addTokenDB");
        mMint = db.getTokenAddress();
//        if (!Arrays.asList(myPulishAllToken).contains(mMint)) {
//            dialogTip2(getString(R.string.Risk_statement));
//        }
        updateData();
    }

    @Override
    protected void initData() {
        mDataList.add(getString(R.string.transaction_record));
        mDataList.add(getString(R.string.asset_introduction));
        mDataList.add(getString(R.string.tool));
        mDataList.add("Web3");
//        mDataList.add(getString(R.string.QUOTATION));
        boolean isDark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        int tabTextColor = R.color.white;
        if (!isDark) {
            tabTextColor = R.color.txt_333;
        }
        MagicTitleUtils.magicTitleView(this, mDataList, magicIndicator, vpTransfer, R.color.txt_5D6A84, tabTextColor, tabTextColor, 18f, true);
        initFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CacheData.shared().addTokenDB != null) {
            db = CacheData.shared().addTokenDB;
            CacheData.shared().addTokenDB = null;
            updateData();
        }
    }

    private void updateData() {
        name = db.getSymbol();
        mUsdt = db.getUsdt();
        amount = db.getAmount();
        mMint = db.getTokenAddress();
        logoUrl = db.getLogoURI();
        decimals = db.getDecimals();
        mTokenAccount = db.getTokenAccount();
        musd = db.getUsd();
        mUsdTotal = db.getUsdTotal();
        tvCopy.setText(mPuk);
        Glide.with(this).load(logoUrl).fitCenter().error(TokenIconUtils.icon(name)).into(ivLogo);
        tvTitle.setText(name);
        tvMoney.setText(MyUtils.decimalFormat(amount));
        tvUsdt.setText(String.format("$%s", MyUtils.decimalFormat(mUsdTotal)));
        tvName.setText(name);
        List<WalletBean> walletList = LitePal.where("pubKey = ? and walletType = ? and mainChain = ?", mPuk, mWalletType, mMainChain).find(WalletBean.class);
        if (walletList.size() > 0) {
            mPik = walletList.get(0).getPvaKey();
        }
        if (name.equals("WSOL")) {
            ivCurrercy.setVisibility(View.VISIBLE);
            ivCurrercy.setOnClickListener(v -> {
                String tipStr = String.format(getString(R.string.close_wsol_tip), MyUtils.decimalFormat(amount));
                dialogConfimCancel(tipStr, (dialog, clickListener) -> {
                    dialog.dismiss();
                    showLoadingInfos(getString(R.string.reminder), getString(R.string.transaction_data_processing), true);
                    new Thread(() -> {
                        if (!TextUtils.isEmpty(mPik)) {
                            try {
                                String hash = SolanaUtil.createCloseAccountInstruction(mTokenAccount, mPuk, mPuk, mPik);
                                try {
                                    Thread.sleep(10000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(() -> {
                                    if (!TextUtils.isEmpty(hash)) {
                                        hideLoadingInfos();
                                        LitePal.deleteAll(AddTokenDB.class, "walletAddress = ? and tokenAddress = ?", mPuk, mMint);
                                        CacheData.shared().applyFlag = 67;
                                        showSuccessToast(getString(R.string.close_success_wsol));
                                        finish();
                                    }
                                });
                            } catch (RpcException e) {
                                runOnUiThread(() -> hideLoadingInfos());
                                e.printStackTrace();
                            }
                        }
                    }).start();
                });
            });
        }
    }

    private void initFragment() {
        List<BaseFragment> list = new ArrayList<>();
        list.add((BaseFragment) TransferRecordFragment.newInstance(db, mPuk, mMainChain, mWalletType));
        list.add((BaseFragment) AssetIntroductionFragment.newInstance(db, mMainChain));
        list.add((BaseFragment) ToolFragment.newInstance(mMainChain));
        list.add((BaseFragment) Web3Fragment.newInstance(mMainChain));
//        list.add((BaseFragment) QuotationFragment.newInstance());

        BaseTabAdapter tabAdapter = new BaseTabAdapter(getSupportFragmentManager(), list);
        vpTransfer.setAdapter(tabAdapter);
        vpTransfer.setOffscreenPageLimit(mDataList.size());
    }

    //异常信息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ExceptionErrorInfo(ErrorEvent event) {
        hideLoadingInfos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_copy, R.id.rl_transfer, R.id.rl_receive, R.id.rl_exchange})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("addTokenDB", db);
        intent.putExtras(bundle);
        intent.putExtra(Constant.SOL_PUK, mPuk);
        switch (view.getId()) {
            case R.id.iv_back:
                if (isTransfer) {
                    setResult(RESULT_OK, intent);
                    isTransfer = false;
                }
                finish();
                break;
            case R.id.tv_copy:
                String address = tvCopy.getText().toString().trim();
                ClickCopyUtils.copy(this, address);
                break;
            case R.id.rl_transfer:
                intent.setClass(this, TransferActivity.class);
//                startActivity(intent);
                startActivityForResult(intent, 10088);
                break;
            case R.id.rl_receive:
                intent.putExtra(Constant.MAIN_CHAIN, mMainChain);
                intent.setClass(this, CollectionActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_exchange:
                CacheData.shared().isExchange = true;
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 10088) {
                isTransfer = true;
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isTransfer) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                isTransfer = false;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}