package zovlzhongguanhua.api.application;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

public class MainApplication extends Application {

    private final String TAG = this.getClass().getSimpleName();

        @Override
        public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        registerComponentCallbacks(componentCallbacks);
        registerOnProvideAssistDataListener(onProvideAssistDataListener);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            Log.d(TAG, "onActivityCreated: " + activity + ":" + savedInstanceState);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            Log.d(TAG, "onActivityStarted: " + activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            Log.d(TAG, "onActivityResumed: " + activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Log.d(TAG, "onActivityPaused: " + activity);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            Log.d(TAG, "onActivityStopped: " + activity);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            Log.d(TAG, "onActivitySaveInstanceState: " + activity + ":" + outState);
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            Log.d(TAG, "onActivityDestroyed: " + activity);
        }
    };

    ComponentCallbacks componentCallbacks  =new ComponentCallbacks2() {
        @Override
        public void onTrimMemory(int level) {
            Log.d(TAG, "onTrimMemory: " + level);
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            Log.d(TAG, "onConfigurationChanged: " + newConfig);
        }

        @Override
        public void onLowMemory() {
            Log.d(TAG, "onTrimMemory: ");
        }
    };

    OnProvideAssistDataListener onProvideAssistDataListener = new OnProvideAssistDataListener() {
        @Override
        public void onProvideAssistData(Activity activity, Bundle data) {
            Log.d(TAG, "onProvideAssistData: " + activity + ":" + data);
        }
    };
}
