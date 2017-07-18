package com.aomygod.retrofit.eventbus;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Request;
import okhttp3.Response;

public class RequestUtil {

    protected static boolean DEBUG = NetworkManager.getInstance().getDebug();
    private static final String TAG = NetworkManager.getInstance().getTAG();

    // ----------------------------------------------------------------------------

    public static String getString(String url, Map<String, Object> params) {
        InputStream inputStream = getStream(url, params);
        return StreamUtil.toStringUTF8(inputStream);
    }

    public static String getString(String url) {
        return getString(url, null);
    }

    public static Bitmap getBitmap(String url, Map<String, Object> params) {
        InputStream inputStream = getStream(url, params);
        return StreamUtil.toBitmap(inputStream);
    }

    public static Bitmap getBitmap(String url) {
        return getBitmap(url, null);
    }

    public static InputStream getStream(String url) {
        return getStream(url, null);
    }

    public static InputStream getStream(String url, Map<String, Object> params) {
        if (DEBUG) Log.d(TAG, "url: " + url);
        if (DEBUG) Log.d(TAG, "params: " + params);
        if (url == null)
            return null;
        if (!URLUtil.isURL(url))
            return null;
        if (params != null && params.size() > 0) {
            url = Util.getNewUrl(url, params);
        }
        if (DEBUG) Log.d(TAG, "newUrl: " + url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = OkClient.getInstance().getOkHttpClient().newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) {
            InputStream is  = response.body().byteStream();
            if (is != null) {
                return is;
            }
        }
        return null;
    }
}
