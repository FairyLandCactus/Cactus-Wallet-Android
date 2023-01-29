package com.qianlihu.solanawallet.event;

/**
 * author : Duan
 * date   : 2022/5/417:38
 * desc   :
 * version: 1.0.0
 */
public class WebInviteEvent {

    private boolean isInvite;

    public WebInviteEvent(boolean isInvite) {
        this.isInvite = isInvite;
    }

    public boolean isInvite() {
        return isInvite;
    }

    public void setInvite(boolean invite) {
        isInvite = invite;
    }
}
