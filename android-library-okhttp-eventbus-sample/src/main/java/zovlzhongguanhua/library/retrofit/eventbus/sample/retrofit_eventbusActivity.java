package zovlzhongguanhua.library.retrofit.eventbus.sample;

import android.os.Bundle;
import android.view.View;

import com.aomygod.retrofit.eventbus.NetworkManager;
import com.aomygod.retrofit.eventbus.NetworkObject;
import com.aomygod.retrofit.eventbus.RequestObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

public class retrofit_eventbusActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_eventbus);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void doGet(View v) {

        RequestObject requestObject = new RequestObject();
        requestObject.setType(NetworkManager.Method.GET);
        requestObject.setUrl("http://httpbin.org/get");
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Tom");
        params.put("password", "123456");
        requestObject.setParams(params);
        requestObject.setEntity(new GetBean());
        NetworkManager.getInstance().doService(requestObject);
    }

    public void doPost(View v) {

        NetworkObject helper = new NetworkObject();
        RequestObject requestObject = new RequestObject();
        requestObject.setType(NetworkManager.Method.POST);
        requestObject.setUrl("http://httpbin.org/post");
        Map<String, Object> params = new HashMap<>();
        params.put("name", "Tom");
        params.put("password", "123456");
        requestObject.setParams(params);
        requestObject.setEntity(new PostBean());
        NetworkManager.getInstance().doService(requestObject);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(GetBean event) {
        showToast(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(PostBean event) {
        showToast(event);
    }
}
