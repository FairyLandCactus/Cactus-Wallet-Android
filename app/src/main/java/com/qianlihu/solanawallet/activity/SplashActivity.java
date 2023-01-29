package com.qianlihu.solanawallet.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.MainActivity;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.MyImageLoader;
import com.qianlihu.solanawallet.wallet_bean.GuidePageBean;
import com.youth.banner.Banner;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.rl_start_page)
    RelativeLayout rlStartPage;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.rl_guide_page)
    RelativeLayout rlGuidePage;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_dec)
    TextView tvDec;
    @BindView(R.id.tv_jump)
    TextView tvJump;
    @BindView(R.id.btn_into_app)
    Button btnIntoApp;

    private boolean isFirstInstall = false;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        int versionCode = AppUtils.getAppVersionCode();
        int versionCodeSP = SPUtils.getInstance().getInt(Constant.APP_VERSION_CODE, 1);
//        int versionCodeSP = 2;
        isFirstInstall = SPUtils.getInstance().getBoolean(Constant.IS_FIRST_INSTALL, true);
//        isFirstInstall = true;
        if (isFirstInstall || versionCode != versionCodeSP) {
            rlGuidePage.setVisibility(View.VISIBLE);
            rlStartPage.setVisibility(View.GONE);
            SPUtils.getInstance().put(Constant.IS_FIRST_INSTALL, false);
            SPUtils.getInstance().put(Constant.APP_VERSION_CODE, versionCode);
//            guidePage();
            getGuideImage();
        } else {
            rlGuidePage.setVisibility(View.GONE);
//            rlStartPage.setVisibility(View.VISIBLE);
            startPage();
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.btn_into_app, R.id.tv_jump})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_into_app:
            case R.id.tv_jump:
                Intent it = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(it);
                finish();//关闭当前活动
                break;

        }
    }

    private void getGuideImage() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("language", languageType);
        HttpService.doGet(Constant.GUIDE_PAGE, map, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                Log.i("duan==start", "onFailure====== "+e.getMessage());
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
//                Log.i("duan==start", "guide====== "+body);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(body)) {
                            Gson gson = new Gson();
                            GuidePageBean bean = gson.fromJson(body, GuidePageBean.class);
                            if (bean.getCode() == 1) {
                                if (bean.getData().size() > 0) {
                                    guidePage(bean.getData());
                                } else {
                                    startPage();
                                }
                            } else {
                                showInfo(bean.getMsg());
                            }
                        } else {
                            showInfo(getString(R.string.data_exception));
                        }
                    }
                });
            }
        });

    }

    private void guidePage(List<String> picList) {
        MyImageLoader loader = new MyImageLoader(1);
//        List<Integer> picList = new ArrayList<>();
//        List<Integer> picList2 = new ArrayList<>();
//        List<Integer> picList3 = new ArrayList<>();
//        picList.add(R.mipmap.guide_cn_1);
//        picList.add(R.mipmap.guide_cn_2);
//        picList.add(R.mipmap.guide_cn_3);
//        picList2.add(R.mipmap.guide_en_1);
//        picList2.add(R.mipmap.guide_en_2);
//        picList2.add(R.mipmap.guide_en_3);
//        picList3.add(R.mipmap.guide_ru_1);
//        picList3.add(R.mipmap.guide_ru_2);
//        picList3.add(R.mipmap.guide_ru_3);
        //设置图片加载器
        banner.setImageLoader(loader);
        //设置图片集合
        String type = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
//        if (type.equals("en")) {
//            banner.setImages(picList2);
//        } else if (type.equals("ru")) {
//            banner.setImages(picList3);
//        } else {
            banner.setImages(picList);
//        }
        banner.isAutoPlay(false);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                btnIntoApp.setVisibility(View.GONE);
                tvJump.setVisibility(View.VISIBLE);
                if (position == 0) {
                    tvTitle.setText(getString(R.string.guide_brand));
                    tvDec.setText(getString(R.string.guide_wallet));
                } else if (position == 1) {
                    tvTitle.setText(getString(R.string.guide_multiple));
                    tvDec.setText(getString(R.string.guide_various));
                } else if (position == 2) {
                    tvTitle.setText(getString(R.string.guide_available));
                    tvDec.setText(getString(R.string.guide_switched));
                    btnIntoApp.setVisibility(View.VISIBLE);
                    tvJump.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void startPage() {
        Intent it = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(it);
        finish();//关闭当前活动
    }

    @OnClick(R.id.tv_jump)
    public void onViewClicked() {
    }
}