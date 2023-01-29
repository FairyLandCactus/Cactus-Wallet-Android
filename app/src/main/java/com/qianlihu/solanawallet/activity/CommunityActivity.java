package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.CommunityAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.meeting.MeetingTypeBean;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;
import com.qianlihu.solanawallet.wc.C;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.king.zxing.CaptureActivity.KEY_RESULT;

public class CommunityActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_scan)
    ImageView ivScan;
    @BindView(R.id.rv_meeting)
    RecyclerView rvMeeting;

    private int mPosition = 0;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_community;
    }

    @Override
    protected void initView() {

        tvTitle.setText(getString(R.string.my_community));

        ivScan.setVisibility(View.VISIBLE);

        List<MeetingTypeBean> beanList = new ArrayList<>();

        MeetingTypeBean bean = new MeetingTypeBean();
        bean.setTitle(getString(R.string.creat_meeting));
        bean.setDec(getString(R.string.creat_meeting_dec));
        beanList.add(bean);

        MeetingTypeBean bean2 = new MeetingTypeBean();
        bean2.setTitle(getString(R.string.join_meeting));
        bean2.setDec(getString(R.string.join_meeting_dec));
        beanList.add(bean2);

        rvMeeting.setLayoutManager(new LinearLayoutManager(this));
        rvMeeting.addItemDecoration(new GridSpaceItemDecoration(1, 60, 0));
        CommunityAdapter communityAdapter = new CommunityAdapter(beanList);
        rvMeeting.setAdapter(communityAdapter);
        communityAdapter.setOnItemClickListener((adapter, view, position) -> {
            communityAdapter.select(position);
            mPosition = position;
        });

    }

    @Override
    protected void initData() {

    }

    private String[] writePermissions = new String[]{Manifest.permission.CAMERA};

    @OnClick({R.id.iv_back, R.id.iv_scan, R.id.btn_confirm})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_scan:
                if (lacksPermission(writePermissions)) {
                    requestPermissions(writePermissions, 0);
                    return;
                }
                intent.setClass(this, QRActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.btn_confirm:
                if (mPosition == 0) {
                    intent.setClass(CommunityActivity.this, CreatMeetingActivity.class);
                } else if (mPosition == 1) {
                    intent.setClass(CommunityActivity.this, JoinMeetingActivity.class);
                }
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                String result = data.getStringExtra(KEY_RESULT);
                if (result.contains("join_meeting#")) {
                    String str1 = result.substring(0, result.indexOf("#"));
                    String str2 = result.substring(str1.length() + 1);
                    Intent intent = new Intent(this, JoinMeetingActivity.class);
                    intent.putExtra("roomNo", str2);
                    intent.putExtra("flag", 1);
                    startActivity(intent);
                    return;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (lacksPermission(writePermissions)) {
                return;
            }
        }
    }
}