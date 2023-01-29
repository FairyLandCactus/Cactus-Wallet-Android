package com.qianlihu.solanawallet.bean;

/**
 * author : Duan
 * date   : 2022/3/816:44
 * desc   :
 * version: 1.0.0
 */
public class UploadTransferRecodeBean {

    private String from_address;
    private String to_address;
    private String record;
    private String coin;

    public String getFrom_address() {
        return from_address;
    }

    public void setFrom_address(String from_address) {
        this.from_address = from_address;
    }

    public String getTo_address() {
        return to_address;
    }

    public void setTo_address(String to_address) {
        this.to_address = to_address;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    @Override
    public String toString() {
        return "UploadTransferRecodeBean{" +
                "from_address='" + from_address + '\'' +
                ", to_address='" + to_address + '\'' +
                ", record='" + record + '\'' +
                ", coin='" + coin + '\'' +
                '}';
    }
}
