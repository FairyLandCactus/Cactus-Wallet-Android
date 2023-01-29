package com.qianlihu.solanawallet.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.SPUtils;
import com.gyf.immersionbar.ImmersionBar;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.DialogView;
import com.qianlihu.solanawallet.utils.IComfirmCancel;
import com.qianlihu.solanawallet.utils.LanguageUtil;
import com.qianlihu.solanawallet.utils.RepeatClickUtil;
import com.qianlihu.solanawallet.utils.ThemeUtil;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

import static androidx.core.util.Preconditions.checkNotNull;

public abstract class BaseActivity<P extends BasePresenter> extends RxAppCompatActivity implements BaseView {

    private ProgressDialog dialog;
    private MaterialDialog materialDialog;
    protected Unbinder unbinder;
    protected Context mContext;
    public String languageType = "zh_CN";
    public boolean mIsdark = false;
    protected P presenter;

    public abstract P createPresenter();

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        //初始化语言
        Locale locale = this.getResources().getConfiguration().locale;
        LanguageUtil.languageUpdate(this, locale);
        ThemeUtil.setTheme(this);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        languageType = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
        mIsdark = SPUtils.getInstance().getBoolean(Constant.NIGHT);
        initView();
        initData();
        mContext = this;
        //状态栏配置
        configStatusBar();
//        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        EventBus.getDefault().unregister(this);
    }

    //处理用户频繁点击按钮
    public void repeatCheck() {
        if (RepeatClickUtil.isFastDoubleClick()) {
            showerrortoast("请勿频繁点击操作！");
            return;
        }
    }

    @SuppressLint({"AutoDispose", "RestrictedApi"})
    protected void send(Observable observable, Consumer consumer) {

        checkNotNull(observable, "observable is null");
        observable.compose(bindUntilEvent(ActivityEvent.DESTROY)).observeOn(AndroidSchedulers.mainThread()).subscribe(consumer);
    }

    @SuppressLint({"AutoDispose", "RestrictedApi"})
    protected void send(Observable observable, Observer observer) {
        checkNotNull(observable, "observable is null");
        observable.compose(bindUntilEvent(ActivityEvent.DESTROY)).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    @Override
    public void showLoading() {
        showLoadingDialog();
    }


    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }


    @Override
    public void showError(String msg) {
        showerrortoast(msg);
    }

    @Override
    public void showSuccess(String msg) {
        showSuccessToast(msg);
    }

    @Override
    public void onErrorCode(int code, String msg) {
        showerrortoast(msg);
    }

    @Override
    public void showLoadingFileDialog(String msg) {
        showFileDialog(msg);
    }

    @Override
    public void hideLoadingFileDialog() {
        hideFileDialog();
    }

    @Override
    public void onProgress(long totalSize, long downSize) {
        if (dialog != null) {
            dialog.setProgress((int) (downSize * 100 / totalSize));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        Log.i("duan==onstart", "onDestroy()");
//        EventBus.getDefault().unregister(this);
    }

    /**
     * 配置状态栏
     */
    public void configStatusBar() {
        int barColor = 0;
        int barColow1 = R.color.black;
        boolean isDarkFont = false;
        boolean isBarDarkIcon = false;
        boolean isNight = SPUtils.getInstance().getBoolean(Constant.NIGHT, false);
        if (!isNight) {
            isDarkFont = true;
            isBarDarkIcon = true;
            barColow1 = R.color.white;
        }
        ImmersionBar.with(this)
                .navigationBarColor(barColow1)
//                .keyboardEnable(true)
                .statusBarDarkFont(isDarkFont)
                .navigationBarDarkIcon(isBarDarkIcon)
                .init();
    }

    public void showerrortoast(String s) {
//        Toasty.error(this, s).show();
        Toasty.info(this, s).show();
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }


    public void showSuccessToast(String s) {
        Toasty.success(this, s).show();
//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void showFileDialog(String msg) {
        dialog = new ProgressDialog(this);
        dialog.setMessage(msg);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMax(100);
        dialog.show();
    }

    public void hideFileDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void closeLoadingDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public void showLoadingDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.setCancelable(false);
        dialog.setMessage(getString(R.string.loading_data));
        dialog.show();
    }

    public void showInfo(String msg) {
        Toasty.info(this, msg).show();
    }

    //如果返回true表示缺少权限
    public boolean lacksPermission(String[] permissions) {
        for (String permission : permissions) {
            //判断是否缺少权限，true=缺少权限
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    protected void skipActivity(Class<? extends Activity> className) {
        startActivity(new Intent(this, className));
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void showLoadingInfos(String title, String content, boolean style) {
        materialDialog = new MaterialDialog.Builder(this)
                .title(title)
                .content(content)
                .progress(true, 0)
                .progressIndeterminateStyle(style)
                .show();
        if (style) {
            materialDialog.setCancelable(false);
            materialDialog.setCanceledOnTouchOutside(false);
        }
    }

    public void hideLoadingInfos() {
        if (materialDialog != null) {
            materialDialog.hide();
            materialDialog.cancel();
            materialDialog.dismiss();
            materialDialog = null;
        }
    }

    public void dialogTip(String msg) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tip, null);
        TextView tvMsg = view.findViewById(R.id.tv_tip);
        TextView tvGotIt = view.findViewById(R.id.tv_got_it);
        Dialog dialog = new DialogView(this, view);
        tvMsg.setText(msg);
        tvGotIt.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void dialogTip2(String msg) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_tip2, null);
        TextView tvMsg = view.findViewById(R.id.tv_content);
        TextView tvGotIt = view.findViewById(R.id.tv_got_it);
        TextView tvDialogTitle = view.findViewById(R.id.tv_dialogtitle);
        tvDialogTitle.setText(getString(R.string.reminder));
        Dialog dialog = new DialogView(this, view);
        tvMsg.setText(msg);
        tvGotIt.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });
        dialog.show();
    }

    public void dialogConfimCancel(String msg, IComfirmCancel iComfirmCancel) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_cancel_tip, null);
        TextView tvMsg = view.findViewById(R.id.tv_content);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        Dialog dialog = new DialogView(this, view);
        tvMsg.setText(msg);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iComfirmCancel.setConfirmClick(dialog, v);
            }
        });
        tvCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }
}
