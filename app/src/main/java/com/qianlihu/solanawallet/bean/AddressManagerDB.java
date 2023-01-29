package com.qianlihu.solanawallet.bean;

import org.litepal.crud.LitePalSupport;

/**
 * author : Duan
 * date   : 2022/1/1710:53
 * desc   :
 * version: 1.0.0
 */
public class AddressManagerDB extends LitePalSupport {

    private String name;
    private String address;
    private String myAddress;
    private String mainChain;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMyAddress() {
        return myAddress;
    }

    public void setMyAddress(String myAddress) {
        this.myAddress = myAddress;
    }

    public String getMainChain() {
        return mainChain;
    }

    public void setMainChain(String mainChain) {
        this.mainChain = mainChain;
    }

    @Override
    public String toString() {
        return "AddressManagerDB{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", myAddress='" + myAddress + '\'' +
                ", mainChain='" + mainChain + '\'' +
                '}';
    }
}
