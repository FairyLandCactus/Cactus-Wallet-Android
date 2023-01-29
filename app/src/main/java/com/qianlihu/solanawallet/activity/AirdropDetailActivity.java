package com.qianlihu.solanawallet.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AirdropDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_ius)
    TextView tvIus;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_condition)
    TextView tvCondition;
    @BindView(R.id.tv_rules)
    TextView tvRules;
    @BindView(R.id.tv_introduction)
    TextView tvIntroduction;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_airdrop_detail;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.claim_airdrop));
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.btn_right_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_right_now:
                break;
        }
    }
}