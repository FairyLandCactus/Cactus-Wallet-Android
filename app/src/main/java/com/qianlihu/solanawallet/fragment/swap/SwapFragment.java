package com.qianlihu.solanawallet.fragment.swap;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.MainActivity;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.activity.AddWalletActivity;
import com.qianlihu.solanawallet.adapter.ChainNameAdapter;
import com.qianlihu.solanawallet.adapter.MyWalletAdapter;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.base.BaseFragment;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.ChainNameBean;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.DialogUtils;
import com.qianlihu.solanawallet.utils.LoadWebPageUtils;
import com.qianlihu.solanawallet.utils.MyUtils;
import com.qianlihu.solanawallet.utils.RepeatClickUtil;
import com.qianlihu.solanawallet.utils.WalletResource;
import com.qianlihu.solanawallet.view.GlobalLayoutUtil;
import com.qianlihu.solanawallet.wallet.IPSelectWallet;
import com.qianlihu.solanawallet.wc.DappBrowserSwipeInterface;
import com.qianlihu.solanawallet.wc.DappBrowserSwipeLayout;
import com.qianlihu.solanawallet.wc.Web3View;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.qianlihu.solanawallet.utils.LoadWebPageUtils.determineMimeType;
import static com.xuexiang.xutil.resource.ResUtils.getColor;

/**
 * author : Duan
 * date   : 2021/11/118:15
 * desc   :
 * version: 1.0.0
 */
public class SwapFragment extends BaseFragment implements DappBrowserSwipeInterface {

    @BindView(R.id.swip_refresh)
    DappBrowserSwipeLayout swipRefresh;
    @BindView(R.id.pb_web)
    ProgressBar pbWeb;
    @BindView(R.id.wv_h5_QA)
    Web3View wvH5QA;
    @BindView(R.id.rl_net_error)
    RelativeLayout rlNetError;

    private String methodName = "";
    private final String REG_LOGIN_TOKEN = "regLoginToken";
    private final String SELECT_ADDRESS = "selectAddress";
    private ValueCallback<Uri[]> uploadMessage;

    private int REQUEST_CODE = 1234;

    public static SwapFragment newInstance() {
        SwapFragment fragment = new SwapFragment();
        return fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        //禁止app录屏和截屏
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        return R.layout.fragment_swap;
    }

    @Override
    public void initView() {
        GlobalLayoutUtil util = new GlobalLayoutUtil(getActivity());
        util.init();
        swipRefresh.setRefreshInterface(this);
        swipRefresh.setColorSchemeColors(getColor(R.color.yellow));
        loadChatUI();
        rlNetError.setOnClickListener(v -> loadChatUI());
    }

    @Override
    public void initData() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wvH5QA != null) {
            wvH5QA.removeAllViews();
            wvH5QA.destroy();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (wvH5QA != null) {
                MyUtils.hideKeyboard(wvH5QA);
            }
        }
    }

    private String[] writePermissions = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private void loadChatUI() {
        String mUrl = "https://hjweb.liecaizhijia.com/";
//        String mUrl = "http://192.168.2.212:8000/";
        LoadWebPageUtils.webViewPage(getActivity(), wvH5QA, mUrl, true);

        wvH5QA.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (pbWeb != null) {
                    if (newProgress == 100) {
                        pbWeb.setVisibility(View.GONE);//加载完网页进度条消失
                        rlNetError.setVisibility(View.GONE);
                        swipRefresh.setVisibility(View.VISIBLE);
                    } else {
                        pbWeb.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                        pbWeb.setProgress(newProgress);//设置进度值
                    }
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                result.confirm();
                return true;
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public void onPermissionRequest(PermissionRequest request) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Log.i("duan==webview", "onPermissionRequest  ");
                            request.grant(request.getResources());
                            request.getOrigin();
                        }
                    }
                });

            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                uploadMessage = filePathCallback;
                if (fileChooserParams == null) {
                    return true;
                }
                getContent.launch(determineMimeType(fileChooserParams));
//                LoadWebPageUtils.takePhoto(getActivity());
                return true;
            }
        });
        wvH5QA.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.i("duan==webview", "onPageStarted  " + url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Log.i("duan==webview", "onReceivedHttpError  " + errorResponse.getStatusCode());
                rlNetError.setVisibility(View.VISIBLE);
                swipRefresh.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i("duan==webview", "shouldOverrideUrlLoading  " + url);
                if (url.contains("#")) {
                    String str1 = url.substring(0, url.indexOf("#"));
                    String str2 = url.substring(str1.length() + 1, url.length());
                    String str3 = str2.replaceAll("%22", "\"");
                    if (!TextUtils.isEmpty(str2)) {
                        Log.i("duan==webview", "截取数据  " + str3);
                        if (!TextUtils.isEmpty(str3)) {
                            if (str3.contains("=")) {
                                String str4 = str3.substring(0, str3.indexOf("="));
                                methodName = str4;
                                Log.i("duan==webview", "方法名==   " + str4);
                                String str5 = str3.substring(str4.length() + 1);
                                if (!TextUtils.isEmpty(str4)) {
                                    if (str4.equals(REG_LOGIN_TOKEN)) {
//                                        myWallet();
                                    } else if (str4.equals(SELECT_ADDRESS)) {
                                        if (lacksPermission(writePermissions)) {
                                            requestPermissions(writePermissions, 0);
                                            return true;
                                        }
                                        selectMyWallet();
                                    }
                                    return true;
                                }
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    ActivityResultLauncher<String> getContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    if (uri != null) {
                        uploadMessage.onReceiveValue(new Uri[]{uri});
                    } else {
                        uploadMessage.onReceiveValue(null);
                        uploadMessage = null;
                    }
                }
            });

    //选择我的钱包
    private void selectMyWallet() {
        DialogUtils.selectMyWallet(getActivity(), "0", new IPSelectWallet() {
            @Override
            public void onSelectWallet(WalletBean bean) {
                Gson gson = new Gson();
                Map<String, String> map = new HashMap<>();
                map.put("chain", bean.getMainChain());
                map.put("address", bean.getPubKey());
                String jsonStr = gson.toJson(map);
                wvH5QA.evaluateJavascript("javascript:seccessCallback( \'" + methodName + "\',\'" + jsonStr + "\' )", null);
            }

            @Override
            public void onDismiss() {
                wvH5QA.evaluateJavascript("javascript:closeFun( \'" + 1 + "\' )", null);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (lacksPermission(writePermissions)) {
//                getActivity().finish();
            }
        }
    }

    @Override
    public void RefreshEvent() {
        Log.i("duan==getScrollY", "getScrollY===== " + wvH5QA.getScrollY());
        if (wvH5QA.getScrollY() == 0) {
            swipRefresh.setOnRefreshListener(() -> {
                swipRefresh.setRefreshing(false);
                loadChatUI();
            });
        }
    }

    @Override
    public int getCurrentScrollPosition() {
        return wvH5QA.getScrollY();
    }

    private long waitTime = 2000;
    private long touchTime = 0;
    public void onKeyDownChild(int keyCode, KeyEvent event) {
        if (wvH5QA.canGoBack()) {
            wvH5QA.goBack();
        } else {
            if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
                long currentTime = System.currentTimeMillis();
                if ((currentTime - touchTime) >= waitTime) {
                    //让Toast的显示时间和等待时间相同
                    showInfo(getString(R.string.press_again_to_exit));
                    touchTime = currentTime;
                } else {
                   getActivity().finish();
                }
            }
        }
    }
}
