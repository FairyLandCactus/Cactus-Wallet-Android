package com.qianlihu.solanawallet.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.base.BaseActivity;
import com.qianlihu.solanawallet.base.BasePresenter;
import com.qianlihu.solanawallet.bean.AddTokenDB;
import com.qianlihu.solanawallet.bean.WalletBean;
import com.qianlihu.solanawallet.constant.CacheData;
import com.qianlihu.solanawallet.interfere.IFingerprint;
import com.qianlihu.solanawallet.interfere.IPassword;
import com.qianlihu.solanawallet.utils.ClickCopyUtils;
import com.qianlihu.solanawallet.utils.DialogView;
import com.qianlihu.solanawallet.utils.EnterPayPwd;
import com.qianlihu.solanawallet.utils.FingerprintPwd;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyWalletActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_wallet_address)
    TextView tvWalletAddress;
    @BindView(R.id.ll_view_mnemonics)
    LinearLayout llViewMnemonics;

    private WalletBean mBean;
    private String mPwd = "";
    private int position = 0;
    private String mWalletType = "0";

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected void initView() {
        position = getIntent().getIntExtra("position", 0);
//        List<WalletBean> list = LitePal.findAll(WalletBean.class);
        mBean = (WalletBean) getIntent().getSerializableExtra("wallet");
        tvTitle.setText(mBean.getName());
        tvWalletAddress.setText(mBean.getPubKey());
        mPwd = mBean.getPassword();
        mWalletType = mBean.getWalletType();
        if (mWalletType.equals("0") || TextUtils.isEmpty(mBean.getMnemonics())) {
            llViewMnemonics.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.iv_back, R.id.ll_modify_Wallet_Name, R.id.ll_view_private_key, R.id.ll_view_mnemonics, R.id.tv_wallet_address, R.id.btn_delete_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            //修改钱包名称
            case R.id.ll_modify_Wallet_Name:
                changeWalletName();
                break;
            //查看私钥
            case R.id.ll_view_private_key:
                FingerprintPwd.fingerprintVerify(this, new IFingerprint() {
                    @Override
                    public void onVerify(boolean isVerify) {
                        if (isVerify) {
                            walletPrivateKey();
                        } else {
                            EnterPayPwd.password(MyWalletActivity.this, 1, new IPassword() {
                                @Override
                                public void onPassword(String pwd) {
                                    if (!pwd.equals(mPwd)) {
                                        showInfo(getString(R.string.pwd_error));
                                        return;
                                    }
                                    walletPrivateKey();
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                        }
                    }

                    @Override
                    public void onClose() {

                    }
                });

                break;
            case R.id.ll_view_mnemonics:
                Intent intent = new Intent(MyWalletActivity.this, LookMnemonicsActivity.class);
                intent.putExtra("name", mBean.getName());
                intent.putExtra("mn", mBean.getMnemonics());
                FingerprintPwd.fingerprintVerify(this, new IFingerprint() {
                    @Override
                    public void onVerify(boolean isVerify) {
                        if (isVerify) {
                            startActivity(intent);
                        } else {
                            EnterPayPwd.password(MyWalletActivity.this, 1, new IPassword() {
                                @Override
                                public void onPassword(String pwd) {
                                    if (!pwd.equals(mPwd)) {
                                        showInfo(getString(R.string.pwd_error));
                                        return;
                                    }
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                        }
                    }

                    @Override
                    public void onClose() {

                    }
                });

                break;
            case R.id.tv_wallet_address:
                String address = tvWalletAddress.getText().toString().trim();
                ClickCopyUtils.copy(this, address);
                break;
            case R.id.btn_delete_wallet:
                deleteWalllet();
                break;
        }
    }

    private void changeWalletName() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_change_wallet_name, null);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        EditText etWalletName = view.findViewById(R.id.et_wallet_name);
        Dialog dialog = new DialogView(this, view);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String walletName = etWalletName.getText().toString().trim();
                if (TextUtils.isEmpty(walletName)) {
                    showInfo(getString(R.string.enter_wallet_name));
                    return;
                }
                List<WalletBean> list = LitePal.findAll(WalletBean.class);
                Log.i("duan==wallet", "修改前的数据===   " + list.toString());
                WalletBean bean = new WalletBean();
                bean.setName(walletName);
//                bean.update(position+1);
                bean.saveOrUpdate("pubKey = ?", mBean.getPubKey());
                List<WalletBean> list2 = LitePal.findAll(WalletBean.class);
                Log.i("duan==wallet", "修改后的数据===   " + list2.toString());
                tvTitle.setText(walletName);
                dialog.dismiss();
                showInfo(getString(R.string.modified_successfully));
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
            }
        });
        dialog.show();
    }

    private void walletPrivateKey() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_wallet_private_key, null);
        TextView tvClose = view.findViewById(R.id.tv_close);
        TextView tvCopy = view.findViewById(R.id.tv_copy);
        TextView tvPrivateKey = view.findViewById(R.id.tv_private_key);
        Dialog dialog = new DialogView(this, view);
        tvPrivateKey.setText(mBean.getPvaKey());
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickCopyUtils.copy(MyWalletActivity.this, mBean.getPvaKey());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void deleteWalllet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog_delete_wallet, null);
        TextView tvConfirmDelete = view.findViewById(R.id.tv_confirm_delete);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);

        tvConfirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LitePal.deleteAll(WalletBean.class, "pubKey = ?", mBean.getPubKey());
                LitePal.deleteAll(AddTokenDB.class, "walletAddress = ?", mBean.getPubKey());
                dialog.dismiss();
//                CacheData.shared().updateWalletInfoFlag = 67;
                if (mWalletType.equals("0")) {
                    CacheData.shared().applyFlag = 67;
                } else {
                    CacheData.shared().saveFlag = 67;
                }
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        tvCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
    }
}