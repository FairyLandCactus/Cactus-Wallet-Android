package com.qianlihu.solanawallet.event;

/**
 * author : Duan
 * date   : 2021/12/1615:54
 * desc   :
 * version: 1.0.0
 */
public class ErrorEvent {
    private int code;
    private String message;
    public ErrorEvent(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
