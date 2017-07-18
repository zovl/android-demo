package com.aomygod.retrofit.eventbus;

/**
 * 功能：网络请求响应回调
 */
public interface ResponseListener {
	
	/**
	 * 功能：返回网络访问结果
	 */
	void onResponse(ResponseObject object);
}
