package com.qianlihu.solanawallet.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * author : Duan
 * date   : 2022/10/2714:33
 * desc   : 切割工具类
 * version: 1.0.0
 */
public class SplitUtil {

    public static List<String> stringSplit(String str){
        List<String> lis = new ArrayList<>();
        if(str.indexOf(",") != -1){
            String[] split = str.split(",");
            for (int i = 0; i < split.length; i++) {
                String url = split[i];
                lis.add(url);
            }
        }else if(str.indexOf("，") != -1){
            String[] split = str.split("，");
            for (int i = 0; i < split.length; i++) {
                String url = split[i];
                lis.add(url);
            }
        }else{
            String[] split = str.split(",");
            for (int i = 0; i < split.length; i++) {
                String url = split[i];
                lis.add(url);
            }
        }
        return lis;
    }
}
