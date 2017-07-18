package zovlzhongguanhua.library.retrofit.eventbus.sample;

import android.app.Application;

import com.aomygod.retrofit.eventbus.NetworkManager;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        NetworkManager.getInstance().setmContext(this)
                .setDebug(true);
    }
}
