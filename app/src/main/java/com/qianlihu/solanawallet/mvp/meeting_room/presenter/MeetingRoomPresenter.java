package com.qianlihu.solanawallet.mvp.meeting_room.presenter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.HongbaoActivity;
import com.qianlihu.solanawallet.activity.MeetingRoomActivity;
import com.qianlihu.solanawallet.activity.MeetingSafeActivity;
import com.qianlihu.solanawallet.activity.MeetingSetActivity;
import com.qianlihu.solanawallet.activity.MeetingShareActivity;
import com.qianlihu.solanawallet.adapter.MeetingMoreAdapter;
import com.qianlihu.solanawallet.api.retrofit.ApiResponse;
import com.qianlihu.solanawallet.api.retrofit.ApiServer;
import com.qianlihu.solanawallet.base.BaseObserver;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.meeting.IconNameBean;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.DisbandRoom;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.ExistRoomBean;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.MeetingRoomUserBean;
import com.qianlihu.solanawallet.mvp.meeting_room.view.MeetingRoomView;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.xuexiang.xutil.resource.ResUtils.getString;

/**
 * author : Duan
 * date   : 2022/12/1916:30
 * desc   :
 * version: 1.0.0
 */
public class MeetingRoomPresenter extends BasePresenter<MeetingRoomView> {
    public MeetingRoomPresenter(MeetingRoomView baseView) {
        super(baseView);
    }

    public void getRoomMemberList(String languge, String roomNo) {

        addDisposable(apiServer.userListRoom(languge, roomNo), new BaseObserver<ApiResponse<MeetingRoomUserBean>>(baseView) {
            @Override
            public void onSuccess(ApiResponse<MeetingRoomUserBean> data) {
                baseView.roomMemberListCallback(data.getData());
            }

            @Override
            public void onError(String msg, int code) {

            }
        });
    }

    //用户退出房间
    public void userExitRoom(String address, int type, String roomId) {
        addDisposable(apiServer.userExitRoom(address, type, roomId), new BaseObserver<ApiResponse<ExistRoomBean>>(baseView) {
            @Override
            public void onSuccess(ApiResponse<ExistRoomBean> data) {

            }

            @Override
            public void onError(String msg, int code) {

            }
        });
    }

    //解散房间
    public void disbandRoom(String roomId) {
        DisbandRoom room = new DisbandRoom();
        room.setRoom_id(roomId);
        addDisposable(apiServer.disbandRoom(room), new BaseObserver<ApiResponse<String>>(baseView) {
            @Override
            public void onSuccess(ApiResponse<String> data) {

            }

            @Override
            public void onError(String msg, int code) {

            }
        });
    }

    public void moreDialog(Context context, boolean mIsdark, String time, String roomId, String author) {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_meeting_room_more, null, false);
        RecyclerView rvMore = view.findViewById(R.id.rv_more);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(v -> sheetDialog.dismiss());

        int spanCount = 4;
        String type = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
        if (type.equals(Constant.LANGUAGE_CN)) {
            spanCount = 5;
        }

        rvMore.setLayoutManager(new GridLayoutManager(context, spanCount));
        rvMore.addItemDecoration(new GridSpaceItemDecoration(spanCount, 30, 80));
        String[] nameArr = {
                context.getString(R.string.more_intive),
                context.getString(R.string.more_safe),
//                getString(R.string.more_chat),
                context.getString(R.string.more_red_paper),
                context.getString(R.string.more_float_window),
                context.getString(R.string.more_set)
        };
        Integer[] iconArr = {
                R.mipmap.icon_meeting_more_invite,
                R.mipmap.icon_meeting_more_safe,
//                R.mipmap.icon_meeting_more_chat,
                R.mipmap.icon_meeting_more_red_paper,
                R.mipmap.icon_meeting_more_float,
                R.mipmap.icon_meeting_more_set
        };

        Integer[] iconArrDark = {
                R.mipmap.icon_meeting_more_invite_dark,
                R.mipmap.icon_meeting_more_safe_dark,
//                R.mipmap.icon_meeting_more_chat_dark,
                R.mipmap.icon_meeting_more_red_paper_dark,
                R.mipmap.icon_meeting_more_float_dark,
                R.mipmap.icon_meeting_more_set_dark
        };

