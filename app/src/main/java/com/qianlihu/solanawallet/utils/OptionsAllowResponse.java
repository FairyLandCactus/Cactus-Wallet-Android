package com.qianlihu.solanawallet.utils;

import android.annotation.TargetApi;
import android.webkit.WebResourceResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * author : Duan
 * date   : 2022/11/1417:08
 * desc   :
 * version: 1.0.0
 */
public class OptionsAllowResponse {

    static final SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy kk:mm:ss", Locale.US);

    @TargetApi(21)
    public static WebResourceResponse build(String url) {
        Date date = new Date();
        final String dateString = formatter.format(date);

        Map<String, String> headers = new HashMap<String, String>() {{
            put("Connection", "close");
            put("Content-Type", "text/plain");
            put("Date", dateString + " GMT");
            put("Access-Control-Allow-Origin", url);
            put("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
            put("Access-Control-Max-Age", "600");
            put("Access-Control-Allow-Credentials", "true");
            put("Access-Control-Allow-Headers", "accept, authorization, Content-Type");
            put("Via", "1.1 vegur");
        }};

        return new WebResourceResponse("text/plain", "UTF-8", 200, "OK", headers, null);
    }
}
