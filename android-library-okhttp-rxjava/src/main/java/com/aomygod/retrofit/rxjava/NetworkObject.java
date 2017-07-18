package com.aomygod.retrofit.rxjava;

import android.content.Context;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Log;

import com.trello.rxlifecycle2.LifecycleTransformer;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 网络访问控制中心
 */
public class NetworkObject {
    
    private final boolean DEBUG = NetworkCenter.getInstance().getDebug();
    private final String TAG = NetworkCenter.getInstance().getTag();

    static NetworkObject newInstance() {
        return new NetworkObject();
    }

    private NetworkObject() {
        context = NetworkCenter.getInstance().getContext();
        hostUrl = NetworkCenter.getInstance().getHostUrl();

        queryParams = new HashMap<>();
        formParams = new HashMap<>();
        headers = new HashMap<>();
    }

    // -----------------------------------------------------------------

    private LifecycleTransformer transformer;// 网络访问生命周期
    private int cacheSeconds;// 缓存时间（单位：秒）：在cacheSeconds时间内会返回缓存，并不进行执行网络访问
    private Context context;

    public NetworkObject setTransformer(LifecycleTransformer transformer) {
        this.transformer = transformer;
        if (DEBUG) Log.d(TAG, "transformer: " + transformer);
        return this;
    }

    public NetworkObject setCacheSeconds(int cacheSeconds) {
        this.cacheSeconds = cacheSeconds;
        if (DEBUG) Log.d(TAG, "cacheSeconds: " + cacheSeconds);
        return this;
    }

    // -----------------------------------------------------------------

    /**
     * @param tag                 请求标记，用于取消请求
     * @param method              方法，GET/POST/BODY
     * @param lastUrl             除去域名的Url
     * @param newUrl              完整的Url（lastUrl和newUrl只能传一个）
     * @param queryParams         加在Url路径上面的参数
     * @param formParams          表单参数（POST方法）
     * @param body                要上传的实体（String/Gson实体等）
     * @param headers             请求头
     * @param subscriber          回调
     */
    
    private Object tag;
    private int method;
    private String hostUrl;
    private String lastUrl;
    private String newUrl;
    private Map<String, Object> queryParams;
    private Map<String, Object> formParams;
    private Object body;
    private Map<String, Object> headers;
    private NetworkSubscriber subscriber;

    public NetworkObject setTag(Object tag) {
        this.tag = tag;
        if (DEBUG) Log.d(TAG, "tag: " + tag);
        return this;
    }

    public NetworkObject setMethod(int method) {
        this.method = method;
        if (DEBUG) Log.d(TAG, "method: " + method);
        return this;
    }

    public NetworkObject setLastUrl(String lastUrl) {
        this.lastUrl = lastUrl;
        if (DEBUG) Log.d(TAG, "lastUrl: " + lastUrl);
        return this;
    }

    public NetworkObject setNewUrl(String newUrl) {
        this.newUrl = newUrl;
        if (DEBUG) Log.d(TAG, "newUrl: " + newUrl);
        return this;
    }

