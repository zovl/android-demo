package com.aomygod.retrofit.eventbus;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 功能：URL工具
 */
class URLUtil {

    /**
     * 功能：URL是否合法
     */
    public static boolean isURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }
    }
}
