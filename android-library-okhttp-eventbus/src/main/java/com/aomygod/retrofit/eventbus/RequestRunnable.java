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
		try {
			if (requestObject.getType() == NetworkObject.Method.GET) {
				responseObject = ok.doExecute(requestObject);
			} else if (requestObject.getType() == NetworkObject.Method.POST) {
				responseObject = ok.doExecute(requestObject);
			} else if (requestObject.getType() == NetworkObject.Method.UPLOAD) {
				// TODO: 2016/11/23 上传文件
				responseObject = ok.doExecute(requestObject);
			} else if (requestObject.getType() == NetworkObject.Method.DOWNLOAD) {
				ok.doExecute(requestObject);
			} else {
				throw new NullPointerException("Error: method is not supported!");
			}
		} catch (Exception e) {
			e.printStackTrace();
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

