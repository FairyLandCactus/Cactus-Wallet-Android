package com.qianlihu.solanawallet.event;

import android.content.Intent;

import androidx.annotation.Nullable;

/**
 * author : Duan
 * date   : 2022/11/3015:44
 * desc   :
 * version: 1.0.0
 */
public class HideMianTabEvent {

   private boolean isHideTab;

    public HideMianTabEvent(boolean isHideTab) {
        this.isHideTab = isHideTab;
    }

    public boolean isHideTab() {
        return isHideTab;
    }
}
