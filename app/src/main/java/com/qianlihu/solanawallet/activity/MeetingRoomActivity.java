package com.qianlihu.solanawallet.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lzf.easyfloat.permission.PermissionUtils;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.MeetingChatAdapter;
import com.qianlihu.solanawallet.adapter.MemberAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.meeting.utils.GenerateUserSig;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.ChatBean;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.MeetingRoomUserBean;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.MemberBean;
import com.qianlihu.solanawallet.mvp.meeting_room.presenter.MeetingRoomPresenter;
import com.qianlihu.solanawallet.mvp.meeting_room.view.MeetingRoomView;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.PlatformUtils;
import com.qianlihu.solanawallet.utils.ShareUtil;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;
import com.qianlihu.solanawallet.view.VideoFloatWindView;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.liteav.device.TXDeviceManager;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MeetingRoomActivity extends BaseActivity<MeetingRoomPresenter> implements MeetingRoomView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_moderator)
    TextView tvModerator;
    @BindView(R.id.rv_audience)
    RecyclerView rvAudience;
    @BindView(R.id.iv_mute)
    ImageView ivMute;
    @BindView(R.id.iv_video)
    ImageView ivVideo;
    @BindView(R.id.ll_member_manager)
    LinearLayout llMemberManager;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.txcvv_host)
    TXCloudVideoView txcvvHost;
    @BindView(R.id.iv_scan)
    ImageView ivCamera;
    @BindView(R.id.iv_currercy)
    ImageView ivSoundMode;
    @BindView(R.id.tv_member)
    TextView tvMember;
    @BindView(R.id.rv_chat)
    RecyclerView rvChat;
    @BindView(R.id.ll_input)
    LinearLayout llInput;
    @BindView(R.id.et_chat_content)
    EditText etChatContent;
    @BindView(R.id.ll_et_chat)
    LinearLayout llEtChat;

    private MemberAdapter memberAdapter;
    private int mPersonNum = 0;
    private String mRoomNo = "";
    private String mRoomName = "";
    private String mUserName = "";
    private String mUserId = "";
    private int mUserType = 1;
    private String mHeaderPath = "";
    private String mHostHeaderPath = "";
    private WalletBean walletBean = null;
    private boolean isOpenMike = false;
    private boolean isOpenSpeaker = false;
    private boolean isOpenVideo = false;
    private boolean mIsFrontCamera = true;
    private String mStartTime = "";
    private String mAuth = "";

    private TRTCCloud mTRTCCloud;
    private TXDeviceManager mTXDeviceManager;
    private List<MemberBean> memberBeanList;
    private List<ChatBean> chatBeanList;
    private MeetingChatAdapter chatAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, MeetingRoomActivity.class);
        String roomNo = CacheData.shared().roomNo;
        String userName = CacheData.shared().userName;
        String userHeader = CacheData.shared().userHeader;
        WalletBean walletBean = CacheData.shared().walletBean;
        starter.putExtra("roomNo", roomNo);
        starter.putExtra("userName", userName);
        starter.putExtra("headerPath", userHeader);
        starter.putExtra("userType", 2);
        Bundle bundle = new Bundle();
        bundle.putSerializable("wallet", walletBean);
        starter.putExtras(bundle);
        starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(starter);
    }

    @Override
    public MeetingRoomPresenter createPresenter() {
        return new MeetingRoomPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting_room;
    }

    @Override
    protected void initView() {
        memberBeanList = new ArrayList<>();
        chatBeanList = new ArrayList<>();
        tvTitle.setText(String.format(getString(R.string.meeting_room), (memberBeanList.size() + 1) + ""));
        rvAudience.setLayoutManager(new GridLayoutManager(this, 3));
        rvAudience.addItemDecoration(new GridSpaceItemDecoration(3, 40, 100));
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.addItemDecoration(new GridSpaceItemDecoration(1, 20, 0));

        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    @Override
    protected void initData() {
        mRoomNo = getIntent().getStringExtra("roomNo");
        mUserName = getIntent().getStringExtra("userName");
        mHeaderPath = getIntent().getStringExtra("headerPath");
        mUserType = getIntent().getIntExtra("userType", 1);
        walletBean = (WalletBean) getIntent().getSerializableExtra("wallet");

        if (mUserType == 1) {
            mPersonNum = getIntent().getIntExtra("personalNum", 0);
            mRoomName = getIntent().getStringExtra("roomName");
        }
        isOpenMike = SPUtils.getInstance().getBoolean(Constant.IS_OPEN_MIKE);
        isOpenVideo = SPUtils.getInstance().getBoolean(Constant.IS_OPEN_VIDEO);
        if (isOpenVideo) {
            txcvvHost.setVisibility(View.VISIBLE);
            ivCamera.setVisibility(View.VISIBLE);
        }
        if (mIsdark) {
            ivCamera.setImageDrawable(getDrawable(R.mipmap.icon_camera_dark));
        } else {
            ivCamera.setImageDrawable(getDrawable(R.mipmap.icon_camera_night));
        }

        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setListener(trtcCloudListener);
        mTXDeviceManager = mTRTCCloud.getDeviceManager();
        presenter.getRoomMemberList(languageType, mRoomNo);
        changeSoundMode(false);
        muteAudio(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtils.checkPermission(this)) {
            VideoFloatWindView.dismissFloatView(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(mGlobalLayoutListener);
    }

    @OnClick({R.id.iv_back,
            R.id.ll_mute,
            R.id.ll_video,
            R.id.ll_member_manager,
            R.id.ll_more,
            R.id.iv_scan,
            R.id.iv_currercy,
            R.id.iv_emoticon,
            R.id.tv_chat,
            R.id.btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                exitMeetingTip();
                break;
            case R.id.ll_mute:
                muteAudio(true);
                break;
            case R.id.ll_video:
                muteVideo(true);
                break;
            case R.id.ll_member_manager:
                Intent intent = new Intent(this, MemberManagerActivity.class);
                intent.putExtra("roomName", mRoomName);
                intent.putExtra("roomId", mRoomNo);
                intent.putExtra("hostName", mAuth);
                if (isHost()) {
                    intent.putExtra("userType", 1);
                } else {
                    intent.putExtra("userType", 2);
                }
                intent.putExtra("hostHeader", mHostHeaderPath);
                intent.putExtra("member", (Serializable) memberBeanList);
                startActivityForResult(intent, 333);
                break;
            case R.id.ll_more:
                presenter.moreDialog(this, mIsdark, mStartTime, mRoomNo, mAuth);
                break;
            case R.id.iv_scan:
                switchCamera();
                break;
            case R.id.iv_currercy:
                changeSoundMode(true);
                break;
            case R.id.iv_emoticon:
                break;
            case R.id.tv_chat:
                inputChatContent();
                break;
            case R.id.btn_send:
                sendMessage();
                break;
        }
    }

    private void inputChatContent() {
        llInput.setVisibility(View.GONE);
        llEtChat.setVisibility(View.VISIBLE);
        MyUtils.showSoftInput(this, etChatContent);
    }

    private void sendMessage() {
        String contentMsg = etChatContent.getText().toString();
        if (TextUtils.isEmpty(contentMsg)) {
            showInfo(getString(R.string.input_content_empty));
            return;
        }
        if (contentMsg.length() > 200) {
            showInfo(getString(R.string.message_overrun_tip));
            return;
        }
        mTRTCCloud.sendSEIMsg(contentMsg.getBytes(), 1);
        etChatContent.setText("");
        llInput.setVisibility(View.VISIBLE);
        llEtChat.setVisibility(View.GONE);
        MyUtils.hideKeyboard(etChatContent);
    }

    private int mWindowHeight = 0;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            //获取当前窗口实际的可见区域
            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            int height = r.height();
            if (mWindowHeight == 0) {
                //一般情况下，这是原始的窗口高度
                mWindowHeight = height;
            } else {
                if (mWindowHeight != height) {
                    //两次窗口高度相减，就是软键盘高度
                    int softKeyboardHeight = mWindowHeight - height - 180;
                    llEtChat.setPadding(0, 0, 0, softKeyboardHeight);
                } else {
                    llEtChat.setPadding(0, 0, 0, 0);
                }
            }
        }
    };

    /**
     * 点击空白处,关闭输入法软键盘
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                llInput.setVisibility(View.VISIBLE);
                llEtChat.setVisibility(View.GONE);
                MyUtils.hideKeyboard(etChatContent);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 333 && resultCode == 666) {
            Bundle bundle = getIntent().getExtras();
            List<String> list = (List<String>) bundle.getSerializable("kickoutMember");
            for (int k = 0; k < memberBeanList.size(); k++) {
                for (int i = 0; i < list.size(); i++) {
                    String userId = list.get(i);
                    if (userId.equals(memberBeanList.get(k).getUserId())) {
                        memberBeanList.remove(k);
                        notifyMemerList(k);
                        k--;
                    }
                }
            }
        }
    }

    @Override
    public void inviteTypeCheck(int position) {
        String meetingShareDec = String.format(getString(R.string.meeting_share_dec), mUserName, mRoomNo);
        if (position == 1) {
            if (PlatformUtils.isInstallApp(this, PlatformUtils.PACKAGE_WECHAT)) {
                ShareUtil.shareWeixinQQText(this, meetingShareDec, mRoomName, "Weixin");
            } else {
                showInfo(getString(R.string.not_indtall_weichat_tip));
            }
        } else if (position == 2) {
            if (PlatformUtils.isInstallApp(this, PlatformUtils.PACKAGE_MOBILE_QQ)) {
                ShareUtil.shareWeixinQQText(this, meetingShareDec, mRoomName, "QQ");
            } else {
                showInfo(getString(R.string.not_indtall_qq_tip));
            }
        } else if (position == 3) {
            ClickCopyUtils.copy(this, mRoomNo);
        } else if (position == 4) {
            Intent intent = new Intent(this, MeetingShareActivity.class);
            intent.putExtra("time", mStartTime);
            intent.putExtra("roomId", mRoomNo);
            intent.putExtra("auth", mAuth);
            startActivity(intent);
        } else if (position == 5) {
            //最小化Activity
            loadPermissions();
        }
    }

    @Override
    public void roomMemberListCallback(MeetingRoomUserBean bean) {
        if (bean.getAnchor().size() > 0) {
            mUserId = bean.getAnchor().get(0).getAddress();
            mRoomNo = bean.getAnchor().get(0).getRoom_id();
            mRoomName = bean.getAnchor().get(0).getRoom_name();
            mStartTime = bean.getAnchor().get(0).getCtime();
            mAuth = bean.getAnchor().get(0).getUser_name();
            tvUserName.setText(bean.getAnchor().get(0).getUser_name());
            hostEnterRoom();
            if (isHost()) {
                mUserType = 1;
            } else {
                mUserType = 0;
                tvMember.setText(getString(R.string.meeting_member));
            }
            if (bean.getUser().size() > 0) {
                for (int i = 0; i < bean.getUser().size(); i++) {
                    boolean isLive = bean.getUser().get(i).getLive_status() == 1 ? true : false;
                    String memberPuk = bean.getUser().get(i).getAddress();
                    int index = memberBeanList.indexOf(memberPuk);
                    if (memberBeanList.size() > 0) {
                        if (index == -1) {
                            return;
                        }
                    }
                    memberEnterRoom(memberPuk, isLive, 1);
                }
            }
            muteVideo(false);
        }
    }

    //主持人进入房间
    private void hostEnterRoom() {
        TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
        trtcParams.sdkAppId = GenerateUserSig.SDKAPPID;
        trtcParams.userId = mUserId;
        trtcParams.roomId = Integer.parseInt(mRoomNo);
        trtcParams.userSig = GenerateUserSig.genUserSig(trtcParams.userId);

        if (isHost()) {
            mTRTCCloud.startLocalPreview(isOpenVideo, txcvvHost);
            mTRTCCloud.startLocalAudio(TRTCCloudDef.TRTC_AUDIO_QUALITY_SPEECH);
            mTRTCCloud.enterRoom(trtcParams, TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);
        } else {
            mTRTCCloud.startRemoteView(mUserId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                    txcvvHost);
        }
    }

    //观众进入房间
    private void memberEnterRoom(String userId, boolean isLive, int flag) {
        MemberBean bean = new MemberBean();
        bean.setHeader(mHeaderPath);
        bean.setName(mUserName);
        bean.setUserId(userId);
        bean.setRoomId(mRoomNo);
        if (flag == 1) {
            bean.setVideo(isLive);
        } else {
            bean.setVideo(false);
        }
        bean.setWalletBean(walletBean);
        memberBeanList.add(bean);
        if (userId.equals(walletBean.getPubKey())) {
            int size = memberBeanList.size();
            if (size > 1) {
                Collections.swap(memberBeanList, 0, (size - 1));
            }
        }

        tvTitle.setText(String.format(getString(R.string.meeting_room), (memberBeanList.size() + 1) + ""));
        if (memberAdapter != null) {
            memberAdapter.notifyItemChanged(memberBeanList.size() - 1);
        } else {
            updateMemberList();
        }
    }

    //实时音视频监听事件
    private TRTCCloudListener trtcCloudListener = new TRTCCloudListener() {

        //某远端用户发布/取消了主路视频画面回调
        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            Log.i("duan==video", "userId     " + userId);
            if (userId.equals(mUserId)) {
                if (available) {
                    mTRTCCloud.startRemoteView(userId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG, txcvvHost);
                    txcvvHost.setVisibility(View.VISIBLE);
                } else {
                    txcvvHost.setVisibility(View.GONE);
                }
                return;
            }
            int index = memberBeanList.indexOf(userId);
            if (index == -1) {
                for (int i = 0; i < memberBeanList.size(); i++) {
                    if (userId.equals(memberBeanList.get(i).getUserId())) {
                        MemberBean bean = memberBeanList.get(i);
                        bean.setVideo(available);
                        memberBeanList.set(i, bean);
                        memberAdapter.notifyItemChanged(i);
                    }
                }
            }
        }

        //进入房间回调
        @Override
        public void onRemoteUserEnterRoom(String userId) {
            Log.i("duan==video", "onRemoteUserEnterRoom     " + userId);
            if (userId.equals(mUserId)) {
//                mTRTCCloud.startRemoteView(userId, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG, txcvvHost);
                return;
            }
            int index = memberBeanList.indexOf(userId);
            if (memberBeanList.size() > 0) {
                if (index == -1) {
                    return;
                }
            } else {
                if (index != -1) {
                    return;
                }
            }
            memberEnterRoom(userId, false, 2);
        }

        //离开房间回调
        @Override
        public void onRemoteUserLeaveRoom(String userId, int reason) {
            Log.i("duan==video", "onRemoteUserLeaveRoom     " + reason);
            if (userId.equals(mUserId)) {
                mTRTCCloud.stopLocalAudio();
                mTRTCCloud.stopLocalPreview();
                return;
            }
            for (int i = 0; i < memberBeanList.size(); i++) {
                if (userId.equals(memberBeanList.get(i).getUserId())) {
                    memberBeanList.remove(i);
                    notifyMemerList(i);
                }
            }
        }

        //退出房间回调
        @Override
        public void onExitRoom(int reason) {
            Log.i("duan==video", "reason     " + reason);
            if (reason == 1) {
                dialogTip2(getString(R.string.kick_out_tip));
            } else if (reason == 2) {
                dialogTip2(getString(R.string.room_disbanded_tip));
            }
        }

        //信息消息回调
        @Override
        public void onRecvSEIMsg(String userId, byte[] data) {
            super.onRecvSEIMsg(userId, data);
            try {
                String message = new String(data, "UTF-8");
                ChatBean bean = new ChatBean();
                bean.setMsg(message);
                bean.setUserName(userId);
                chatBeanList.add(bean);
                if (chatAdapter == null) {
                    chatAdapter = new MeetingChatAdapter(chatBeanList);
                    rvChat.setAdapter(chatAdapter);
                } else {
                    chatAdapter.notifyItemChanged(chatBeanList.size());
                    rvChat.smoothScrollToPosition(chatBeanList.size() - 1);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        //错误事件回调
        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            super.onError(errCode, errMsg, extraInfo);
            Log.i("duan==video", "onError     " + errMsg + "== " + errCode);
            showError(errMsg);
            if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                exitRoom();
            }
        }
    };

    //更新观众列表
    private void updateMemberList() {
        memberAdapter = new MemberAdapter(memberBeanList, mTRTCCloud, mUserType);
        rvAudience.setAdapter(memberAdapter);
    }

    //刷新观众列表
    private void notifyMemerList(int position) {
        if (memberAdapter != null) {
            tvTitle.setText(String.format(getString(R.string.meeting_room), (memberBeanList.size() + 1) + ""));
            memberAdapter.notifyItemChanged(position);
        }

    }

    //切换摄像头
    private void switchCamera() {
        mIsFrontCamera = !mIsFrontCamera;
        mTXDeviceManager.switchCamera(mIsFrontCamera);
    }

    //切换声音模式
    private void changeSoundMode(boolean isChange) {
        isOpenSpeaker = SPUtils.getInstance().getBoolean(Constant.IS_OPEN_SPEAKER);
        ivSoundMode.setVisibility(View.VISIBLE);
        if (isChange) {
            isOpenSpeaker = !isOpenSpeaker;
            SPUtils.getInstance().put(Constant.IS_OPEN_SPEAKER, isOpenSpeaker);
        }
        if (isOpenSpeaker) {
            if (mIsdark) {
                ivSoundMode.setImageDrawable(getDrawable(R.mipmap.icon_spreaker_dark));
            } else {
                ivSoundMode.setImageDrawable(getDrawable(R.mipmap.icon_speaker_night));
            }
            mTXDeviceManager.setAudioRoute(TXDeviceManager.TXAudioRoute.TXAudioRouteSpeakerphone);
        } else {
            if (mIsdark) {
                ivSoundMode.setImageDrawable(getDrawable(R.mipmap.icon_earplugs_dark));
            } else {
                ivSoundMode.setImageDrawable(getDrawable(R.mipmap.icon_earplugs_night));
            }
            mTXDeviceManager.setAudioRoute(TXDeviceManager.TXAudioRoute.TXAudioRouteEarpiece);
        }
    }

    //是否打开麦克风
    private void muteAudio(boolean isChange) {
        isOpenMike = SPUtils.getInstance().getBoolean(Constant.IS_OPEN_MIKE);
        if (isChange) {
            isOpenMike = !isOpenMike;
            SPUtils.getInstance().put(Constant.IS_OPEN_MIKE, isOpenMike);
        }
        if (isOpenMike) {
            mTRTCCloud.muteLocalAudio(false);
            if (mIsdark) {
                ivMute.setImageDrawable(getDrawable(R.mipmap.icon_mute_dark));
            } else {
                ivMute.setImageDrawable(getDrawable(R.mipmap.icon_mute));
            }
        } else {
            mTRTCCloud.muteLocalAudio(true);
            if (mIsdark) {
                ivMute.setImageDrawable(getDrawable(R.mipmap.meeting_room_icon_mute_off_dark));
            } else {
                ivMute.setImageDrawable(getDrawable(R.mipmap.meeting_icon_mute_off_night));
            }
        }

    }

    //是否打开视频
    private void muteVideo(boolean isChange) {
        isOpenVideo = SPUtils.getInstance().getBoolean(Constant.IS_OPEN_VIDEO);
        if (isChange) {
            isOpenVideo = !isOpenVideo;
            SPUtils.getInstance().put(Constant.IS_OPEN_VIDEO, isOpenVideo);
        }
        if (isOpenVideo) {
            ivCamera.setVisibility(View.VISIBLE);
            if (mIsdark) {
                ivVideo.setImageDrawable(getDrawable(R.mipmap.icon_video_dark));
            } else {
                ivVideo.setImageDrawable(getDrawable(R.mipmap.icon_video));
            }
            if (isHost()) {
                mTRTCCloud.startLocalPreview(isOpenVideo, txcvvHost);
                txcvvHost.setVisibility(View.VISIBLE);
            } else {
                if (memberAdapter != null) {
                    memberAdapter.notifyItemChanged(0);
                }
            }
        } else {
            ivCamera.setVisibility(View.GONE);
            if (mIsdark) {
                ivVideo.setImageDrawable(getDrawable(R.mipmap.meeting_room_icon_video_off_dark));
            } else {
                ivVideo.setImageDrawable(getDrawable(R.mipmap.meeting_icon_video_off_night));
            }
            if (isHost()) {
                mTRTCCloud.stopLocalPreview();
                txcvvHost.setVisibility(View.GONE);
            } else {
                if (memberAdapter != null) {
                    memberAdapter.notifyItemChanged(0);
                }
            }
        }
    }

    //退出房间
    private void exitRoom() {
        if (mTRTCCloud != null) {
            mTRTCCloud.stopLocalAudio();
            mTRTCCloud.stopLocalPreview();
            mTRTCCloud.exitRoom();
            mTRTCCloud.setListener(null);
        }
        mTRTCCloud = null;
        TRTCCloud.destroySharedInstance();
        finish();
    }

    //主持人离开房间
    private void leavRoom() {
        if (mTRTCCloud != null) {
            mTRTCCloud.stopLocalAudio();
            mTRTCCloud.stopLocalPreview();
            mTRTCCloud.setListener(null);
        }
        finish();
    }

    //判断是否主持人
    private boolean isHost() {
        if (mUserId.equals(walletBean.getPubKey())) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
            exitMeetingTip();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //离开房间提示
    private void exitMeetingTip() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_leav_meeting_tip, null);
        TextView tvMemberLeav = view.findViewById(R.id.tv_title_tip);
        TextView tvLeav = view.findViewById(R.id.tv_leav_meeting);
        TextView tvEnd = view.findViewById(R.id.tv_end_meeting);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);

        if (!isHost()) {
            tvLeav.setVisibility(View.GONE);
            tvEnd.setText(getString(R.string.leav_meeting));
            tvMemberLeav.setText(getString(R.string.exit_meeting_member_tip));
        }

        tvLeav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                leavRoom();
            }
        });

        tvEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                exitRoom();
                if (isHost()) {
                    presenter.disbandRoom(mRoomNo);
                    presenter.userExitRoom(walletBean.getPubKey(), 3, mRoomNo);
                } else {
                    presenter.userExitRoom(walletBean.getPubKey(), 2, mRoomNo);
                }
            }
        });

        tvCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    /**
     * 请求悬浮窗权限
     */
    private void loadPermissions() {
        if (PermissionUtils.checkPermission(this)) {
            moveTaskToBack(true);
            VideoFloatWindView.doOnActivityResume(this);
        } else {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(getString(R.string.open_float_window_tip));
            alert.setNegativeButton(getString(R.string.cancel), ((dialog, which) -> dialog.dismiss()));
            alert.setPositiveButton(getString(R.string.go_open), (dialog, which) -> {
                moveTaskToBack(true);
                VideoFloatWindView.doOnActivityResume(this);
                dialog.dismiss();
            });
            alert.show();
        }
    }
}