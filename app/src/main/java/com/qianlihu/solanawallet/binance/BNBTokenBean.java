package com.qianlihu.solanawallet.binance;

/**
 * author : Duan
 * date   : 2022/3/318:19
 * desc   :
 * version: 1.0.0
 */
public class BNBTokenBean {

    /**
     * name : ETH
     * dec : Binance-Peg Ethereum Token
     * contractAdd : 0x2170Ed0880ac9A755fd29B2688956BD959F933F8
     * logoUrl : https://bscscan.com/token/images/ethereum_32.png
     * decimals : 18
     */

    private String name;
    private String dec;
    private String contractAdd;
    private String logoUrl;
    private int decimals;
    private Integer select = 0;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public String getContractAdd() {
        return contractAdd;
    }

    public void setContractAdd(String contractAdd) {
        this.contractAdd = contractAdd;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public Integer getSelect() {
        return select;
    }

    public void setSelect(Integer select) {
        this.select = select;
    }
}
