package com.aomygod.retrofit.eventbus;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 网络请求服务类
 */
public class RequestService extends Service {

    protected static boolean DEBUG = NetworkManager.getInstance().getDebug();
    private static final String TAG = NetworkManager.getInstance().getTAG();

    /**
     * 启动服务
     */
    public static void startRequestService() {
        startRequestService(null);
    }

    /**
     * 启动服务（网络访问）
     */
    public static void startRequestService(RequestObject requestObject) {
        if (DEBUG) Log.d(TAG,  "RequestService start!");
        Context context = NetworkManager.getInstance().getContext();
        Intent intent = new Intent(context, RequestService.class);
        if (requestObject != null) {
            intent.putExtra("requestObject", requestObject);
        }
        try {
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止服务
     */
    public static void stopRequestService() {
        if (DEBUG) Log.d(TAG,  "RequestService stop!");
        Context context = NetworkManager.getInstance().getContext();
        Intent intent = new Intent(context, RequestService.class);
        try {
            context.stopService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------------------------------------

    /**
     * 网络访问
     */
    private void request(RequestObject event) {
        if (event != null) {
            NetworkObject helper = new NetworkObject();
            helper.doExecutor(event, null);
        }
    }

    // ---------------------------------------------------------------------------------------

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            RequestObject requestObject = null;
            try {
                requestObject = (RequestObject) intent.getSerializableExtra("requestObject");
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (requestObject != null) {
                request(requestObject);
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
