package com.aomygod.retrofit.eventbus;

import android.app.Activity;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EventBusActivity extends Activity {

    public final RequestManager requestManager = new RequestManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (requestManager != null) {
            requestManager.cancelAllTask();
            requestManager.clearAllTask();
        }
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessage(Object event) {}
}
