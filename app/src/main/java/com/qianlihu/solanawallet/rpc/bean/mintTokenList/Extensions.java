/**
  * Copyright 2021 bejson.com 
  */
package com.qianlihu.solanawallet.rpc.bean.mintTokenList;

import java.io.Serializable;

/**
 * Auto-generated: 2021-07-29 15:43:14
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Extensions implements Serializable {


    /**
     * address : 0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c
     * assetContract : https://bscscan.com/address/0xbb4cdb9cbd36b01bd1cbaebf2de08d9173bc095c
     * bridgeContract : https://bscscan.com/address/0xB6F6D86a8f9879A9c87f643768d9efc38c1Da6E7
     * coingeckoId : binance-coin
     * serumV3Usdc : 4UPUurKveNEJgBqJzqHPyi8DhedvpYsMXi7d43CjAg2f
     * serumV3Usdt : FjbKNZME5yVSC1R3HJM99kB3yir3q3frS5MteMFD72sV
     */

    private String address;
    private String assetContract;
    private String bridgeContract;
    private String coingeckoId;
    private String serumV3Usdc;
    private String serumV3Usdt;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAssetContract() {
        return assetContract;
    }

    public void setAssetContract(String assetContract) {
        this.assetContract = assetContract;
    }

    public String getBridgeContract() {
        return bridgeContract;
    }

    public void setBridgeContract(String bridgeContract) {
        this.bridgeContract = bridgeContract;
    }

    public String getCoingeckoId() {
        return coingeckoId;
    }

    public void setCoingeckoId(String coingeckoId) {
        this.coingeckoId = coingeckoId;
    }

    public String getSerumV3Usdc() {
        return serumV3Usdc;
    }

    public void setSerumV3Usdc(String serumV3Usdc) {
        this.serumV3Usdc = serumV3Usdc;
    }

    public String getSerumV3Usdt() {
        return serumV3Usdt;
    }

    public void setSerumV3Usdt(String serumV3Usdt) {
        this.serumV3Usdt = serumV3Usdt;
    }
}