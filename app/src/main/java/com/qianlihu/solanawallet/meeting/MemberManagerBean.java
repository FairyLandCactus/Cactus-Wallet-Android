package com.qianlihu.solanawallet.meeting;

/**
 * author : Duan
 * date   : 2022/12/1316:51
 * desc   :
 * version: 1.0.0
 */
public class MemberManagerBean {

    private String userName;
    private String userId;
    private String member;
    private boolean isCheck;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
