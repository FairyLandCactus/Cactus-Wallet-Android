package com.qianlihu.solanawallet.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.qianlihu.solanawallet.R;


public class UpdateDialog extends Dialog implements View.OnClickListener {

    private View view;
    private TextView tv_title;
    private TextView tv_update_info;
    private Button btn_update;
    private AppCompatImageView iv_close;

    private Callback callback;
    private String title;
    private String content;
    private String versionName;
    private String apkUrl;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public UpdateDialog(@NonNull Context context, String title, String versionName, String content, String apkUrl) {
        super(context);
        this.title = title;
        this.versionName = versionName;
        this.content = content;
        this.apkUrl = apkUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(getContext()).inflate(R.layout.update_dialog, null);
        setCanceledOnTouchOutside(true);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Window window = this.getWindow();
        window.setContentView(view, params);
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.6f;
        windowParams.width = windowParams.WRAP_CONTENT;
        windowParams.height = windowParams.WRAP_CONTENT;
        window.setAttributes(windowParams);
        View decorView = this.getWindow().getDecorView();
        decorView.setPadding(0, 0, 0, 0);
        decorView.setBackgroundColor(Color.TRANSPARENT);
        dismiss();
        initView();
    }

    private void initView() {
        tv_title = view.findViewById(R.id.tv_title);
        tv_update_info = view.findViewById(R.id.tv_update_info);
        btn_update = view.findViewById(R.id.btn_update);
        iv_close = view.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        btn_update.setOnClickListener(this);

        tv_title.setText(title);
        tv_update_info.setText(content + "\n" + String.format(getContext().getString(R.string.is_upgrade_version), versionName));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_update:
                callback.update(apkUrl);
                break;
        }
    }

    public interface Callback {
        void update(String apkUrl);
    }
}
