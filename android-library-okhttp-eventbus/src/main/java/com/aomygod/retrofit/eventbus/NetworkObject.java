package com.aomygod.retrofit.eventbus;

import android.util.Log;

/**
 * 网络请求帮助类
 */
public class NetworkObject {

    protected static boolean DEBUG = NetworkManager.getInstance().getDebug();
    private static final String TAG = NetworkManager.getInstance().getTAG();

    public void doService(RequestObject request) {
        doService(request, null);
    }

    /**
     * Android后台服务Service执行Http网络访问任务
     */
    public void doService(RequestObject request, ResponseListener listener) {
        if (DEBUG) Log.d(TAG, "request: " + request);
        if (DEBUG) Log.d(TAG, "listener: " + listener);
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
        if (DEBUG) Log.d(TAG, "request: " + request);
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
        if (DEBUG) Log.d(TAG, "request: " + request);
        if (request != null) {
            RequestTarget target = new RequestTarget();
            Object entity = target.postEntity(request);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

    public static class Method {
        public static final int GET = 1;
        public static final int POST = 2;
        public static final int UPLOAD = 3;
        public static final int DOWNLOAD = 4;
    }
}
