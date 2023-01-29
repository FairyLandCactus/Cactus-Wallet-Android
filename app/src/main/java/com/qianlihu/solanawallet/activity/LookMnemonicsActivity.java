package com.qianlihu.solanawallet.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.MnemonicsAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.view.CommonDecoration;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LookMnemonicsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_mnemonics)
    RecyclerView rvMnemonics;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        return R.layout.activity_look_mnemonics;
    }

    @Override
    protected void initView() {
        String title = getIntent().getStringExtra("name");
        tvTitle.setText(title);
        String wordStore = getIntent().getStringExtra("mn");
        String[] word = wordStore.split("\\s+");
        List<String> wordsList = Arrays.asList(word);
        rvMnemonics.setLayoutManager(new GridLayoutManager(this, 3));
        rvMnemonics.addItemDecoration(new CommonDecoration(this));
        MnemonicsAdapter mAdapter = new MnemonicsAdapter(wordsList);
        rvMnemonics.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.btn_verify_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.btn_verify_wallet:
                finish();
                break;
        }
    }
}