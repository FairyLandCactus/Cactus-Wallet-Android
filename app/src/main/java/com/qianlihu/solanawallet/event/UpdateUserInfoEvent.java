package com.qianlihu.solanawallet.event;

/**
 * author : Duan
 * date   : 2022/1/1015:33
 * desc   : 更新用户信息
 * version: 1.0.0
 */
public class UpdateUserInfoEvent {

    private String puk;

    public UpdateUserInfoEvent(String puk) {
        this.puk = puk;
    }

    public String getPuk() {
        return puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }
}
