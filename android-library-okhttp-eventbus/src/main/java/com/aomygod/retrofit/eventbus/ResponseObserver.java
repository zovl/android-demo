package com.aomygod.retrofit.eventbus;

import java.util.HashMap;
import java.util.Map;

class ResponseObserver {

    protected static boolean DEBUG = NetworkManager.getInstance().getDebug();
    private static final String TAG = NetworkManager.getInstance().getTAG();

    private static ResponseObserver instance;

    public static ResponseObserver getInstance() {
        if (instance == null) {
            synchronized (ResponseObserver.class) {
                if (instance == null) {
                    instance = new ResponseObserver();
                }
            }
        }
        return instance;
    }

    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------------------------------

    private Map<String, ResponseListener> listeners = new HashMap<>();

    public void addListener(String url, ResponseListener listener) {
        synchronized (instance) {
            if (url != null && listener != null && !listeners.containsKey(url)) {
                listeners.put(url, listener);
            }
        }
    }

    public void removeListener(String url) {
        synchronized (instance) {
            if (url != null && listeners.containsKey(url)) {
                listeners.remove(url);
            }
        }
    }

    public void notifyListener(String url, ResponseObject object) {
        if (url != null &&
                listeners != null &&
                listeners.containsKey(url)) {
            synchronized (instance) {
                ResponseListener listener = listeners.get(url);
                if (listener != null) {
                    listener.onResponse(object);
                }
            }
        }
        removeListener(url);
    }
}
