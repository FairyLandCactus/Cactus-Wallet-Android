package com.qianlihu.solanawallet.wallet;

import com.qianlihu.solanawallet.wc.Wallet;

import java.math.BigInteger;

/**
 * author : Duan
 * date   : 2022/11/1010:11
 * desc   :
 * version: 1.0.0
 */
public class Transfcation {

    private String pik;
    private String chain;
    private Wallet from;
    private String toAddress;
    private BigInteger subunitAmount;
    private BigInteger gasPrice;
    private BigInteger gasLimit;
    private long nonce;
    private byte[] data;
    private long chainId;

    public Transfcation(String pik,
                        String chain,
                        Wallet from,
                        String toAddress,
                        BigInteger subunitAmount,
                        BigInteger gasPrice,
                        BigInteger gasLimit,
                        long nonce,
                        byte[] data,
                        long chainId) {

        this.pik = pik;
        this.chain = chain;
        this.from = from;
        this.toAddress = toAddress;
        this.subunitAmount = subunitAmount;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.nonce = nonce;
        this.data = data;
        this.chainId = chainId;

    }

    public String getPik() {
        return pik;
    }

    public String getChain() {
        return chain;
    }

    public Wallet getFrom() {
        return from;
    }

    public String getToAddress() {
        return toAddress;
    }

    public BigInteger getSubunitAmount() {
        return subunitAmount;
    }

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public BigInteger getGasLimit() {
        return gasLimit;
    }

    public long getNonce() {
        return nonce;
    }

    public byte[] getData() {
        return data;
    }

    public long getChainId() {
        return chainId;
    }
}
