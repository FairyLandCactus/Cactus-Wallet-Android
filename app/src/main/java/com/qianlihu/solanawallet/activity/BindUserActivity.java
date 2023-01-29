package com.qianlihu.solanawallet.activity;

import android.Manifest;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.adapter.NoDataAdapter;
import com.qianlihu.solanawallet.adapter.SelectAddTokenWalletAdapter;
import com.qianlihu.solanawallet.api.HttpService;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.BoundBean;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.Constant;
import com.qianlihu.solanawallet.utils.MyUtils;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.king.zxing.CaptureActivity.KEY_RESULT;

public class BindUserActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_invite_id)
    EditText etInviteId;
    @BindView(R.id.ll_invite_id)
    LinearLayout llInviteId;
    @BindView(R.id.tv_device_id)
    TextView tvDeviceId;
    @BindView(R.id.et_address)
    EditText etAddress;

    private int bindType = 0;
    private String androidId = "";
    private List<WalletBean> mWalletList = new ArrayList<>();
    private String mChain = Constant.SOL_CHAIN;
    private String mWalletType = "0";

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_user;
    }

    @Override
    protected void initView() {
        bindType = getIntent().getIntExtra("bindType", 0);
        androidId = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        tvDeviceId.setText(androidId);
        if (bindType == 0) {
            llInviteId.setVisibility(View.GONE);
            tvTitle.setText(getString(R.string.bind_wallet));
        } else {
            tvTitle.setText(getString(R.string.invite_bind));
        }
        mWalletList = LitePal.where("walletType = ? and mainChain = ? and select = ?", mWalletType, mChain, "1").find(WalletBean.class);
        if (mWalletList.size() == 0) {
            mWalletList = LitePal.where("walletType = ? and mainChain = ?", mWalletType, mChain).find(WalletBean.class);
        }
        if (mWalletList.size() > 0) {
            etAddress.setText(mWalletList.get(0).getPubKey());
        }
    }

    @Override
    protected void initData() {

    }

    private String[] writePermissions = new String[]{Manifest.permission.CAMERA};

    @OnClick({R.id.iv_back, R.id.btn_bind, R.id.iv_select, R.id.iv_scan_qr})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_bind:
                if (bindType == 0) {
                    mobileBoundWallet();
                } else {
                    inviteBound();
                }
                break;
            case R.id.iv_select:
                selectWallet();
                break;
            case R.id.iv_scan_qr:
                if (lacksPermission(writePermissions)) {
                    ActivityCompat.requestPermissions(this, writePermissions, 0);
                    return;
                }
                Intent intent1 = new Intent(this, QRActivity.class);
                startActivityForResult(intent1, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String result = data.getStringExtra(KEY_RESULT);
            String qrUrl = "http://download.ius.finance/download/app_download.html";
            if (result.contains(qrUrl)) {
                if (result.contains("#")) {
                    String str1 = result.substring(0, result.indexOf("#"));
                    String str2 = result.substring(str1.length() + 1, result.length());
                    if (str2.contains("=")) {
                        String str4 = str2.substring(0, str2.indexOf("="));
                        String phoneId = str2.substring(str4.length() + 1);
                        etInviteId.setText(phoneId);
                    }
                }
            } else {
                showInfo(getString(R.string.error_in_invitation_QR_code));
            }
        }
    }

    private void selectWallet() {
        mWalletList = LitePal.where("walletType = ? and mainChain = ?", mWalletType, mChain).find(WalletBean.class);
        if (mWalletList.size() == 0) {
            showInfo(getString(R.string.no_wallet_data));
            return;
        }
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_select_add_walldet, null, false);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        RecyclerView rvWallet = view.findViewById(R.id.rv_wallet);
        rvWallet.setLayoutManager(new LinearLayoutManager(this));
        if (mWalletList.size() > 0) {
            SelectAddTokenWalletAdapter walletAdapter = new SelectAddTokenWalletAdapter(mWalletList);
            rvWallet.setAdapter(walletAdapter);
            walletAdapter.setOnItemClickListener((adapter, view1, position) -> {
                dialog.dismiss();
                etAddress.setText(mWalletList.get(position).getPubKey());
            });
        } else {
            rvWallet.setAdapter(new NoDataAdapter(Arrays.asList(getString(R.string.no_wallet))));
        }
        ivClose.setOnClickListener(v -> dialog.dismiss());

        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }

    private void mobileBoundWallet() {
        String walletAdddress = etAddress.getText().toString().trim();

        if (TextUtils.isEmpty(walletAdddress)) {
            runOnUiThread(() -> showInfo(getString(R.string.hint_wallet_address)));
            return;
        }
        if (walletAdddress.length() < 40) {
            runOnUiThread(() -> showInfo(getString(R.string.incorrect_input)));
            return;
        }
        if (walletAdddress.contains("0") || walletAdddress.contains("l") || walletAdddress.contains("O") || walletAdddress.contains("I")) {
            runOnUiThread(() -> showInfo(getString(R.string.incorrect_input)));
            return;
        }

        String avatar = MyUtils.md5("Cactus99" + androidId + "66Yuehu");
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("deviceUniqueId", androidId);
        map.put("walletAddress", walletAdddress);
        map.put("avatar", avatar);
        String url = "http://ioyapi.wivyswap.com/api/user/BindingWalletAddress";
        runOnUiThread(() -> showLoadingInfos("", getString(R.string.submitting_data), false));
        HttpService.post(url, map, true, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("duan==bound", "请求失败===   " + e.getMessage());
                runOnUiThread(() -> {
                    hideLoadingInfos();
                    showInfo(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                Log.i("duan==bound", "请求成功===   " + body);
                Gson gson = new Gson();
                runOnUiThread(() -> {
                    hideLoadingInfos();
                    if (!TextUtils.isEmpty(body)) {
                        if (!body.startsWith("{") && !body.endsWith("}")) {
                            showInfo(getString(R.string.request_abnormal));
                            return;
                        }
                        BoundBean bean = gson.fromJson(body, BoundBean.class);
                        if (bean != null) {
                            if (bean.getCode() == 200) {
                                showSuccessToast(bean.getMsg());
                                SPUtils.getInstance().put(Constant.IS_BOUND_WALLET, true);
                                Intent intent = new Intent(BindUserActivity.this, WalletConnect3Activity.class);
                                intent.putExtra(Constant.WEB_TITLE, "");
                                intent.putExtra(Constant.WEB_URL, "http://ive.wivyswap.com/");
                                startActivity(intent);
                                finish();
                            } else {
                                showInfo(bean.getMsg());
                            }
                        } else {
                            showInfo(getString(R.string.request_abnormal));
                        }
                    } else {
                        showInfo(getString(R.string.request_abnormal));
                    }
                });
            }
        });
    }

    private void inviteBound() {
        String walletAdddress = etAddress.getText().toString().trim();
        String inviteID = etInviteId.getText().toString().trim();

        if (TextUtils.isEmpty(inviteID)) {
            runOnUiThread(() -> showInfo(getString(R.string.hint_invite_mobile_id)));
            return;
        }
        if (TextUtils.isEmpty(walletAdddress)) {
            runOnUiThread(() -> showInfo(getString(R.string.hint_wallet_address)));
            return;
        }
        if (walletAdddress.length() < 40) {
            runOnUiThread(() -> showInfo(getString(R.string.incorrect_input)));
            return;
        }
        if (walletAdddress.contains("0") || walletAdddress.contains("l") || walletAdddress.contains("O") || walletAdddress.contains("I")) {
            runOnUiThread(() -> showInfo(getString(R.string.incorrect_input)));
            return;
        }

        String avatar = MyUtils.md5("Cactus99" + androidId + "66Yuehu");
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("deviceUniqueId", androidId);
        map.put("invitationDeviceUniqueId", inviteID);
        map.put("avatar", avatar);
        String url = "http://ioyapi.wivyswap.com/api/user/BindingInvite";
        runOnUiThread(() -> showLoadingInfos("", getString(R.string.submitting_data), false));
        HttpService.post(url, map, true, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("duan==bound", "请求失败===   " + e.getMessage());
                runOnUiThread(() -> {
                    hideLoadingInfos();
                    showInfo(e.getMessage());
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String body = response.body().string();
                Log.i("duan==bound", "请求成功===   " + body);
                Gson gson = new Gson();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingInfos();
                        if (!TextUtils.isEmpty(body)) {
                            if (!body.startsWith("{") && !body.endsWith("}")) {
                                showInfo(getString(R.string.request_abnormal));
                                return;
                            }
                            BoundBean bean = gson.fromJson(body, BoundBean.class);
                            if (bean != null) {
                                if (bean.getCode() == 200) {
                                    showSuccessToast(bean.getMsg());
                                    finish();
                                } else {
                                    showInfo(bean.getMsg());
                                }
                            } else {
                                showInfo(getString(R.string.request_abnormal));
                            }
                        } else {
                            showInfo(getString(R.string.request_abnormal));
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (lacksPermission(writePermissions)) {
                finish();
            }
        }
    }
}