package com.aomygod.retrofit.rxjava;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit网络访问服务管理类
 */
public class RetrofitManager {

    private static final boolean DEBUG = NetworkCenter.getInstance().getDebug();
    private static final String TAG = NetworkCenter.getInstance().getTag();

    private static RetrofitManager instance;

    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    private RetrofitManager() {}

    // ------------------------------------------------------------------------------------

    public <T> T createService(final Class<T> service) {
        T t = createRetrofit().create(service);
        if (DEBUG) Log.d(TAG, "service: " + t);
        return t;
    }

    public <T> T newService(final Class<T> service, String newHostUrl) {
        T t = newRetrofit(newHostUrl).create(service);
        if (DEBUG) Log.d(TAG, "nweService: " + t);
        return t;
    }

    private Retrofit retrofit;

    private Retrofit createRetrofit() {
        if (retrofit == null) {
            retrofit = Creator.newRetrofit();
        }
        return retrofit;
    }

    private Retrofit newRetrofit(String newHostUrl) {
        if (DEBUG) Log.d(TAG, "newHostUrl: " + newHostUrl);
        if (newHostUrl == null) {
            throw new NullPointerException("Error: newHostUrl is null!");
        }
        return Creator.newRetrofit(newHostUrl);
    }

    private static class Creator {
        private static Retrofit newRetrofit() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkCenter.getInstance().getHostUrl())
                    .addConverterFactory(GsonConverterFactory.create(GSONUtil.GSON))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(Client.getInstance().getClient())
                    .build();
            if (DEBUG) Log.d(TAG, "retrofit: " + retrofit);
            return retrofit;
        }

        private static Retrofit newRetrofit(String newHostUrl) {
            Retrofit newRetrofit = new Retrofit.Builder()
                    .baseUrl(newHostUrl)
                    .addConverterFactory(GsonConverterFactory.create(GSONUtil.GSON))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(Client.getInstance().getClient())
                    .build();
            if (DEBUG) Log.d(TAG, "newRetrofit: " + newRetrofit);
            return newRetrofit;
        }
    }

    // ------------------------------------------------------------------------------------

    private Map<Object, Disposable> disposables = new HashMap<>();

    void addDisposable(Object tag, Disposable disposable) {
        if (tag != null && disposable != null) {
            disposables.put(tag, disposable);
        }
    }

    void removeDisposable(Object tag) {
        if (tag != null) {
            disposables.remove(tag);
        }
    }

    void cancelDisposable(Object tag) {
        if (tag != null) {
            Disposable d = disposables.get(tag);
            if (d != null && !d.isDisposed()) {
                d.dispose();
            }
            disposables.remove(tag);
        }
    }

    void cancelAllDisposable() {
        if (!disposables.isEmpty()) {
            for (Map.Entry<Object, Disposable> entry : disposables.entrySet()) {
                Disposable d = entry.getValue();
                if (!d.isDisposed()) {
                    d.dispose();
                }
            }
            disposables.clear();
        }
    }
}
