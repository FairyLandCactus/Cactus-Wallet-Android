package com.qianlihu.solanawallet.event;

/**
 * author : Duan
 * date   : 2022/4/2011:26
 * desc   :
 * version: 1.0.0
 */
public class ResumeEvent {

    private int flag;

    public ResumeEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
