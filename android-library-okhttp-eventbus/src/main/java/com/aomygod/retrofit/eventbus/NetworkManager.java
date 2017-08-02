package com.aomygod.retrofit.eventbus;

import android.content.Context;
import android.util.Log;

public class NetworkManager {

    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if (instance == null) {
            synchronized (NetworkManager.class) {
                if (instance == null) {
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }

    private NetworkManager() {}

    // -----------------------------------------------------------------

    private final String TAG = "NetworkManager";
    private boolean mDebug = false;
    private Context mContext;

    public String getTAG() {
        return TAG;
    }

    public boolean getDebug() {
        return mDebug;
    }

    public Context getContext() {
        if (mContext == null) {
            throw new NullPointerException("Error: mContext is null!");
        }
        return mContext;
    }

    public NetworkManager setDebug(boolean debug) {
        this.mDebug = debug;
        if (mDebug) Log.d(TAG, "mDebug: " + mDebug);
        return this;
    }

    public NetworkManager setContext(Context context) {
        this.mContext = context;
        if (mDebug) Log.d(TAG, "mContext: " + mContext);
        return this;
    }

    public void doService(RequestObject request) {
        doService(request, null);
    }

    // -----------------------------------------------------------------

    /**
     * Android后台服务Service执行Http网络访问任务
     */
    public void doService(RequestObject request, ResponseListener listener) {
        if (mDebug) Log.d(TAG, "request: " + request);
        if (mDebug) Log.d(TAG, "listener: " + listener);
        if (request != null) {
            if (listener != null) {
                ResponseObserver.getInstance().addListener(request.getNewUrl(), listener);
            }
            RequestService.startRequestService(request);
        }
    }

    /**
     * Java线程池ExecutorService执行Http网络访问任务
     */
    public void doExecutor(RequestObject request, ResponseListener listener) {
        if (mDebug) Log.d(TAG, "request: " + request);
        if (request != null) {
            if (listener != null) {
                ResponseObserver.getInstance().addListener(request.getNewUrl(), listener);
            }
            EventBusActivity activity = request.getActivity();
            if (activity != null) {
                RequestRunnable runnable = activity.requestManager.createTask(request);
                RequestExecutor.execute(runnable);
            } else {
                RequestRunnable runnable = new RequestRunnable(request);
                RequestExecutor.execute(runnable);
            }
        }
    }

    /**
     * 执行Http网络访问任务并返回实体
     */
    public Object postEntity(RequestObject request) {
        if (mDebug) Log.d(TAG, "request: " + request);
        if (request != null) {
            RequestTarget target = new RequestTarget();
            Object entity = target.postEntity(request);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

    // -----------------------------------------------------------------

    public static class Method {
        public static final int GET = 1;
        public static final int POST = 2;
        public static final int UPLOAD = 3;
        public static final int DOWNLOAD = 4;
    }
}
