package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.QRCodeUtil;
import com.qianlihu.solanawallet.utils.ShareUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteActivity extends BaseActivity {

    @BindView(R.id.iv_qr)
    ImageView ivQr;
    @BindView(R.id.ll_share_qr)
    LinearLayout llShareQr;
    @BindView(R.id.ll_share_bg)
    LinearLayout llShareBg;
    @BindView(R.id.tv_copy_url)
    TextView tvCopyUrl;
    @BindView(R.id.tv_android_id)
    TextView tvAndroidId;

    private String downloaAppdUrl = "http://download.ius.finance/download/app_download.html";
    private String androidId = "";

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite;
    }

    @Override
    protected void initView() {
        androidId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String qrUrl = downloaAppdUrl + "?#androidId="+androidId;
        Bitmap bitmap = QRCodeUtil.createQRImage(qrUrl, 320, 320, null, null);
        ivQr.setImageBitmap(bitmap);
        tvAndroidId.setText(getString(R.string.moblie_id)+androidId);
    }

    @Override
    protected void initData() {

    }

    private String[] writePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @OnClick({R.id.iv_back, R.id.btn_share, R.id.tv_copy_url, R.id.tv_android_id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_share:
                share();
                break;
            case R.id.tv_copy_url:
                ClickCopyUtils.copy(this, downloaAppdUrl);
                break;
            case R.id.tv_android_id:
                ClickCopyUtils.copy(this, androidId);
                break;
        }
    }

    private void share() {
        if (lacksPermission(writePermissions)) {
            ActivityCompat.requestPermissions(this, writePermissions, 0);
            return;
        }
        ShareUtil.shareImage2(this, llShareBg, 1);
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