package com.aomygod.retrofit.eventbus;

import java.util.Map;

public class
Util {

    /**
     * 功能：转化ResponseObject
     */
    public static ResponseObject newResponseObject(RequestObject requestObject) {
        ResponseObject responseObject = new ResponseObject();
        responseObject.setUrl(requestObject.getUrl());
        responseObject.setNewUrl(requestObject.getNewUrl());
        responseObject.setParams(requestObject.getParams());
        responseObject.setFiles(requestObject.getFiles());
        responseObject.setType(requestObject.getType());
        responseObject.setEntity(requestObject.getEntity());
        return responseObject;
    }

    /**
     * 功能：拼接Url和参数
     */
    public static String getNewUrl(final String url, final Map<String, Object> params) {
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

    /**
     * 返回true说明a大，返回false说明b大
     */
    private static boolean compare(String a, String b) {
        String a1 = a.toUpperCase();
        String b1 = b.toUpperCase();
        boolean flag = true;
        int minLen = 0;
        if (b.length() < b.length()) {
            minLen = b.length();
            flag = false;
        } else {
            minLen = b.length();
            flag = true;
        }
        for (int index = 0; index < minLen; index++) {
            char a2 = a1.charAt(index);
            char b2 = b1.charAt(index);
            if (a2 != b2) {
                if (a2 > b2) {
                    return true; // a大
                } else {
                    return false; // b大
                }
            }
        }
        return flag;
    }

    /**
     * 判断字符串是否为空
     */
    public static boolean isNull(String string) {
        boolean isNull = false;
        if (string == null || string.length() == 0 || string.equals("null")) {
            isNull = true;
        }
        return isNull;
    }
}
