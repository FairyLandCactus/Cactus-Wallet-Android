package com.qianlihu.solanawallet.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingSafeActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.switch_lock_meeting)
    Switch switchLockMeeting;
    @BindView(R.id.switch_waiting_room)
    Switch switchWaitingRoom;
    @BindView(R.id.switch_hide_pwd)
    Switch switchHidePwd;
    @BindView(R.id.switch_initiate_sharing)
    Switch switchInitiateSharing;
    @BindView(R.id.switch_own_chat)
    Switch switchOwnChat;
    @BindView(R.id.switch_open_video)
    Switch switchOpenVideo;
    @BindView(R.id.switch_change_name)
    Switch switchChangeName;
    @BindView(R.id.switch_self_unmute)
    Switch switchSelfUnmute;
    @BindView(R.id.switch_sharing_apps)
    Switch switchSharingApps;
    @BindView(R.id.switch_comments)
    Switch switchComments;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting_safe;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.more_safe));
        if (mIsdark) {
            rlBg.setBackgroundColor(getColor(R.color.bg_232323));
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}