package com.qianlihu.solanawallet.mvp.create_room.bean;

/**
 * author : Duan
 * date   : 2022/12/2315:45
 * desc   :
 * version: 1.0.0
 */
public class CreateMeetingBody {

    private String languge;
    private String address;
    private String roomNo;
    private String roomName;
    private int num;
    private String userName;
    private String userHeader;

    public void setLanguge(String languge) {
        this.languge = languge;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserHeader(String userHeader) {
        this.userHeader = userHeader;
    }

    @Override
    public String toString() {
        return "CreateMeetingBody{" +
                "languge='" + languge + '\'' +
                ", address='" + address + '\'' +
                ", roomNo='" + roomNo + '\'' +
                ", roomName='" + roomName + '\'' +
                ", num=" + num +
                ", userName='" + userName + '\'' +
                ", userHeader='" + userHeader + '\'' +
                '}';
    }
}
