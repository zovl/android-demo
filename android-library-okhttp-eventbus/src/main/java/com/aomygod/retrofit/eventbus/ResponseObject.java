package com.aomygod.retrofit.eventbus;

import org.json.JSONObject;

/**
 * 功能：网络请求响应对象
 */
@SuppressWarnings("serial")
public class ResponseObject extends RequestObject {

	private static final long serialVersionUID = 1L;

	// 返回码
	private int statusCode = -1;

	// 返回的String
	private String resultString = "";

	// 返回的JSON
	private JSONObject resultJson;

	// 返回的Bean
	private Object resultBean;

	public ResponseObject() {
		super();
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public JSONObject getResultJson() {
		return resultJson;
	}

	public void setResultJson(JSONObject resultJson) {
		this.resultJson = resultJson;
	}

	public Object getResultBean() {
		if (resultString != null && entity != null) {
			resultBean = GsonUtil.parse(resultString, entity.getClass());
		}
		return resultBean;
	}
}
