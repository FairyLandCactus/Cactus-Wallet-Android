package com.qianlihu.solanawallet.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.LanguageUtil;
import com.qianlihu.solanawallet.utils.SelectTypeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetUpActivity extends BaseActivity {

    @BindView(R.id.tv_management)
    TextView tvManagement;
    @BindView(R.id.switch_pwd)
    Switch switchPwd;
    @BindView(R.id.switch_during)
    Switch switchDuring;
    @BindView(R.id.tv_language)
    TextView tvLanguage;
    @BindView(R.id.tv_unit)
    TextView tvUnit;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private List<String> mLanguageList = new ArrayList<>();

    private boolean isChange = false;

    private String types;
    private int flag = 1;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_set_up;
    }

    @Override
    protected void initView() {
        tvTitle.setText(getString(R.string.set_up));

        mLanguageList.add(getString(R.string.chinese));
        mLanguageList.add(getString(R.string.english));

        types = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
        if (types.isEmpty()) {
            tvLanguage.setText(getString(R.string.chinese));
        } else {
            if (types.equals("en")) {
                tvLanguage.setText(getString(R.string.english));
            } else {
                tvLanguage.setText(getString(R.string.chinese));
            }
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.tv_management, R.id.ll_language, R.id.ll_unit, R.id.tv_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                if (isChangeLanguage()) {
                    Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
                    LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LaunchIntent);
                }
                break;
            case R.id.tv_management:
                startActivity(new Intent(this, AddressManagerActivity.class));
                break;
            case R.id.ll_language:
//                SelectTypeUtils.selectTypePop(this, tvLanguage, tvLanguage, R.drawable.language_bg_bord, 0, 400, mLanguageList, (itemView, position) -> {
//                    flag = position;
//                    reload();
//                });
                selectLanguage();
                break;
            case R.id.ll_unit:
                break;
            case R.id.tv_setting:
                break;
        }
    }

    private void selectLanguage() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_select_language, null, false);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        ImageView ivChoose1 = view.findViewById(R.id.iv_choose_1);
        ImageView ivChoose2 = view.findViewById(R.id.iv_choose_2);
        ImageView ivChoose3 = view.findViewById(R.id.iv_choose_3);
        TextView tvChinese = view.findViewById(R.id.tv_chinese);
        TextView tvEnglish = view.findViewById(R.id.tv_english);
        TextView tvRussia = view.findViewById(R.id.tv_ru);
        TextView tvClose = view.findViewById(R.id.tv_close);
        RelativeLayout rlChinese = view.findViewById(R.id.rl_chinese);
        RelativeLayout rlEnglish = view.findViewById(R.id.rl_english);
        RelativeLayout rlRussia = view.findViewById(R.id.rl_ru);

        String chinese = tvChinese.getText().toString().trim();
        String english = tvEnglish.getText().toString().trim();
        String russian = tvRussia.getText().toString().trim();
        if (types.isEmpty()) {
            ivChoose1.setVisibility(View.GONE);
            ivChoose2.setVisibility(View.VISIBLE);
        } else {
            if (types.equals("en")) {
                ivChoose1.setVisibility(View.VISIBLE);
                ivChoose2.setVisibility(View.GONE);
                ivChoose3.setVisibility(View.GONE);
            } else if (types.equals("ru")) {
                ivChoose1.setVisibility(View.GONE);
                ivChoose2.setVisibility(View.GONE);
                ivChoose3.setVisibility(View.VISIBLE);
            } else {
                ivChoose1.setVisibility(View.GONE);
                ivChoose2.setVisibility(View.VISIBLE);
                ivChoose3.setVisibility(View.GONE);
            }
        }
        rlChinese.setOnClickListener(v -> {
            dialog.dismiss();
            ivChoose1.setVisibility(View.GONE);
            ivChoose2.setVisibility(View.VISIBLE);
            ivChoose3.setVisibility(View.GONE);
            tvLanguage.setText(chinese);
            SPUtils.getInstance().put(Constant.LANGUAGE_TYPE, Constant.LANGUAGE_CN);
            SPUtils.getInstance().put(Constant.LANGUAGE_CN_EN, "zh-Hans");
            flag = 0;
            reload();
        });

        rlEnglish.setOnClickListener(v -> {
            dialog.dismiss();
            ivChoose1.setVisibility(View.VISIBLE);
            ivChoose2.setVisibility(View.GONE);
            ivChoose3.setVisibility(View.GONE);
            tvLanguage.setText(english);
            SPUtils.getInstance().put(Constant.LANGUAGE_TYPE, Constant.LANGUAGE_EN);
            SPUtils.getInstance().put(Constant.LANGUAGE_CN_EN, "en");
            flag = 2;
            reload();
        });

        rlRussia.setOnClickListener(v -> {
            dialog.dismiss();
            ivChoose1.setVisibility(View.GONE);
            ivChoose2.setVisibility(View.GONE);
            ivChoose3.setVisibility(View.VISIBLE);
            tvLanguage.setText(russian);
            SPUtils.getInstance().put(Constant.LANGUAGE_TYPE, Constant.LANGUAGE_RU);
            SPUtils.getInstance().put(Constant.LANGUAGE_CN_EN, "ru");
            flag = 3;
            reload();
        });

        tvClose.setOnClickListener(v -> dialog.dismiss());
        ivClose.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();

    }

    /**
     * 重启activity
     */
    public void reload() {
        if (flag == 0) {
            LanguageUtil.setLanguage(this, Locale.CHINESE);
        } else if (flag == 2){
            LanguageUtil.setLanguage(this, Locale.ENGLISH);
        } else if (flag == 3) {
//            Locale locale = this.getResources().getConfiguration().locale;
            Locale locale = new Locale("ru");
            LanguageUtil.setLanguage(this, locale);
        }

        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);

//        Intent intent = getIntent();
//        overridePendingTransition(0, 0);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        finish();
//        overridePendingTransition(0, 0);
//        startActivity(intent);
    }

    private boolean isChangeLanguage() {
        String type = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
        if (type.equals(CacheData.shared().languageType)) {
            isChange = false;
        } else {
            isChange = true;
        }
        return isChange;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isChangeLanguage()) {
            Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(getApplication().getPackageName());
            LaunchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(LaunchIntent);
        }
    }
}