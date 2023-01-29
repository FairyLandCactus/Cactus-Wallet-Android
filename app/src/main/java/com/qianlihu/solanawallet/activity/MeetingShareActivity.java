package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.utils.DateTimeUtils;
import com.qianlihu.solanawallet.utils.QRCodeUtil;
import com.qianlihu.solanawallet.utils.ShareUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeetingShareActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_currercy)
    ImageView ivCurrercy;
    @BindView(R.id.tv_host_name)
    TextView tvHostName;
    @BindView(R.id.tv_meeting_no)
    TextView tvMeetingNo;
    @BindView(R.id.tv_time_1)
    TextView tvTime1;
    @BindView(R.id.tv_tiem_long)
    TextView tvTiemLong;
    @BindView(R.id.tv_time_2)
    TextView tvTime2;
    @BindView(R.id.tv_date_1)
    TextView tvDate1;
    @BindView(R.id.tv_gmt)
    TextView tvGmt;
    @BindView(R.id.tv_date_2)
    TextView tvDate2;
    @BindView(R.id.iv_qr)
    ImageView ivQr;
    @BindView(R.id.ll_qr)
    LinearLayout llQr;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_meeting_share;
    }

    @Override
    protected void initView() {

        tvTitle.setText(getString(R.string.share_meeting_qr));
        if (mIsdark) {
            rlBg.setBackgroundColor(getColor(R.color.bg_232323));
        }
        String roomId = getIntent().getStringExtra("roomId");
        String time = getIntent().getStringExtra("time");
        String auth = getIntent().getStringExtra("auth");
        if (!TextUtils.isEmpty(time)) {
            String startTime = DateTimeUtils.formatData("HH:mm", DateTimeUtils.getLongTime(time));
            String endTime = DateTimeUtils.formatData("HH:mm", DateTimeUtils.getLongTime(DateTimeUtils.addDateMinut(time,1)));
            String date = DateTimeUtils.getStringByFormat(time, DateTimeUtils.dateFormatYMD);
            if (languageType.equals("en")) {
                date = DateTimeUtils.convertTimestamp2DateEnglish2(DateTimeUtils.getLongTime(time));
            }
            tvTime1.setText(startTime);
            tvTime2.setText(endTime);
            tvDate1.setText(date);
            tvDate2.setText(date);
        }
        ivCurrercy.setVisibility(View.VISIBLE);
        ivCurrercy.setImageDrawable(getDrawable(R.mipmap.icon_share_meeting));
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
        tvMeetingNo.setText(roomId);
        String qrUrl = "join_meeting#"+roomId;
        Bitmap bitmap = QRCodeUtil.createQRImage(qrUrl, 320, 320, bmp, null);
        ivQr.setImageBitmap(bitmap);

        tvHostName.setText(String.format(getString(R.string.share_launch), auth));
        tvTiemLong.setText(String.format(getString(R.string.share_hour), 1+""));
    }

    @Override
    protected void initData() {

    }

    private String[] writePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    @OnClick({R.id.iv_back, R.id.iv_currercy, R.id.ll_save_album})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_currercy:
                share(1);
                break;
            case R.id.ll_save_album:
                share(0);
                break;
        }
    }

    private void share(int flag) {
        if (lacksPermission(writePermissions)) {
            ActivityCompat.requestPermissions(this, writePermissions, 0);
            return;
        }
//        ShareUtil.shareImage2(this, llShareQr);
        ShareUtil.shareImage2(this, llQr, flag);
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