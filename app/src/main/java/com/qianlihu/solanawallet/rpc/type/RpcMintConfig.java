package com.qianlihu.solanawallet.rpc.type;

import com.squareup.moshi.Json;

/**
 * author : Duan
 * date   : 2021/12/2314:18
 * desc   :
 * version: 1.0.0
 */
public class RpcMintConfig {

    @Json(name = "mint")
    private String mint;

    public RpcMintConfig(String mint) {
        this.mint = mint;
    }

    public String getMint() {
        return mint;
    }
}
