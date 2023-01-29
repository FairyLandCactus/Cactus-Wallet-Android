package com.qianlihu.solanawallet.event;

/**
 * author : Duan
 * date   : 2022/4/1510:25
 * desc   : 转账记录更新条数事件
 * version: 1.0.0
 */
public class DataUpdateEvent {

    private int flag;
    private int total;

    public DataUpdateEvent(int flag, int total) {
        this.flag = flag;
        this.total = total;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
