package com.qianlihu.solanawallet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.MemberManagerAdapter;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.meeting.MemberManagerBean;
import com.qianlihu.solanawallet.mvp.meeting_room.bean.MemberBean;
import com.qianlihu.solanawallet.mvp.meeting_room.presenter.MeetingRoomPresenter;
import com.qianlihu.solanawallet.mvp.meeting_room.view.MeetingRoomView;
import com.qianlihu.solanawallet.mvp.member_manager.MemberManagerPresenter;
import com.qianlihu.solanawallet.mvp.member_manager.MemberManagerView;
import com.qianlihu.solanawallet.view.GridSpaceItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MemberManagerActivity extends BaseActivity<MemberManagerPresenter> implements MemberManagerView {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_person)
    TextView tvPerson;
    @BindView(R.id.iv_header)
    CircleImageView ivHeader;
    @BindView(R.id.tv_host_name)
    TextView tvHostName;
    @BindView(R.id.tv_moderator)
    TextView tvModerator;
    @BindView(R.id.rv_member)
    RecyclerView rvMember;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;
    @BindView(R.id.btn_kick_out)
    Button btnKickOut;

    private List<MemberManagerBean> beanList;
    private MemberManagerAdapter managerAdapter;

    private List<MemberBean> memberBeanList;
    private String mRoomNo = "";
    private List<String> mKickoutList = new ArrayList<>();

    @Override
    public MemberManagerPresenter createPresenter() {
        return new MemberManagerPresenter(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_member_manager;
    }

    @Override
    protected void initView() {
        if (mIsdark) {
            rlBg.setBackgroundColor(getColor(R.color.bg_232323));
        }

        String roomName = getIntent().getStringExtra("roomName");
        mRoomNo = getIntent().getStringExtra("roomId");
        String hostName = getIntent().getStringExtra("hostName");
        String hostHeader = getIntent().getStringExtra("hostHeader");
        int userType = getIntent().getIntExtra("userType", 1);
        if (userType == 1) {
            btnKickOut.setVisibility(View.VISIBLE);
            tvTitle.setText(getString(R.string.member_manager));
        } else {
            btnKickOut.setVisibility(View.GONE);
            tvTitle.setText(getString(R.string.meeting_member));
        }
        List<MemberBean> memberBeanList = (List<MemberBean>) getIntent().getSerializableExtra("member");
        if (memberBeanList.size() == 0) {
            btnKickOut.setVisibility(View.GONE);
        }
        tvHostName.setText(hostName);
        tvName.setText(roomName);
        tvNo.setText(mRoomNo);
        tvPerson.setText(memberBeanList.size()+"");

        Glide.with(this).load(hostHeader).error(R.mipmap.icon_solana).into(ivHeader);
        beanList = new ArrayList<>();
        for (int i = 0; i < memberBeanList.size(); i++) {
            MemberManagerBean bean = new MemberManagerBean();
            bean.setCheck(false);
            bean.setUserId(memberBeanList.get(i).getUserId());
            bean.setUserName(memberBeanList.get(i).getName());
            beanList.add(bean);
        }
        rvMember.setLayoutManager(new LinearLayoutManager(this));
        rvMember.addItemDecoration(new GridSpaceItemDecoration(1, 40, 0));
        managerAdapter = new MemberManagerAdapter(beanList, userType);
        rvMember.setAdapter(managerAdapter);
        managerAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.iv_check) {
                boolean isCheck = beanList.get(position).isCheck();
                MemberManagerBean bean = beanList.get(position);
                if (isCheck) {
                    bean.setCheck(false);
                } else {
                    bean.setCheck(true);
                }
                beanList.set(position, bean);
                managerAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.btn_kick_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_kick_out:
               kickoutMember();
                break;
        }
    }

    private void kickoutMember() {
        if (beanList.size() > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < beanList.size(); i++) {
                boolean isCheck = beanList.get(i).isCheck();
                if (isCheck) {
                    mKickoutList.add(beanList.get(i).getUserId());
                    beanList.remove(i);
                    i--;
                }
            }
            if (mKickoutList.size() == 0) {
                return;
            }
            sb.replace(sb.length(), sb.length(), "");
            for (int i = 0; i < mKickoutList.size(); i++) {
                sb.append(mKickoutList.get(i));
                if (mKickoutList.size() != (i + 1)) {
                    sb.append(",");
                }
            }
            presenter.kickoutRoom(mRoomNo, sb.toString());
            managerAdapter.notifyDataSetChanged();closeLoadingDialog();
        }
    }

    @Override
    public void kickoutRoomCallback(String msg) {
        showInfo(msg);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("kickoutMember", (Serializable) mKickoutList);
        intent.putExtras(bundle);
        setResult(666, intent);
        finish();
    }
}