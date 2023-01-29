package com.qianlihu.solanawallet.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.LoadWebPageUtils;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.wc.DappBrowserSwipeInterface;
import com.qianlihu.solanawallet.wc.DappBrowserSwipeLayout;
import com.qianlihu.solanawallet.wc.Web3View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SolscanWebActivity extends BaseActivity implements DappBrowserSwipeInterface {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.pb_web)
    ProgressBar pbWeb;
    @BindView(R.id.wv_h5_QA)
    WebView wvH5QA;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_solscan_web;
    }

    @Override
    protected void initView() {
        String mUrl = getIntent().getStringExtra(Constant.WEB_URL);
        String title = getIntent().getStringExtra(Constant.WEB_TITLE);
        tvTitle.setText(title);
        LoadWebPageUtils.webViewPage(this, wvH5QA, mUrl, true);
        wvH5QA.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webview, int newProgress) {
                if (pbWeb != null) {
                    if (newProgress == 100) {
                        pbWeb.setVisibility(View.GONE);//加载完网页进度条消失
                    } else {
                        pbWeb.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                        pbWeb.setProgress(newProgress);//设置进度值
                    }
                }
            }
        });

        wvH5QA.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
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
}