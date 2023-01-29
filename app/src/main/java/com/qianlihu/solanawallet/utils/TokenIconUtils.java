package com.qianlihu.solanawallet.utils;

import com.qianlihu.solanawallet.R;

/**
 * author : Duan
 * date   : 2022/6/210:26
 * desc   :
 * version: 1.0.0
 */
public class TokenIconUtils {

    public static Integer icon(String name) {
        int error = R.mipmap.icon_unknown;
        if (name.equals("IUS")) {
            error = R.mipmap.transfer_accounts_ius;
        } else if (name.equals("SOL")) {
            error = R.mipmap.icon_solana;
        } else if (name.equals("BNB")) {
            error = R.mipmap.icon_binance_logo;
        } else if (name.equals("IUX")) {
            error = R.mipmap.icon_iux;
        }else if (name.equals("IVY")) {
            error = R.mipmap.icon_ivy;
        }else if (name.equals("IVE")) {
            error = R.mipmap.icon_ive;
        }  else if (name.equals("WSOL")) {
            error = R.mipmap.icon_solana;
        }else if (name.equals("USDT")) {
            error = R.mipmap.icon_usdt;
        }else if (name.equals("USDC")) {
            error = R.mipmap.icon_usdc;
        }else if (name.equals("TAE")) {
            error = R.mipmap.icon_tae;
        }else if (name.equals("TEST")) {
            error = R.mipmap.icon_token_test;
        }else if (name.equals("ETH")) {
            error = R.mipmap.icon_eth;
        }
        return error;
    }
}
