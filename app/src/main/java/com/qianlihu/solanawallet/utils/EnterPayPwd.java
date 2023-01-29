package com.qianlihu.solanawallet.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.qianlihu.solanawallet.R;
import com.qianlihu.solanawallet.interfere.IPassword;
import com.qianlihu.solanawallet.view.PasswordView;

import es.dmoral.toasty.Toasty;

/**
 * author : Duan
 * date   : 2021/12/618:05
 * desc   : 输入密码
 * version: 1.0.0
 */
public class EnterPayPwd {

    private static String pwdFirst = "";

    public static void password(Context context, int flag, IPassword iPassword) {
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetEdit);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_password, null);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        TextView tvPwdTip = view.findViewById(R.id.tv_pwd_tip);
        PasswordView etPassword = view.findViewById(R.id.et_password);
        etPassword.requestFocus();
        ivClose.setOnClickListener(v -> {
            dialog.dismiss();
            pwdFirst = "";
            iPassword.onCancel();
        });

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                pwdFirst = "";
                iPassword.onCancel();
            }
        });

        dialog.setOnDismissListener(dialog1 -> pwdFirst = "");
        dialog.setOnCancelListener(dialog12 -> pwdFirst = "");
        if (flag == 1) {
            tvPwdTip.setText(context.getString(R.string.enter_wallet_pwd));
        } else if (flag == 3) {
            tvPwdTip.setText(context.getString(R.string.import_wallet_pwd));
        }
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
        //软键盘自动弹出
        etPassword.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwdOne = s.toString().trim();
                if (flag == 2 || flag == 3) {
                    if (pwdOne.length() == 6 && TextUtils.isEmpty(pwdFirst)) {
                        pwdFirst = pwdOne;
                        etPassword.setText("");
                        tvPwdTip.setText(context.getString(R.string.confirm_wallet_pwd));
                        return;
                    }
                    if (pwdOne.length() == 6 && pwdFirst.length() == 6) {
                        if (!pwdOne.equals(pwdFirst)) {
                            Toasty.info(context, context.getString(R.string.two_pwd_inconsistent)).show();
                            return;
                        }
                        MyUtils.hideKeyboard(etPassword);
                        iPassword.onPassword(pwdFirst);
                        pwdFirst = "";
                        dialog.dismiss();
                    }
                } else {
                    if (pwdOne.length() == 6) {
                        MyUtils.hideKeyboard(etPassword);
                        etPassword.setText("");
                        dialog.dismiss();
                        iPassword.onPassword(pwdOne);
                    }
                }
            }
        });
    }

    public static void passwordE(Context context, int flag, IPassword iPassword) {
        BottomSheetDialog dialog = new BottomSheetDialog(context, R.style.BottomSheetEdit);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog_password_keyboard, null);
        ImageView ivClose = view.findViewById(R.id.iv_close);
        TextView tvPwdTip = view.findViewById(R.id.tv_pwd_tip);
        PasswordView etPassword = view.findViewById(R.id.et_password);
        RecyclerView rvKeyboard = view.findViewById(R.id.rv_keyboard);
        ivClose.setOnClickListener(v -> {
            dialog.dismiss();
            pwdFirst = "";
        });

        dialog.setOnDismissListener(dialog1 -> pwdFirst = "");
        dialog.setOnCancelListener(dialog12 -> pwdFirst = "");
        if (flag == 1) {
            tvPwdTip.setText(context.getString(R.string.enter_wallet_pwd));
        } else if (flag == 3) {
            tvPwdTip.setText(context.getString(R.string.import_wallet_pwd));
        }
        dialog.setContentView(view);
        dialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        dialog.show();
        //软键盘自动弹出
        etPassword.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwdOne = s.toString().trim();
                if (flag == 2 || flag == 3) {
                    if (pwdOne.length() == 6 && TextUtils.isEmpty(pwdFirst)) {
                        pwdFirst = pwdOne;
                        etPassword.setText("");
                        tvPwdTip.setText(context.getString(R.string.confirm_wallet_pwd));
                        return;
                    }
                    if (pwdOne.length() == 6 && pwdFirst.length() == 6) {
                        if (!pwdOne.equals(pwdFirst)) {
                            Toasty.info(context, context.getString(R.string.two_pwd_inconsistent)).show();
                            return;
                        }
                        iPassword.onPassword(pwdFirst);
                        pwdFirst = "";
                        dialog.dismiss();
                    }
                } else {
                    if (pwdOne.length() == 6) {
                        etPassword.setText("");
                        dialog.dismiss();
                        iPassword.onPassword(pwdOne);
                    }
                }
            }
        });
    }
}
