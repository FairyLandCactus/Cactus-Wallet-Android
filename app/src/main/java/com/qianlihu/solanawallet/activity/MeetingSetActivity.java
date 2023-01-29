package com.qianlihu.solanawallet.activity;

import android.os.Bundle;
import android.view.View;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class MeetingSetActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_member_join_mute)
    TextView tvMemberJoinMute;
    @BindView(R.id.switch_mike)
    Switch switchMike;
    @BindView(R.id.tv_recording_permissions)
    TextView tvRecordingPermissions;
    @BindView(R.id.tv_mine_name)
    TextView tvMineName;
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.switch_window)
    Switch switchWindow;
    @BindView(R.id.switch_open_subtitle)
    Switch switchOpenSubtitle;
    @BindView(R.id.switch_voice_mode)
    Switch switchVoiceMode;
    @BindView(R.id.switch_video_mirroring)
    Switch switchVideoMirroring;
    @BindView(R.id.switch_show_duration)
    Switch switchShowDuration;
    @BindView(R.id.switch_show_bullet_chat)
    Switch switchShowBulletChat;
    @BindView(R.id.switch_voice_stimulation)
    Switch switchVoiceStimulation;
    @BindView(R.id.switch_noise_reduction)
    Switch switchNoiseReduction;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting_set;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.more_set));
        if (mIsdark) {
            rlBg.setBackgroundColor(getColor(R.color.bg_232323));
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.tv_member_join_mute, R.id.tv_recording_permissions, R.id.tv_mine_name, R.id.iv_header})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_member_join_mute:
                break;
            case R.id.tv_recording_permissions:
                break;
            case R.id.tv_mine_name:
                break;
            case R.id.iv_header:
                break;
        }
    }
}