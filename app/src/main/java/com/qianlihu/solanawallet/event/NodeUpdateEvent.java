package com.qianlihu.solanawallet.event;

/**
 * author : Duan
 * date   : 2022/10/2818:27
 * desc   :
 * version: 1.0.0
 */
public class NodeUpdateEvent {

    private int status;
    private String address;

    public NodeUpdateEvent(int status, String address) {
        this.status = status;
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
