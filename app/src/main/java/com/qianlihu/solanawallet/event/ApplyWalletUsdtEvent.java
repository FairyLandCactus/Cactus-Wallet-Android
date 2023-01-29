package com.qianlihu.solanawallet.event;

/**
 * author : Duan
 * date   : 2022/4/1218:22
 * desc   :
 * version: 1.0.0
 */
public class ApplyWalletUsdtEvent {

    private String last;

    public ApplyWalletUsdtEvent(String last) {
        this.last = last;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }
}
