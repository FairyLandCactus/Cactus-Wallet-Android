package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.api.retrofit.ApiRetrofit;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.mvp.create_room.presenter.CreateRoomPresenter;
import com.qianlihu.solanawallet.mvp.create_room.view.CreateRoomView;
import com.qianlihu.solanawallet.utils.DialogUtils;
import com.qianlihu.solanawallet.utils.FileUtils;
import com.qianlihu.solanawallet.utils.PictureSelectedUtil;
import com.qianlihu.solanawallet.wallet.IPSelectWallet;

import org.bouncycastle.asn1.esf.SPuri;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jnr.ffi.annotations.In;

public class CreatMeetingActivity extends BaseActivity<CreateRoomPresenter> implements CreateRoomView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_no)
    EditText etNo;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_person)
    EditText etPerson;
    @BindView(R.id.tv_wallet)
    TextView tvWallet;
    @BindView(R.id.switch_mike)
    Switch switchMike;
    @BindView(R.id.switch_speaker)
    Switch switchSpeaker;
    @BindView(R.id.switch_video)
    Switch switchVideo;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.civ_header)
    CircleImageView civHeader;

    private WalletBean walletBean = null;
    private String mHeaderPath = "";

    @Override
    public CreateRoomPresenter createPresenter() {
        return new CreateRoomPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_creat_meeting;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.creat_meeting));
        if (mIsdark) {
            rlBg.setBackgroundColor(getColor(R.color.bg_232323));
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
    @OnClick({R.id.iv_back, R.id.tv_wallet, R.id.btn_creat_meeting, R.id.civ_header})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_wallet:
                selectMyWallet();
                break;
            case R.id.btn_creat_meeting:
                if (lacksPermission(writePermissions)) {
                    requestPermissions(writePermissions, 0);
                    return;
                }
                creatMeeting();
                break;
            case R.id.civ_header:
                selectPic();
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

    private void selectPic() {
        PictureSelectedUtil.getInstance().picSelector(this, PictureSelectedUtil.HOME_MODE, true, new PictureSelectedUtil.IPathCallback() {
            @Override
            public void onResult(String path) {
                if (path.contains("content://")) {
                    Uri uri = Uri.parse(path);
                    path = FileUtils.getFilePathByUri_BELOWAPI11(uri, CreatMeetingActivity.this);
                }
                Log.i("duan==path", "图片路径==  " + path);
                mHeaderPath = path;
                Glide.with(CreatMeetingActivity.this).load(path).fitCenter().error(R.mipmap.icon_solana).into(civHeader);
            }

            @Override
            public void OnCancel() {

            }
        });
    }

    private void creatMeeting() {
        String roomNumber = etNo.getText().toString();
        String roomName = etName.getText().toString();
        String personalNum = etPerson.getText().toString();
        String userName = etUserName.getText().toString();
        String tvSelectAddress = tvWallet.getText().toString();
        if (roomNumber.isEmpty()) {
            showInfo(getString(R.string.hint_meeting_no));
            return;
        }

        if (roomName.isEmpty()) {
            showInfo(getString(R.string.hint_meeting_name));
            return;
        }

        if (personalNum.isEmpty()) {
            showInfo(getString(R.string.hint_meeting_person));
            return;
        }

        if (userName.isEmpty()) {
            showInfo(getString(R.string.hint_user_name));
            return;
        }

        if (walletBean == null) {
            showInfo(getString(R.string.select_wallet));
            return;
        }
        int num = Integer.parseInt(personalNum);
        if (num < 2) {
            showInfo(getString(R.string.meeting_num_tip));
            return;
        }

        if (num > 300) {
            showInfo(getString(R.string.meeting_max_personal));
            return;
        }

        presenter.createRoom(this,languageType, tvSelectAddress, roomNumber, roomName, num, userName, mHeaderPath);
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

    @Override
    public void createMeetingCallback(String address, String roomNo, String roomName, String userName, int num) {
        CacheData.shared().roomNo = roomNo;
        CacheData.shared().userName = userName;
        CacheData.shared().userHeader = mHeaderPath;
        CacheData.shared().walletBean = walletBean;
        Intent intent = new Intent(this, MeetingRoomActivity.class);
        intent.putExtra("roomNo", roomNo);
        intent.putExtra("roomName", roomName);
        intent.putExtra("personalNum", num);
        intent.putExtra("userName", userName);
        intent.putExtra("headerPath", mHeaderPath);
        intent.putExtra("userType", 1);
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