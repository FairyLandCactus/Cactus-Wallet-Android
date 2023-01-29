package com.qianlihu.solanawallet.wallet;

import com.qianlihu.solanawallet.bean.WalletBean;

/**
 * author : Duan
 * date   : 2022/12/1915:37
 * desc   :
 * version: 1.0.0
 */
public interface IPSelectWallet {

    void onSelectWallet(WalletBean bean);

    void onDismiss();
}
