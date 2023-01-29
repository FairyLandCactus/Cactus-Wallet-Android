package com.qianlihu.solanawallet.activity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.wc.DappBrowserSwipeInterface;
import com.qianlihu.solanawallet.wc.DappBrowserSwipeLayout;
import com.qianlihu.solanawallet.wc.Web3View;

import butterknife.BindView;
import butterknife.OnClick;

import static com.qianlihu.solanawallet.utils.MyUtils.loadFile;

public class DappWebActivity extends BaseActivity implements DappBrowserSwipeInterface {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.pb_web)
    ProgressBar pbWeb;
    @BindView(R.id.wv_h5_QA)
    WebView wvH5QA;
    @BindView(R.id.web3view)
    Web3View web3;
    @BindView(R.id.swipe_refresh)
    DappBrowserSwipeLayout swipeRefresh;

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
//        LoadWebPageUtils.webViewPage(this, wvH5QA, mUrl);
        swipeRefresh.setRefreshInterface(this);
        web3.getSettings().setJavaScriptEnabled(true);
        web3.getSettings().setDomStorageEnabled(true);
        web3.loadUrl(MyUtils.formatUrl(mUrl));
        web3.setWebChromeClient(new WebChromeClient() {
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

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
//                Log.i("duan==webview", "onConsoleMessage  " + consoleMessage.message());
                return super.onConsoleMessage(consoleMessage);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.i("duan==webview", "onJsAlert  " + url);
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                Log.i("duan==webview", "onJsBeforeUnload  " + url);
                return super.onJsBeforeUnload(view, url, message, result);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                Log.i("duan==webview", "onJsConfirm  " + url);
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                Log.i("duan==webview", "onJsConfirm2  " + url);
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                if (resultMsg != null) {
                    Log.i("duan==webview", "onCreateWindow2  "+resultMsg.obj);
                }
                return true;
            }

            @Override
            public void onCloseWindow(WebView window) {
                super.onCloseWindow(window);
                Log.i("duan==webview", "onCloseWindow  ");
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.i("duan==webview", "onReceivedTitle  "+title);
            }

            @Override
            public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
                super.onReceivedTouchIconUrl(view, url, precomposed);
                Log.i("duan==webview", "onReceivedTouchIconUrl  "+url);
            }

            @Override
            public void onGeolocationPermissionsHidePrompt() {
                super.onGeolocationPermissionsHidePrompt();
                Log.i("duan==webview", "onGeolocationPermissionsHidePrompt  ");
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Log.i("duan==webview", "onShowFileChooser  ");
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);
            }

            @Override
            public void onRequestFocus(WebView view) {
                super.onRequestFocus(view);
                Log.i("duan==webview", "onRequestFocus  ");
            }

            @Override
            public boolean onJsTimeout() {
                Log.i("duan==webview", "onJsTimeout  ");
                return super.onJsTimeout();
            }

            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                super.onShowCustomView(view, callback);
                Log.i("duan==webview", "onShowCustomView  ");
            }
        });
        web3.evaluateJavascript(loadFile(DappWebActivity.this, R.raw.inpage), null);
        web3.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("duan==webview", "onPageStarted  " + url);
//                view.evaluateJavascript(loadFile(DappWebActivity.this, R.raw.inpage), null);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("duan==webview", "shouldOverrideUrlLoading  " + url);
                return super.shouldOverrideUrlLoading(view, url);
            }


            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
//                Log.i("duan==webview", "shouldOverrideKeyEvent  " + event.toString());
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//                Log.i("duan==webview", "shouldInterceptRequest  " + url);
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.i("duan==webview", "shouldOverrideUrlLoading2 " );
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
                Log.i("duan==webview", "onReceivedClientCertRequest " );
            }

            @Override
            public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
                super.onReceivedLoginRequest(view, realm, account, args);
                Log.i("duan==webview", "onReceivedLoginRequest " );
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("duan==webview", "onPageFinished  " + url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.i("duan==webview", "onLoadResource  " + url);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (web3 != null) {
            web3.removeAllViews();
            web3.destroy();
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web3.canGoBack()) {
            web3.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void RefreshEvent() {

    }

    @Override
    public int getCurrentScrollPosition() {
        return web3.getScrollY();
    }
}