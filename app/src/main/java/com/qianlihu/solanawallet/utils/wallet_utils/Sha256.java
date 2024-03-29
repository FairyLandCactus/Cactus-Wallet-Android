package com.qianlihu.solanawallet.utils.wallet_utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * author : Duan
 * date   : 2021/10/2715:14
 * desc   :
 * version: 1.0.0
 */
public class Sha256 {

    /**
     * SHA-256
     *
     * @param input input
     * @return sha256(input)
     */
    public static byte[] sha256(byte[] input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to find SHA-256", e);
        }
    }
        /**
         * 利用java原生的类实现SHA256加密
         *
         * @param str 参数拼接的字符串
         * @return
         */
        public static String getSHA256(String str) {
            MessageDigest messageDigest;
            String encodeStr = "";
            try {
                messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(str.getBytes("UTF-8"));
                encodeStr = byte2Hex(messageDigest.digest());
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return encodeStr;
        }

        /**
         * 将byte转为16进制
         *
         * @param bytes
         * @return
         */
        private static String byte2Hex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            String temp = null;
            for (byte aByte : bytes) {
                temp = Integer.toHexString(aByte & 0xFF);
                if (temp.length() == 1) {
                    // 1得到一位的进行补0操作
                    sb.append("0");
                }
                sb.append(temp);
            }
            return sb.toString();
        }

}