    public NetworkObject setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
        if (DEBUG) Log.d(TAG, "queryParams: " + queryParams);
        return this;
    }

    public NetworkObject setFormParams(Map<String, Object> formParams) {
        this.formParams = formParams;
        if (DEBUG) Log.d(TAG, "formParams: " + formParams);
        return this;
    }

    public NetworkObject setBody(Object body) {
        this.body = body;
        if (DEBUG) Log.d(TAG, "body: " + body);
        return this;
    }

    public NetworkObject setHeaders(Map<String, Object> headers) {
        this.headers = headers;
        if (DEBUG) Log.d(TAG, "headers: " + headers);
        return this;
    }

    public NetworkObject setSubscriber(NetworkSubscriber subscriber) {
        this.subscriber = subscriber;
        if (DEBUG) Log.d(TAG, "subscriber: " + subscriber);
        return this;
    }

    // -----------------------------------------------------------------

    /**
     * 执行网络访问
     */
    public void doExecute() {
        if (DEBUG) Debug.startMethodTracing("/mnt/sdcard/retrofit-rxjava-" + System.currentTimeMillis());
        /** 检查缓存 */
        String fullUrl;
        if (!TextUtils.isEmpty(newUrl)) {
            fullUrl = newUrl;
        } else {
            if (lastUrl != null) {
                fullUrl = hostUrl + lastUrl;
            } else {
                fullUrl = hostUrl;
            }
        }
        Map<String, Object> params = new HashMap<>();
        params.putAll(queryParams);
        params.putAll(formParams);
        params = com.aomygod.retrofit.rxjava.Util.sortMap(params);
        String key = Util.urlParams(fullUrl, params);

        final String md5Key = Util.md5(key);

        Cache cache = null;
        if(null != context) {
            cache = Cache.get(context);
            String cacheString = cache.getAsString(md5Key);
            if(!TextUtils.isEmpty(cacheString)) {
                try {
                    Class cls = subscriber.getCls();
                    Object o = GSONUtil.GSON.fromJson(cacheString, cls);
                    subscriber.onNext(o);
                    return;
                } catch (Exception s) {}
            }
        }

        /** 发起请求 */
        RetrofitService service = RetrofitManager.getInstance().createService(RetrofitService.class);
        Observable<Object> observable;
        if (method == NetworkCenter.Method.GET) {
            if (lastUrl != null) {
                observable = service.doGet(lastUrl, queryParams, headers);
            } else if (newUrl != null) {
                observable = service.doGetNewUrl(newUrl, queryParams, headers);
            } else {
                throw new NullPointerException("Error: lastUrl and newUrl must has one!");
            }
        } else if (method == NetworkCenter.Method.POST) {
            if (lastUrl != null) {
                observable = service.doPost(lastUrl, queryParams, formParams,  headers);
            } else if (newUrl != null) {
                observable = service.doPostNewUrl(newUrl, queryParams, formParams,  headers);
            } else {
                throw new NullPointerException("Error: lastUrl and newUrl must has one!");
            }
        } else if (method == NetworkCenter.Method.BODY) {
            observable = service.doPostNewUrl(newUrl, queryParams, body,  headers);
        } else {
            throw new NullPointerException("Error: request has not such method!");
        }

        final Cache mCache = cache;
        Observer<Object> observer = new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                if (DEBUG) Log.d(TAG, "Callback: " + d);
                if (tag != null) {
                    RetrofitManager.getInstance().addDisposable(tag, d);
                }
            }

            @Override
            public void onNext(Object value) {
                if (DEBUG) Debug.stopMethodTracing();
                if (DEBUG) Log.d(TAG, "Callback: " + value);
                onNextResult(value, subscriber, mCache, md5Key, cacheSeconds);
                if (tag != null) {
                    RetrofitManager.getInstance().removeDisposable(tag);
                }
            }

            @Override
            public void onError(Throwable e) {
                if (DEBUG) Debug.stopMethodTracing();
                if (DEBUG) Log.d(TAG, "Callback: " + e);
                onErrorResult(e, subscriber);
                if (tag != null) {
                    RetrofitManager.getInstance().removeDisposable(tag);
                }
            }

            @Override
            public void onComplete() {
                if (DEBUG) Debug.stopMethodTracing();
                if (DEBUG) Log.d(TAG, "Callback: onComplete");
                onCompleteResult(subscriber);
                if (tag != null) {
                    RetrofitManager.getInstance().removeDisposable(tag);
                }
            }
        };

        observable.compose(new ObservableTransformer<Object, Object>() {
            @Override
            public ObservableSource<Object> apply(Observable<Object> upstream) {
                if (transformer == null) {
                    return upstream.subscribeOn(Schedulers.io())
                            .unsubscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());
                }
                return upstream.compose(transformer)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        }).compose(new ObservableTransformer<Object, Object>() {
            @Override
            public ObservableSource<Object> apply(Observable<Object> upstream) {
                return upstream.map(new Function<Object, Object>() {
                    @Override
                    public Object apply(Object o) throws Exception {
                        return o;
                    }
                }).onErrorResumeNext(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        return Observable.error(throwable);
                    }
                });
            }
        }).subscribe(observer);
    }

    // -----------------------------------------------------------------

    private void onNextResult(Object value, NetworkSubscriber subscriber, Cache cache, String md5Key, int cacheSeconds) {
        if (subscriber != null) {
            if (value == null) {
                subscriber.onNext(null);
            } else {
                try {
                    String string = GSONUtil.GSON.toJson(value);
                    if(string!= null &&
                            cache != null &&
                            md5Key != null &&
                            cacheSeconds > 0) {
                        cache.put(md5Key, string, cacheSeconds);
                    }
                    Class cls = subscriber.getCls();
                    Object o = GSONUtil.GSON.fromJson(string, cls);
                    subscriber.onNext(o);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onNext(null);
                }
            }
        }
    }

    private void onErrorResult(Throwable e, NetworkSubscriber subscriber) {
        if (subscriber != null) {
            subscriber.onError(e);
        }
    }

    private void onCompleteResult(NetworkSubscriber subscriber) {
        if (subscriber != null) {
            subscriber.onComplete();
        }
    }
}