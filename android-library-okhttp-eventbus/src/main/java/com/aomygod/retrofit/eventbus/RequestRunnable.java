package com.aomygod.retrofit.eventbus;

import android.os.Handler;
import android.os.Looper;

/**
 * 网络请求线程执行体类
 */
class RequestRunnable implements Runnable {

	protected static boolean DEBUG = NetworkManager.getInstance().getDebug();
	private static final String TAG = NetworkManager.getInstance().getTAG();

	private Handler handler = new Handler(Looper.getMainLooper());

	private RequestObject requestObject;
	private ResponseHandler responseHandler;
	private OkMethod ok;
	private ResponseObject responseObject;

	public RequestRunnable(RequestObject requestObject) {
		super();
		this.requestObject = requestObject;
		ok = new OkMethod();
	}
	
	@Override
	public void run() {
		if (requestObject.getType() == NetworkManager.Method.GET ||
				requestObject.getType() == NetworkManager.Method.POST ||
				requestObject.getType() == NetworkManager.Method.UPLOAD ||
				requestObject.getType() == NetworkManager.Method.DOWNLOAD) {
			responseObject = ok.doExecute(requestObject);
		} else {
			throw new NullPointerException("Error: method is not supported!");
		}
		responseHandler = new ResponseHandler(responseObject);
		responseHandler.handle();
	}

	public void cancel() {
		if (ok != null) {
			try {
				ok.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

