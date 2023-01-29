package com.qianlihu.solanawallet.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.DateTimeUtils;
import com.qianlihu.solanawallet.utils.LoadWebPageUtils;
import com.qianlihu.solanawallet.wallet_bean.ArticleDetailBean;
import com.qianlihu.solanawallet.wallet_bean.ArticlesListBean;
import com.qianlihu.solanawallet.wc.DappBrowserSwipeInterface;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ArticlesDetailWebActivity extends BaseActivity implements DappBrowserSwipeInterface {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.pb_web)
    ProgressBar pbWeb;
    @BindView(R.id.wv_h5_QA)
    WebView wvH5QA;
    @BindView(R.id.tv_article_title)
    TextView tvArticleTitle;
    @BindView(R.id.tv_auth)
    TextView tvAuth;
    @BindView(R.id.tv_time)
    TextView tvTime;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article_detail_web;
    }

    @Override
    protected void initView() {
        String mUrl = getIntent().getStringExtra(Constant.WEB_URL);
        String title = getIntent().getStringExtra(Constant.WEB_TITLE);
        tvTitle.setText(title);
        wvH5QA.setBackgroundColor(0);
        getArticleDetail();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wvH5QA != null) {
            wvH5QA.removeAllViews();
            wvH5QA.destroy();
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wvH5QA.canGoBack()) {
            wvH5QA.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void RefreshEvent() {

    }

    @Override
    public int getCurrentScrollPosition() {
        return wvH5QA.getScrollY();
    }

    private void getArticleDetail() {
        int id = getIntent().getIntExtra("id", 0);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("language", languageType);
        map.put("id", id);
        showLoadingDialog();
        HttpService.doGet(Constant.ARTICLES_DETAILS, map, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                runOnUiThread(() -> {
                    showInfo(e.getMessage());
                    closeLoadingDialog();
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                runOnUiThread(() -> {
                    if (!TextUtils.isEmpty(body)) {
                        Gson gson = new Gson();
                        ArticleDetailBean bean = gson.fromJson(body, ArticleDetailBean.class);
                        if (bean.getCode() == 1) {
                            tvArticleTitle.setText(bean.getData().getTitle());
                            tvAuth.setText(bean.getData().getAuthor());
                            tvTime.setText(DateTimeUtils.formatLanguageDate(bean.getData().getCreatetime()));
                            LoadWebPageUtils.webViewPageData(ArticlesDetailWebActivity.this, wvH5QA, bean.getData().getContent(), mIsdark);
                            wvH5QA.setWebChromeClient(new WebChromeClient() {
                                @Override
                                public void onProgressChanged(WebView webview, int newProgress) {
                                    if (pbWeb != null) {
                                        if (newProgress == 100) {
                                            closeLoadingDialog();
                                            pbWeb.setVisibility(View.GONE);//加载完网页进度条消失
                                        } else {
                                            pbWeb.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                                            pbWeb.setProgress(newProgress);//设置进度值
                                        }
                                    }
                                }
                            });
                        } else {
                            showInfo(bean.getMsg());
                        }
                    } else {
                        showInfo(getString(R.string.data_exception));
                    }
                });
            }
        });

    }

}