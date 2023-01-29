package com.qianlihu.solanawallet.interfere;

/**
 * author : Duan
 * date   : 2022/8/614:29
 * desc   :
 * version: 1.0.0
 */
public interface IFingerprint {
    void onVerify(boolean isVerify);

    void onClose();
}
