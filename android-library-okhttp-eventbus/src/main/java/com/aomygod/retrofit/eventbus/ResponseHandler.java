package com.aomygod.retrofit.eventbus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求响应结果处理
 */
public class ResponseHandler {

	protected static boolean DEBUG = NetworkManager.getInstance().getDebug();
	private static final String TAG = NetworkManager.getInstance().getTAG();

	private Handler handler = new Handler(Looper.getMainLooper());

	private ResponseObject response;
	private Object entity;

	public ResponseObject getResponse() {
		return response;
	}

	public ResponseHandler(ResponseObject response) {
		super();
		this.response = response;
	}

	public void handle() {
		if (response == null) {
			return;
		}
		String url = "";
		Map<String, Object> params = new HashMap<>();
		String resultString = "";

		try {
			url = response.getUrl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			params = response.getParams();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			resultString = response.getResultString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			entity = GsonUtil.parse(resultString, response.entity.getClass());
			if (DEBUG) Log.d(TAG, "entity: " + entity);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			callbackResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			postResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			saveResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接口回调
	 */
	private void callbackResponse(final ResponseObject response) throws Exception {
		handler.post(new Runnable() {

			@Override
			public void run() {

				ResponseObserver.getInstance().notifyListener(response == null ? null : response.getNewUrl(), response);
			}
		});
	}
	
	/**
	 * 事件回调
	 */
	private void postResponse() throws Exception {
		if (entity == null) {
			entity = response.entity.getClass().newInstance();
		}
		EventBus.getDefault().post(entity);
	}

	/**
	 * 保存结果
	 */
	private void saveResponse(ResponseObject response) throws Exception {
		// TODO: 2016/12/9  缓存数据 1.Preferences缓存数据 2.File本地文件缓存数据
	}
}
