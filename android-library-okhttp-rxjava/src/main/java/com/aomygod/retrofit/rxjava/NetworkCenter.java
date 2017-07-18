package com.aomygod.retrofit.rxjava;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * 网络访问中心
 */
public class NetworkCenter {

    private static NetworkCenter instance;

    public static NetworkCenter getInstance() {
        if (instance == null) {
            synchronized (NetworkCenter.class) {
                if (instance == null) {
                    instance = new NetworkCenter();
                }
            }
        }
        return instance;
    }

    private NetworkCenter() {}

    // -----------------------------------------------------------------

    private final String TAG = "NetworkCenter";
    private boolean mDebug = false;// 是否打印日志
    private Context mContext;
    private String mHostUrl;// 网络访问主域名；注意：完整的网络路径是(mHostUrl + lastUrl)，如果传入了newUrl，则会优先使用newUrl
    List<Interceptor> interceptors = new ArrayList<>();// 拦截器

    public String getTag() {
        return TAG;
    }
    
    public NetworkCenter setDebug(boolean debug) {
        this.mDebug = debug;
        if (mDebug) Log.d(TAG, "mDebug: " + mDebug);
        return this;
    }

    public NetworkCenter setContext(Context context) {
        this.mContext = context;
        if (mDebug) Log.d(TAG, "mContext: " + mContext);
        return this;
    }

    public NetworkCenter setHostUrl(String hostUrl) {
        this.mHostUrl = hostUrl;
        if (mDebug) Log.d(TAG, "mHostUrl: " + mHostUrl);
        return this;
    }

    public Context getContext() {
        if (mContext == null) {
            throw new NullPointerException("Error: mContext is null!");
        }
        return mContext;
    }

    public boolean getDebug() {
        return mDebug;
    }

    public String getHostUrl() {
        if (Util.isEmpty(mHostUrl) || !Util.isURL(mHostUrl)) {
            throw new NullPointerException("Error: mHostUrl is invalid!");
        }
        if (!mHostUrl.endsWith("/")) {
            mHostUrl = mHostUrl + "/";
        }
        return mHostUrl;
    }

    public NetworkCenter addInterceptor(Interceptor interceptor) {
        if (interceptor != null &&
                !interceptors.contains(interceptor)) {
            interceptors.add(interceptor);
            if (mDebug) Log.d(TAG, "interceptor: " + interceptor);
        }
        return this;
    }

    // -----------------------------------------------------------------

    public static NetworkObject newObject() {
        return NetworkObject.newInstance();
    }

    // -----------------------------------------------------------------

    public static void cancel(Object tag) {
        if (tag != null) {
            RetrofitManager.getInstance().cancelDisposable(tag);
        }
    }

    public static void cancelAll() {
        RetrofitManager.getInstance().cancelAllDisposable();
    }

    // -----------------------------------------------------------------

    public interface Method {
        int GET = 0;// HTTP GET 方法
        int POST = 1;// HTTP POST 方法
        int BODY = 12;// HTTP POST 上传实体类
    }
}