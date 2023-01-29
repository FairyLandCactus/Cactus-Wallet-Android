package com.qianlihu.solanawallet.interfere;

/**
 * author : Duan
 * date   : 2022/10/219:58
 * desc   :
 * version: 1.0.0
 */
public interface IDecimals {

    void onSetDecimal(int decimal);

    void onError(int decimal);
}
