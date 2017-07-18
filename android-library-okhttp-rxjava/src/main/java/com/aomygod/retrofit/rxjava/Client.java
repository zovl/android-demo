package com.aomygod.retrofit.rxjava;

import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OKHttp网络请求客户端
 */
class Client {

	private static final boolean DEBUG = NetworkCenter.getInstance().getDebug();
	private static final String TAG = NetworkCenter.getInstance().getTag();

	// ------------------------------------------------------------------------------------

	private static Client instance;

	public static Client getInstance() {
		if (instance == null) {
			synchronized (Client.class) {
				if (instance == null) {
					instance = new Client();
				}
			}
		}
		return instance;
	}

	private Client() {
		if (client == null) {
			client = Creator.newOkHttpClient();
		}
	}

	// ------------------------------------------------------------------------------------

	private OkHttpClient client;

	public OkHttpClient getClient() {
		if (client == null) {
			client = Creator.newOkHttpClient();
		}
		return client;
	}

	private static class Creator {
		private static OkHttpClient newOkHttpClient() {
			// final Cache cache = new Cache(null, 100 * 1000 * 1000);
			final Dispatcher dispatcher = new Dispatcher(ThreadPool.getInstance().createService());
			OkHttpClient.Builder builder = new OkHttpClient.Builder()
					// .cache(cache)
					.dispatcher(dispatcher)
					.connectTimeout(12, TimeUnit.SECONDS)
					.readTimeout(12, TimeUnit.SECONDS)
					.retryOnConnectionFailure(true);
			List<Interceptor> interceptors = NetworkCenter.getInstance().interceptors;
			for (Interceptor interceptor : interceptors) {
				builder.addInterceptor(interceptor);
			}
			if (DEBUG) {
				final HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
					@Override public void log(String message) {
						Platform.get().log(Platform.INFO, message, null);
					}
				};
				final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
				loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
				builder.addInterceptor(loggingInterceptor);
			}
			OkHttpClient okHttpClient = builder.build();
			if (DEBUG) Log.d(TAG, "okHttpClient: " + okHttpClient);
			return okHttpClient;
		}
	}
}
