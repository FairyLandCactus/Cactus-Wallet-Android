package com.qianlihu.solanawallet.base;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.SPUtils;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.DialogView;
import com.qianlihu.solanawallet.utils.LanguageUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

/**
 * author : Duan
 * date   : 2021/4/911:44
 * desc   :
 * version: 1.0.0
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements BaseView {

    public Context context;
    private ProgressDialog dialog;
    private MaterialDialog materialDialog;
    public String languageType = "zh_CN";
    protected P presenter;

    protected Unbinder unbinder;
    protected Context mContext;

    public abstract P createPresenter();

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract void initData();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
//        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = createPresenter();
        //初始化语言
        Locale locale = this.getResources().getConfiguration().locale;
        LanguageUtil.languageUpdate(getActivity(), locale);
        mContext = getActivity();
        context = getActivity();
        languageType = SPUtils.getInstance().getString(Constant.LANGUAGE_TYPE);
        initView();
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
//        EventBus.getDefault().unregister(this);
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

    public void showInfo(String msg) {
        Toasty.info(getActivity(), msg).show();
    }

    public void showerrortoast(String s) {
//        Toasty.error(getActivity(), s).show();
        Toasty.info(getActivity(), s).show();
//        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }


    public void showSuccessToast(String s) {
        Toasty.success(getActivity(), s).show();
//        Toast.makeText(mContext, s, Toast.LENGTH_SHORT).show();
    }

    public void showFileDialog(String msg) {
        dialog = new ProgressDialog(getContext());
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
            dialog = new ProgressDialog(getContext());
        }
        dialog.setCancelable(false);
        dialog.setMessage(getString(R.string.loading_data));
        dialog.show();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (materialDialog != null) {
            materialDialog.cancel();
            materialDialog.dismiss();
            materialDialog = null;
        }
    }

    //如果返回true表示缺少权限
    public boolean lacksPermission(String[] permissions) {
        for (String permission : permissions) {
            //判断是否缺少权限，true=缺少权限
            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    protected void skipActivity(Class<? extends Activity> className) {
        startActivity(new Intent(getActivity(), className));
    }
    public void showLoadingInfos(String title, String content, boolean style) {
        materialDialog = new MaterialDialog.Builder(getContext())
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_tip, null);
        TextView tvMsg = view.findViewById(R.id.tv_tip);
        TextView tvGotIt = view.findViewById(R.id.tv_got_it);
        Dialog dialog = new DialogView(getActivity(), view);
        tvMsg.setText(msg);
        tvGotIt.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

}
