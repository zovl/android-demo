package zovlzhongguanhua.library.retrofit.rxjava.sample;

import android.app.Application;

import com.aomygod.retrofit.rxjava.NetworkCenter;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkCenter.getInstance()
                .setContext(this)
                .setDebug(true)
                .setHostUrl("http://httpbin.org/");
    }
}
