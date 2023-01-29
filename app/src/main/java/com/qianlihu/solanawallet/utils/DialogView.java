package com.qianlihu.solanawallet.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.qianlihu.solanawallet.R;

/**
 * author : Duan
 * date   : 2021/12/311:54
 * desc   :
 * version: 1.0.0
 */
public class DialogView extends Dialog {

    public DialogView(@NonNull Context context, View view) {
        super(context, R.style.Dialog_Msg);
        iniView(view);
    }

    private void iniView(View view) {
        super.setContentView(view);
    }


}
