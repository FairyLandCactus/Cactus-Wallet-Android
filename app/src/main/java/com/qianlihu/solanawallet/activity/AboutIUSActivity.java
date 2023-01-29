package com.qianlihu.solanawallet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AppUtils;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutIUSActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_version_code)
    TextView tvVersionCode;
    @BindView(R.id.tv_website)
    TextView tvWebsite;
    @BindView(R.id.tv_email)
    TextView tvEmail;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about_i_u_s;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.about_ius));
        tvVersionCode.setText("V"+ AppUtils.getAppVersionName());
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.ll_website, R.id.tv_user_agreement, R.id.tv_privacy_agreement, R.id.tv_version})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_website:
                break;
            case R.id.tv_user_agreement:
                break;
            case R.id.tv_privacy_agreement:
                break;
            case R.id.tv_version:
                break;
        }
    }
}