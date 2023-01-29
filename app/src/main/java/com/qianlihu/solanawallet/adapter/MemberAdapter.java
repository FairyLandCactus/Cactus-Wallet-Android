package com.qianlihu.solanawallet.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.meeting.utils.GenerateUserSig;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.MemberBean;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.tencent.liteav.base.ContextUtils.getApplicationContext;

/**
 * author : Duan
 * date   : 2022/12/1314:08
 * desc   :
 * version: 1.0.0
 */
public class MemberAdapter extends BaseQuickAdapter<MemberBean, BaseViewHolder> {

    private TRTCCloud trtcCloud;
    private int userType;
    List<MemberBean> data;

    public MemberAdapter(@Nullable List<MemberBean> data, TRTCCloud trtcCloud, int userType) {
        super(R.layout.item_member, data);
        this.trtcCloud = trtcCloud;
        this.userType = userType;
        this.data = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, MemberBean item) {
        updateMemberInfo(helper,item);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder helper, int position) {
        MemberBean item = data.get(position);
        updateMemberInfo(helper, item);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    private void updateMemberInfo(BaseViewHolder helper, MemberBean item) {
        helper.setText(R.id.tv_user_name, item.getUserId());
        TXCloudVideoView txcvvMember = helper.getView(R.id.txcvv_member);
        CircleImageView civHeader = helper.getView(R.id.iv_header);
        Glide.with(mContext).load(item.getHeader()).error(R.mipmap.icon_solana).into(civHeader);
        RelativeLayout rlAudio = helper.getView(R.id.rl_audio);
        boolean isVideo = item.isVideo();
        int layoutPosition = helper.getLayoutPosition();
        if (layoutPosition == 0 && userType == 0) {
            TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
            trtcParams.sdkAppId = GenerateUserSig.SDKAPPID;
            trtcParams.userId = item.getUserId();
            trtcParams.roomId = Integer.parseInt(item.getRoomId());
            trtcParams.userSig = GenerateUserSig.genUserSig(trtcParams.userId);
            boolean isOpenVideo = SPUtils.getInstance().getBoolean(Constant.IS_OPEN_VIDEO);
            if (isOpenVideo) {
                rlAudio.setVisibility(View.GONE);
                txcvvMember.setVisibility(View.VISIBLE);
                trtcCloud.startLocalPreview(true, txcvvMember);
            } else {
                txcvvMember.setVisibility(View.GONE);
                rlAudio.setVisibility(View.VISIBLE);
                trtcCloud.stopLocalPreview();
            }
            trtcCloud.startLocalAudio(TRTCCloudDef.TRTC_AUDIO_QUALITY_SPEECH);
            trtcCloud.enterRoom(trtcParams, TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);
            Log.i("duan==topRemot", "stopRemoteView11    " + isOpenVideo);
        } else {
            if (isVideo) {
                if (trtcCloud != null) {
                    rlAudio.setVisibility(View.GONE);
                    txcvvMember.setVisibility(View.VISIBLE);
                    trtcCloud.startRemoteView(item.getUserId(), TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG,
                            txcvvMember);
                }
            } else {
                txcvvMember.setVisibility(View.GONE);
                rlAudio.setVisibility(View.VISIBLE);
                trtcCloud.stopRemoteView(item.getUserId(), TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG);
            }
            Log.i("duan==topRemot", "stopRemoteView22   " + isVideo);
        }
    }
}
