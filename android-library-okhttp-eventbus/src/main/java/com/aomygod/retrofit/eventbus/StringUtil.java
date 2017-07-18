package com.aomygod.retrofit.eventbus;

class StringUtil {

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
}
