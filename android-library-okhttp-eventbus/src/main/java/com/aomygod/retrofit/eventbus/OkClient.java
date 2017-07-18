package com.aomygod.retrofit.eventbus;

import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Authenticator;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.Dns;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OKHttp网络请求客户端
 */
class OkClient implements IRequestClient {

	protected static boolean DEBUG = NetworkManager.getInstance().getDebug();
	private static final String TAG = NetworkManager.getInstance().getTAG();

	// ------------------------------------------------------------------------------------

	private static OkClient instance;

	public static OkClient getInstance() {
		if (instance == null) {
			synchronized (OkClient.class) {
				if (instance == null) {
					instance = new OkClient();
				}
			}
		}
		return instance;
	}

	public static void destruction() {
		instance = null;
	}

	// ------------------------------------------------------------------------------------

	private OkHttpClient okHttpClient;
	private OkHttpClient newOkHttpClient;

	private OkClient() {
		super();
		if (okHttpClient == null) {
			Cache cache = new Cache(null, 50 * 1000 * 1000);
			final ConnectionPool connectionPool = new ConnectionPool();
			final SocketFactory socketFactory = SocketFactory.getDefault();
			final Dispatcher dispatcher = new Dispatcher(RequestExecutor.getExecutor());
			final Authenticator authenticator = new Authenticator() {
				@Override
				public Request authenticate(Route route, Response response) throws IOException {
					return null;
				}
			};
			Map<String, String> headers = new HashMap<>();
			final Dns dns = new Dns() {
				@Override
				public List<InetAddress> lookup(String hostname) throws UnknownHostException {
					return null;
				}
			};
			SSLSocketFactory sslSocketFactory = null;
			try {
				TrustManager[] trustAllCerts = new TrustManager[]{
						new X509TrustManager() {
							public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
							}

							public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
							}

							public X509Certificate[] getAcceptedIssuers() {
								// return new X509Certificate[0];
								return new X509Certificate[] {};
							}
						}
				};
				SSLContext sslContext = SSLContext.getInstance("SSL");
				sslContext.init(null, trustAllCerts, new SecureRandom());
				sslSocketFactory = sslContext.getSocketFactory();
				/*
				SSLSocket sslSocket  = (SSLSocket) sslSocketFactory.createSocket();
				sslSocket.setUseClientMode(true);
				sslSocket.startHandshake();*/
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (KeyManagementException e) {
				e.printStackTrace();
			} /*catch (IOException e) {
				e.printStackTrace();
			}*/
			HostnameVerifier hostnameVerifier = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder()
					.connectTimeout(12000, TimeUnit.SECONDS)
					.readTimeout(12000, TimeUnit.SECONDS)
					.retryOnConnectionFailure(true)
					// .cache(cache)
					// .connectionPool(connectionPool)
					// .dns(dns)
					// .authenticator(authenticator)
					// .addNetworkInterceptor(interceptor)
					.dispatcher(dispatcher);
			if (DEBUG) {
				final HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
					@Override public void log(String message) {
						Platform.get().log(Platform.INFO, message, null);
					}
				};
				final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
				loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
				final Interceptor interceptor = new Interceptor() {
					@Override
					public Response intercept(Chain chain) throws IOException {
						Request.Builder builder = chain.request().newBuilder();
						return chain.proceed(builder.build());
					}
				};
				builder.addInterceptor(loggingInterceptor);
			}
			/*
			if (sslSocketFactory != null)
				builder.sslSocketFactory(sslSocketFactory);*/
			/*
			if (sslSocketFactory != null)
				builder.socketFactory(sslSocketFactory);
			if (hostnameVerifier != null)
				builder.hostnameVerifier(hostnameVerifier);*/
			okHttpClient = builder.build();
			if (DEBUG) Log.d(TAG, "okHttpClient: " + okHttpClient);
		}

		if (newOkHttpClient == null) {
			final Dispatcher dispatcher = new Dispatcher(RequestExecutor.getExecutor());
			final HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
				@Override public void log(String message) {
					Platform.get().log(Platform.INFO, message, null);
				}
			};
			final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger);
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
			okhttp3.OkHttpClient.Builder newBuilder = new okhttp3.OkHttpClient.Builder();

			newBuilder.connectTimeout(12000, TimeUnit.SECONDS)
					.readTimeout(12000, TimeUnit.SECONDS)
					.retryOnConnectionFailure(true)
					.dispatcher(dispatcher);

			if (DEBUG) {
					newBuilder.addInterceptor(loggingInterceptor);
			}
			newOkHttpClient = newBuilder.build();
			if (DEBUG) Log.d(TAG, "newOkHttpClient: " + newOkHttpClient);
		}
	}

	public OkHttpClient getOkHttpClient() {
		return okHttpClient;
	}

	public OkHttpClient getNewOkHttpClient() {
		return newOkHttpClient;
	}

	// ------------------------------------------------------------------------------------

	@Override
	public Response execute(Request request) throws IOException {
		if (okHttpClient != null) {
			Call call = okHttpClient.newCall(request);
			Response response = call.execute();
			return response;
		}
		return null;
	}
}
