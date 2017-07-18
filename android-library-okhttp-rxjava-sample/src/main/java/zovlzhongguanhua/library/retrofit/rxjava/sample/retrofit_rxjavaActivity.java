package zovlzhongguanhua.library.retrofit.rxjava.sample;

import android.os.Bundle;
import android.view.View;

import com.aomygod.retrofit.rxjava.NetworkCenter;
import com.aomygod.retrofit.rxjava.NetworkSubscriber;
import com.google.gson.JsonObject;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class retrofit_rxjavaActivity extends BaseActivity {

    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_rxjava);
    }

    private void sample() {
        // 响应
        NetworkSubscriber subscriber = new NetworkSubscriber<GetBean>(GetBean.class) {

            @Override
            public void onNext(GetBean o) {
                showToast(o);
            }

            @Override
            public void onError(Throwable t) {
                showToast(t.getMessage());
            }

            @Override
            public void onComplete() {}
        };
        // Url参数
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("method", "login");
        // 表单参数
        Map<String, Object> formParams = new HashMap<>();
        formParams.put("name", "Tom");
        formParams.put("password", "123456");
        // 请求头
        Map<String, Object> headers = new HashMap<>();
        headers.put("Accept-Encoding", "gzip");
        // 请求体
        Object body;

        body = new String();// String
        body = new JsonObject();// GSON
        // 地址
        String lastUrl = "login";
        String newUrl = "http://www.aomygod.com/login";
        String tag = "tag-01";
        // 方法
        int method = NetworkCenter.Method.GET;

        // GET
        NetworkCenter.newObject()
                .setCacheSeconds(1000)
                .setTransformer(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .setTag(tag)
                .setMethod(NetworkCenter.Method.GET)
                .setLastUrl(lastUrl)
                .setNewUrl(newUrl)
                .setQueryParams(queryParams)
                .setHeaders(headers)
                .setSubscriber(subscriber)
                .doExecute();

        // POST
        NetworkCenter.newObject()
                .setCacheSeconds(1000)
                .setTransformer(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .setTag(tag)
                .setMethod(NetworkCenter.Method.POST)
                .setLastUrl(lastUrl)
                .setNewUrl(newUrl)
                .setQueryParams(queryParams)
                .setFormParams(formParams)
                .setHeaders(headers)
                .setSubscriber(subscriber)
                .doExecute();

        NetworkCenter.getInstance().cancel(tag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private NetworkSubscriber subscriberGet = new NetworkSubscriber<GetBean>(GetBean.class) {

        @Override
        public void onNext(GetBean o) {
            showToast(o);
        }

        @Override
        public void onError(Throwable t) {
            showToast(t.getMessage());
        }

        @Override
        public void onComplete() {}
    };

    private NetworkSubscriber subscriberPost = new NetworkSubscriber<PostBean>(PostBean.class) {

        @Override
        public void onNext(PostBean o) {
            showToast(o);
        }

        @Override
        public void onError(Throwable t) {
            showToast(t.getMessage());
        }

        @Override
        public void onComplete() {}
    };

    public void doGet(View v) {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("name", "Tom");
        queryParams.put("password", "123456");
        Map<String, Object> headers = new HashMap<>();
        headers.put("Accept-Encoding", "gzip");

        NetworkCenter.newObject()
                .setCacheSeconds(20)
                .setMethod(NetworkCenter.Method.GET)
                .setLastUrl("get")
                .setQueryParams(queryParams)
                .setHeaders(headers)
                .setSubscriber(subscriberGet)
                .doExecute();
    }

    public void doPost(View v) {

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("method", "login");

        Map<String, Object> formParams = new HashMap<>();
        formParams.put("name", "Tom");
        formParams.put("password", "123456");

        Map<String, Object> headers = new HashMap<>();
        headers.put("Accept-Encoding", "gzip");

        NetworkCenter.newObject()
                .setMethod(NetworkCenter.Method.POST)
                .setLastUrl("post")
                .setQueryParams(queryParams)
                .setFormParams(formParams)
                .setHeaders(headers)
                .setSubscriber(subscriberPost)
                .doExecute();
    }

    public void doGetNewUrl(View v) {

        NetworkCenter.newObject()
                .setMethod(NetworkCenter.Method.GET)
                .setNewUrl("http://httpbin.org/get")
                .setSubscriber(subscriberGet)
                .doExecute();
    }

    public void doPostNewUrl(View v) {

        NetworkCenter.newObject()
                .setMethod(NetworkCenter.Method.POST)
                .setNewUrl("http://httpbin.org/post")
                .setSubscriber(subscriberPost)
                .doExecute();
    }

    public void doPostNewUrlBody(View v) {

        String stringBody = "NetworkCenter";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("json", "json");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObject gsonBody = new JsonObject();
        gsonBody.addProperty("gson", "gson");
        gsonBody.addProperty("long", "123456789456123");

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("method", "login");

        Map<String, Object> headers = new HashMap<>();
        headers.put("Accept-Encoding", "gzip");

        NetworkCenter.newObject()
                .setMethod(NetworkCenter.Method.BODY)
                .setNewUrl("http://httpbin.org/post")
                .setQueryParams(queryParams)
                .setHeaders(headers)
                .setBody(stringBody)
                .setSubscriber(subscriberPost)
                .doExecute();

        NetworkCenter.newObject()
                .setMethod(NetworkCenter.Method.BODY)
                .setNewUrl("http://httpbin.org/post")
                .setQueryParams(queryParams)
                .setHeaders(headers)
                .setBody(stringBody)
                .setSubscriber(subscriberPost)
                .doExecute();

        NetworkCenter.newObject()
                .setMethod(NetworkCenter.Method.BODY)
                .setNewUrl("http://httpbin.org/post")
                .setQueryParams(queryParams)
                .setHeaders(headers)
                .setBody(gsonBody)
                .setSubscriber(subscriberPost)
                .doExecute();
    }

    public void doGetTag(View v) {

        NetworkCenter.newObject()
                .setMethod(NetworkCenter.Method.GET)
                .setTag(TAG + (++index))
                .setLastUrl("get")
                .setSubscriber(subscriberPost)
                .doExecute();
        NetworkCenter.getInstance().cancel(TAG + (index));
    }
}
