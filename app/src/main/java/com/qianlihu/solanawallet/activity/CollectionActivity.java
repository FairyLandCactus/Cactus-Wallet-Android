package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.interfere.IFingerprint;
import com.qianlihu.solanawallet.interfere.IPassword;
import com.qianlihu.solanawallet.sql.WalletManager;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.EnterPayPwd;
import com.qianlihu.solanawallet.utils.FingerprintPwd;
import com.qianlihu.solanawallet.utils.QRCodeUtil;
import com.qianlihu.solanawallet.utils.ShareUtil;
import com.qianlihu.solanawallet.utils.TokenIconUtils;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class CollectionActivity extends BaseActivity {

    @BindView(R.id.ll_securlty)
    LinearLayout llSecurlty;
    @BindView(R.id.iv_logo)
    CircleImageView ivLogo;
    @BindView(R.id.iv_qr)
    ImageView ivQr;
    @BindView(R.id.tv_title_collection)
    TextView tvTitleCollection;
    @BindView(R.id.tv_collection_address)
    TextView tvCollectionAddress;
    @BindView(R.id.tv_collect_dec)
    TextView tvCollectDec;
    @BindView(R.id.ll_collection_qr)
    LinearLayout llCollectionQr;

    private String mPuk = "";
    private String mPwd = "";
    private String mWalletType = "0";

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_collection;
    }

    @Override
    protected void initView() {
        mPuk = getIntent().getStringExtra(Constant.SOL_PUK);
        String chain = getIntent().getStringExtra(Constant.MAIN_CHAIN);
        String chainStr = "";
        if (chain.equals(Constant.SOL_CHAIN)) {
            chainStr = Constant.SOL_CHAIN;
        } else if (chain.equals(Constant.BNB_CHAIN)) {
            chainStr = Constant.BNB_CHAIN;
        } else if (chain.equals(Constant.ETH_CHAIN)) {
            chainStr = Constant.ETH_CHAIN;
        }
        tvCollectDec.setText(String.format(getString(R.string.note), chainStr));
        WalletBean bean = WalletManager.getInstance().getWalletInfo(mPuk);
        if (bean != null) {
            mPwd = bean.getPassword();
            mWalletType = bean.getWalletType();
        }
        AddTokenDB db = (AddTokenDB) getIntent().getSerializableExtra("addTokenDB");
        String name = db.getSymbol();
        String logoUrl = db.getLogoURI();
        tvTitleCollection.setText(name + getString(R.string.collection));
        Glide.with(this).load(logoUrl).error(TokenIconUtils.icon(name)).fitCenter().into(ivLogo);
        String password = CacheData.shared().collectionPwd;
        if (TextUtils.isEmpty(password)) {
            llSecurlty.setVisibility(View.VISIBLE);
        } else {
            llCollectionQr.setVisibility(View.VISIBLE);
            collectionAddress();
        }
    }

    @Override
    protected void initData() {

    }

    private String[] writePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    @OnClick({R.id.iv_back, R.id.btn_confirm, R.id.rl_copy, R.id.rl_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                FingerprintPwd.fingerprintVerify(this, new IFingerprint() {
                    @Override
                    public void onVerify(boolean isVerify) {
                        if (isVerify) {
                            CacheData.shared().collectionPwd = "";
                            llSecurlty.setVisibility(View.GONE);
                            llCollectionQr.setVisibility(View.VISIBLE);
                            collectionAddress();
                        } else {
                            EnterPayPwd.password(CollectionActivity.this, 1, new IPassword() {
                                @Override
                                public void onPassword(String pwd) {
                                    if (pwd.equals(mPwd)) {
                                        CacheData.shared().collectionPwd = pwd;
                                        llSecurlty.setVisibility(View.GONE);
                                        llCollectionQr.setVisibility(View.VISIBLE);
                                        collectionAddress();
                                    } else {
                                        showInfo(getString(R.string.pwd_error));
                                    }
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                        }
                    }

                    @Override
                    public void onClose() {

                    }
                });

                break;
            case R.id.rl_copy:
                String address = tvCollectionAddress.getText().toString().trim();
                ClickCopyUtils.copy(this, address);
                break;
            case R.id.rl_share:
                share();
                break;
        }
    }

    private void collectionAddress() {
        Bitmap bitmap = QRCodeUtil.createQRImage(mPuk, 360, 360, null, null);
        ivQr.setImageBitmap(bitmap);
        tvCollectionAddress.setText(mPuk);
//        CacheData.shared().updateWalletInfoFlag = 66;
        if (mWalletType.equals("0")) {
            CacheData.shared().applyFlag = 67;
        } else {
            CacheData.shared().saveFlag = 67;
        }
    }

    private void share() {
        if (lacksPermission(writePermissions)) {
            ActivityCompat.requestPermissions(this, writePermissions, 0);
            return;
        }
        ShareUtil.shareImage2(this, llCollectionQr, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (lacksPermission(writePermissions)) {
                finish();
            }
        }
    }
}