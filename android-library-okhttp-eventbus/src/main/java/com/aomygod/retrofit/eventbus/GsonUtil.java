package com.aomygod.retrofit.eventbus;

import com.google.gson.Gson;

/**
 * JSON解析工具类
 */
class GsonUtil {

    public static final Gson GSON = new Gson();

    public static <T> T parse(String text, Class<T> clazz) {
        return GSON.fromJson(text, clazz);
    }

    public static String to(Object o) {
        return GSON.toJson(o);
    }
}