        List<IconNameBean> list = new ArrayList<>();
        for (int i = 0; i < nameArr.length; i++) {
            IconNameBean bean = new IconNameBean();
            bean.setName(nameArr[i]);
            if (mIsdark) {
                bean.setIcon(iconArrDark[i]);
            } else {
                bean.setIcon(iconArr[i]);
            }

            list.add(bean);
        }
        MeetingMoreAdapter moreAdapter = new MeetingMoreAdapter(list);
        rvMore.setAdapter(moreAdapter);
        moreAdapter.setOnItemClickListener((adapter, view1, position) -> {
            sheetDialog.dismiss();
            if (position == 0) {
                inviteDialog(context, mIsdark, time, roomId, author);
            } else if (position == 1) {
                context.startActivity(new Intent(context, MeetingSafeActivity.class));
            } else if (position == 2) {
                context.startActivity(new Intent(context, HongbaoActivity.class));
            } else if (position == 3) {
                baseView.inviteTypeCheck(5);
            } else if (position == 4) {
                context.startActivity(new Intent(context, MeetingSetActivity.class));
            }
        });
        sheetDialog.setContentView(view);
        sheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        sheetDialog.show();
    }

    private void inviteDialog(Context context, boolean mIsdark, String time, String roomId, String author) {
        BottomSheetDialog sheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_meeting_room_more_invite, null, false);
        RecyclerView rvInvite1 = view.findViewById(R.id.rv_invite_1);
        RecyclerView rvInvite2 = view.findViewById(R.id.rv_invite_2);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(v -> sheetDialog.dismiss());

        rvInvite1.setLayoutManager(new GridLayoutManager(context, 3));
        rvInvite1.addItemDecoration(new GridSpaceItemDecoration(3, 30, 40));
        rvInvite2.setLayoutManager(new GridLayoutManager(context, 3));
        rvInvite2.addItemDecoration(new GridSpaceItemDecoration(3, 30, 40));
        String[] nameArr = {
                context.getString(R.string.invite_weichat),
                context.getString(R.string.invite_qq),
        };
        Integer[] iconArr = {
                R.mipmap.icon_invite_weichat,
                R.mipmap.icon_invite_qq,
        };
        Integer[] iconArrDark = {
                R.mipmap.icon_invite_weichat_dark,
                R.mipmap.icon_invite_qq_dark,
        };
        List<IconNameBean> list = new ArrayList<>();
        for (int i = 0; i < nameArr.length; i++) {
            IconNameBean bean = new IconNameBean();
            bean.setName(nameArr[i]);
            if (mIsdark) {
                bean.setIcon(iconArrDark[i]);
            } else {
                bean.setIcon(iconArr[i]);
            }
            list.add(bean);
        }
        MeetingMoreAdapter moreAdapter = new MeetingMoreAdapter(list);
        rvInvite1.setAdapter(moreAdapter);
        moreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                sheetDialog.dismiss();
                if (position == 0) {
                    baseView.inviteTypeCheck(1);
                } else if (position == 1) {
                    baseView.inviteTypeCheck(2);
                }
            }
        });
        String[] nameArr2 = {
                context.getString(R.string.invite_copy),
                context.getString(R.string.invite_qr),
        };
        Integer[] iconArr2 = {
                R.mipmap.icon_invite_copy,
                R.mipmap.icon_invite_qr,
        };
        Integer[] iconArr2Dark = {
                R.mipmap.icon_invite_copy_dark,
                R.mipmap.icon_invite_qr_dark,
        };
        List<IconNameBean> list2 = new ArrayList<>();
        for (int i = 0; i < nameArr2.length; i++) {
            IconNameBean bean = new IconNameBean();
            bean.setName(nameArr2[i]);
            if (mIsdark) {
                bean.setIcon(iconArr2Dark[i]);
            } else {
                bean.setIcon(iconArr2[i]);
            }
            list2.add(bean);
        }
        MeetingMoreAdapter moreAdapter2 = new MeetingMoreAdapter(list2);
        rvInvite2.setAdapter(moreAdapter2);
        moreAdapter2.setOnItemClickListener((adapter, view1, position) -> {
            sheetDialog.dismiss();
            if (position == 0) {
                baseView.inviteTypeCheck(3);
            } else if (position == 1) {
                baseView.inviteTypeCheck(4);
            }
        });
        sheetDialog.setContentView(view);
        sheetDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        sheetDialog.show();
    }
}
