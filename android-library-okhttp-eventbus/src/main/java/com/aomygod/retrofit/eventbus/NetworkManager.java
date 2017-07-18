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

    public NetworkManager setmContext(Context context) {
        this.mContext = context;
        if (mDebug) Log.d(TAG, "mContext: " + mContext);
        return this;
    }
}
