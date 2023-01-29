package com.qianlihu.solanawallet.mvp.meeting_room.bean;

import com.qianlihu.solanawallet.bean.WalletBean;

import java.io.Serializable;

/**
 * author : Duan
 * date   : 2022/12/2011:39
 * desc   :
 * version: 1.0.0
 */
public class MemberBean implements Serializable {

    private String userId;
    private String roomId;
    private String name;
    private String header;
    private boolean isVideo;
    private WalletBean walletBean;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public WalletBean getWalletBean() {
        return walletBean;
    }

    public void setWalletBean(WalletBean walletBean) {
        this.walletBean = walletBean;
    }
}
