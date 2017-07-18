package com.aomygod.retrofit.rxjava;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 功能：URL工具
 */
class Util {

    private static final String TAG = NetworkCenter.getInstance().getTag();
    private static final boolean DEBUG = NetworkCenter.getInstance().getDebug();

    // ---------------------------------------------------------------------------------

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isNull(String string) {
        if (string == null || string.length() == 0 || string.equals("null")) {
            return true;
        }
        return false;
    }

    /**
     * 功能：URL是否合法
     */
    public static boolean isURL(String url) {
        try {
            URL mURL = new URL(url);
            if (DEBUG) Log.d(TAG, "isURL: url=" + url + " is true");
            return true;
        } catch (MalformedURLException e) {
            if (DEBUG) Log.d(TAG, "isURL: url=" + url + " is false");
            return false;
        }
    }

    /**
     * 功能：Map按key进行排序
     */
    public static Map<String, Object> sortMap(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<>(new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    /***
     * 生成32位md5码
     */
    public static String md5(String inStr){
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(inStr.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);

        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 拼接Url和参数
     */
    public static String urlParams(final String url, final Map<String, Object> params) {
        if (url == null || url.length() == 0 || url.equals("null")) {
            throw new NullPointerException();
        }
        StringBuffer buffer = new StringBuffer(url);
        if (params != null && params.size() > 0) {
            if (url.contains("?")) {
                for (String key : params.keySet()) {
                    String value = (String) params.get(key);
                    buffer.append("&" + key + "=" + value);
                }
            } else {
                boolean flag = false;
                for (String key : params.keySet()) {
                    String value = (String) params.get(key);
                    if (!flag) {
                        buffer.append("?" + key + "=" + value);
                        flag = true;
                    } else {
                        buffer.append("&" + key + "=" + value);
                    }
                }
            }
        }
        return buffer.toString();
    }
}
