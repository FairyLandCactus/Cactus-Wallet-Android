package com.qianlihu.solanawallet.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.HelpCenterAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HelpCenterActivity extends BaseActivity {

    @BindView(R.id.rv_center)
    RecyclerView rvCenter;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help_center;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.help_center));
    }

    @Override
    protected void initData() {
        List<String> list = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            list.add(i + "");
        }
        rvCenter.setLayoutManager(new LinearLayoutManager(this));
//        rvCenter.setAdapter(new HelpCenterAdapter(list));
        rvCenter.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_data))));
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}