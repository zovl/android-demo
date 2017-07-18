package com.aomygod.retrofit.eventbus;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Http客户端接口
 */
interface IRequestClient {

    /**
     * OkHttpClient执行网络请求
     */
    Response execute(Request request) throws IOException;
}
