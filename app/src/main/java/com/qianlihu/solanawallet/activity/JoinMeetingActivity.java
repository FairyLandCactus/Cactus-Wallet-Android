package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.mvp.join_room.JoinRoomPresenter;
import com.qianlihu.solanawallet.mvp.join_room.JoinRoomView;
import com.qianlihu.solanawallet.utils.DialogUtils;
import com.qianlihu.solanawallet.utils.FileUtils;
import com.qianlihu.solanawallet.utils.PictureSelectedUtil;
import com.qianlihu.solanawallet.wallet.IPSelectWallet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class JoinMeetingActivity extends BaseActivity<JoinRoomPresenter> implements JoinRoomView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_no)
    EditText etNo;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.switch_mike)
    Switch switchMike;
    @BindView(R.id.switch_speaker)
    Switch switchSpeaker;
    @BindView(R.id.switch_video)
    Switch switchVideo;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.tv_wallet)
    TextView tvWallet;

    private WalletBean walletBean = null;
    private String mHeaderPath = "";

    @Override
    public JoinRoomPresenter createPresenter() {
        return new JoinRoomPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_join_meeting;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.join_meeting));
        if (mIsdark) {
            rlBg.setBackgroundColor(getColor(R.color.bg_232323));
        }
        int flag = getIntent().getIntExtra("flag", 0);
        if (flag == 1) {
            String roomNo = getIntent().getStringExtra("roomNo");
            etNo.setText(roomNo);
        }
    }

    @Override
    protected void initData() {
        setDeviceStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDeviceStatus();
    }

    private String[] writePermissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    @OnClick({R.id.iv_back, R.id.iv_header, R.id.btn_join_meeting, R.id.tv_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_header:
                selectPic();
                break;
            case R.id.btn_join_meeting:
                if (lacksPermission(writePermissions)) {
                    requestPermissions(writePermissions, 0);
                    return;
                }
                joinMeeting();
                break;
            case R.id.tv_wallet:
                selectMyWallet();
                break;
        }
    }

    private void setDeviceStatus() {
        boolean isMike = SPUtils.getInstance().getBoolean(Constant.IS_OPEN_MIKE);
        switchMike.setChecked(isMike);
        switchMike.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchMike.setChecked(isChecked);
            SPUtils.getInstance().put(Constant.IS_OPEN_MIKE, isChecked);
        });

        boolean isSpeaker = SPUtils.getInstance().getBoolean(Constant.IS_OPEN_SPEAKER);
        switchSpeaker.setChecked(isSpeaker);
        switchSpeaker.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchSpeaker.setChecked(isChecked);
            SPUtils.getInstance().put(Constant.IS_OPEN_SPEAKER, isChecked);
        });

        boolean isVideo = SPUtils.getInstance().getBoolean(Constant.IS_OPEN_VIDEO);
        switchVideo.setChecked(isVideo);
        switchVideo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            switchVideo.setChecked(isChecked);
            SPUtils.getInstance().put(Constant.IS_OPEN_VIDEO, isChecked);
        });
    }

    private void joinMeeting() {
        String roomNumber = etNo.getText().toString();
        String name = etName.getText().toString();
        if (roomNumber.isEmpty()) {
            showInfo(getString(R.string.hint_meeting_no));
            return;
        }

        if (name.isEmpty()) {
            showInfo(getString(R.string.hint_meeting_name));
            return;
        }

        if (walletBean == null) {
            showInfo(getString(R.string.select_wallet));
            return;
        }
        boolean isVideo = SPUtils.getInstance().getBoolean(Constant.IS_OPEN_VIDEO);
        int live = isVideo ? 1 : 2;
        presenter.joinRoom(this,languageType, roomNumber,walletBean.getPubKey(), name, mHeaderPath, live);
    }

    //选择我的钱包
    private void selectMyWallet() {
        DialogUtils.selectMyWallet(this, "0", new IPSelectWallet() {
            @Override
            public void onSelectWallet(WalletBean bean) {
                walletBean = bean;
                tvWallet.setText(bean.getPubKey());
            }

            @Override
            public void onDismiss() {
            }
        });
    }

    private void selectPic() {
        PictureSelectedUtil.getInstance().picSelector(this, PictureSelectedUtil.HOME_MODE, true, new PictureSelectedUtil.IPathCallback() {
            @Override
            public void onResult(String path) {
                if (path.contains("content://")) {
                    Uri uri = Uri.parse(path);
                    path = FileUtils.getFilePathByUri_BELOWAPI11(uri, JoinMeetingActivity.this);
                }
                Log.i("duan==path", "图片路径==  " + path);
                Glide.with(JoinMeetingActivity.this).load(path).fitCenter().error(R.mipmap.icon_solana).into(ivHeader);
                mHeaderPath = path;
            }

            @Override
            public void OnCancel() {

            }
        });
    }

    @Override
    public void joinRoomCallback(String roomNo, String address, String userName) {
        CacheData.shared().roomNo = roomNo;
        CacheData.shared().userName = userName;
        CacheData.shared().userHeader = mHeaderPath;
        CacheData.shared().walletBean = walletBean;
        Intent intent = new Intent(this, MeetingRoomActivity.class);
        intent.putExtra("roomNo", roomNo);
        intent.putExtra("userName", userName);
        intent.putExtra("headerPath", mHeaderPath);
        intent.putExtra("userType", 2);
        Bundle bundle = new Bundle();
        bundle.putSerializable("wallet", walletBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (lacksPermission(writePermissions)) {
                dialogTip(getString(R.string.prem_tip));
            }
        }
    }
}