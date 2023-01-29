package com.qianlihu.solanawallet.api;

import com.google.gson.Gson;

/**
 * author : Duan
 * date   : 2021/6/315:23
 * desc   : 解析数据
 * version: 1.0.0
 */
public class JsonData {

    public static Object jsonFromData(String jsonStr, Class<?> object) {
        Gson gson = new Gson();
        Object jsonData = gson.fromJson(jsonStr, object);
        return jsonData;
    }
}
