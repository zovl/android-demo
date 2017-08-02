package com.aomygod.retrofit.eventbus;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求方法
 */
class OkMethod {

	protected static boolean DEBUG = NetworkManager.getInstance().getDebug();
	private static final String TAG = NetworkManager.getInstance().getTAG();

	private OkHttpClient client = OkClient.getInstance().getNewOkHttpClient();
	private Call call;
	private Request request;
	private Response response;
	private RequestObject requestObject;
	private int type;
	private String url;
	private Map<String, Object> params;
	private Map<String, Object> headers;
	private String newUrl;
	private Map<String, File> files;
	private String path;
	private ResponseObject responseObject;
	private int statusCode;
	private String resultString = "";
	private File downloadFile;

	private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");
	private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
	private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain;charset=utf-8");
	private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream;charset=utf-8");
	private static final MediaType MEDIA_TYPE_FORM = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
	private static final MediaType MEDIA_TYPE_MULTIPART = MediaType.parse("multipart/form-data; boundary=${--------------------56423498738365};charset=utf-8");

	public OkMethod() {}

	/**
     * 执行网络任务
     */
	public ResponseObject doExecute(RequestObject requestObject) {
		if (requestObject == null) {
			throw new NullPointerException("Error: requestObject is null!");
		}
		this.requestObject = requestObject;
		type = requestObject.getType();
		url = requestObject.getUrl();
		path = requestObject.getPath();
		downloadFile = requestObject.getDownloadFile();
		if (url == null || url.length() == 0 || url.equals("null")) {
			throw new NullPointerException("Error: url is null!");
		}
		if (type == NetworkManager.Method.DOWNLOAD && downloadFile == null) {
			throw new NullPointerException("Error: downloadFile is null!");
		}
		params = requestObject.getParams();
		headers = requestObject.getHeaders();
		files = requestObject.getFiles();
		newUrl = Util.getNewUrl(url, params);
		if (DEBUG) Log.d(TAG, "requestObject: " + requestObject);
		if (DEBUG) Log.d(TAG, "url: " + url);
		if (DEBUG) Log.d(TAG, "params: " + params);
		if (DEBUG) Log.d(TAG, "newUrl: " + newUrl);
		if (DEBUG) Log.d(TAG, "files: " + files);
		responseObject = Util.newResponseObject(requestObject);

		if (type == NetworkManager.Method.GET) {
			doGet();
		} else if (type == NetworkManager.Method.POST) {
			doPost();
		} else if (type == NetworkManager.Method.UPLOAD) {
			uploadFile();
		}  else if (type == NetworkManager.Method.DOWNLOAD) {
			downloadFile();
		} else {
			throw new NullPointerException("Error: request type is unsuppoeted!");
		}
		if (DEBUG) Log.d(TAG, "statusCode: " + statusCode);
		if (DEBUG) Log.d(TAG, "resultString: " + resultString);
		if (DEBUG) Log.d(TAG, "responseObject: " + responseObject);
		return responseObject;
	}

	/**
	 * GET
	 */
	public void doGet() {
		Request.Builder builder = new Request.Builder();
		builder.url(newUrl);
		// 请求头
		if (headers != null && headers.size() > 0) {
			for (String name : headers.keySet()) {
				String values = (String) headers.get(name);
				builder.addHeader(name, values);
			}
		}

		builder.get();

		request = builder.build();
		execute();
		stringResult();
	}

	/**
	 * POST（提交表单）
	 */
	public void doPost() {
		Request.Builder builder = new Request.Builder();
		builder.url(url);
		// 请求头
		if (headers != null && headers.size() > 0) {
			for (String name : headers.keySet()) {
				String values = (String) headers.get(name);
				builder.addHeader(name, values);
			}
		}
		// 请求参数
		RequestBody body = null;
		if (params != null && params.size() > 0) {
			FormBody.Builder b = new FormBody.Builder();
			Set<String> keys = params.keySet();
			for (String key : keys) {
				if (params.get(key) != null) {
					String value = params.get(key).toString();
					b.add(key, value);
				}
			}
			body = b.build();
		}
		builder.post(body);

		request = builder.build();
		execute();
		stringResult();
	}

	/**
	 * 上传文件
	 */
	public void uploadFile() {
		multipart();
	}

	/**
	 * 下载文件
	 */
	public void downloadFile() {
		download();
	}

	/**
	 * GET（下載文件）
	 */
	private void download() {
		Request.Builder builder = new Request.Builder();
		builder.url(url);
		// 请求头
		if (headers != null && headers.size() > 0) {
			for (String name : headers.keySet()) {
                String values = (String) headers.get(name);
                builder.addHeader(name, values);
            }
		}

		request = builder.build();
		execute();
		downloadResult();
	}

	/**
	 * POST（提交文件）
	 */
	private void stream() {
		Request.Builder builder = new Request.Builder();
		builder.url(url);
		// 请求头
		if (headers != null && headers.size() > 0) {
			for (String name : headers.keySet()) {
				String values = (String) headers.get(name);
				builder.addHeader(name, values);
			}
		}
		// 请求文件
		if (!StringUtil.isNull(path)) {
			File file = new File(path);
			RequestBody body = RequestBody.create(MEDIA_TYPE_STREAM, file);
			builder.post(body);
		} else if (files != null && files.size() > 0) {

		}

		request = builder.build();
		execute();
		stringResult();
	}

	/**
	 * POST（提交JSON）
	 */
	public void json() {
		Request.Builder builder = new Request.Builder();
		builder.url(url);
		// 请求头
		if (headers != null && headers.size() > 0) {
			for (String name : headers.keySet()) {
				String values = (String) headers.get(name);
				builder.addHeader(name, values);
			}
		}
		// 请求文本
		RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, requestObject.getJson());
		builder.post(body);

		request = builder.build();
		execute();
		stringResult();
	}

	/**
	 * POST（提交分块）
	 */
	public void multipart() {
		Request.Builder builder = new Request.Builder();
		builder.url(url);
		// 请求块
		MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
		multipartBuilder.setType(MEDIA_TYPE_MULTIPART);
		// 表单
		if (params != null && params.size() > 0) {
			for (String key : params.keySet()) {
				String value = (String) params.get(key);
				if (!StringUtil.isNull(value)) {
					multipartBuilder.addPart(MultipartBody.Part.createFormData(key, value));
					/*
					b.addPart(
							Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
							RequestBody.create(MEDIA_TYPE_TEXT, value));*/
				}
			}
		}
		// 文件
		if (files != null && files.size() > 0) {
			for (String key : files.keySet()) {
				File value = files.get(key);
                if (value != null && value.exists()) {
					multipartBuilder.addPart(MultipartBody.Part.create(RequestBody.create(MEDIA_TYPE_STREAM, value)));
                }
            }
		}
		RequestBody body = multipartBuilder.build();
		builder.post(body);

		request = builder.build();
		execute();
		stringResult();
	}

	/**
	 * 解析结果
	 */
	private void stringResult() {
		if (response != null) {
			try {
				resultString = response.body().string();
			} catch (IOException e) {
				e.printStackTrace();
			}
			statusCode = response.code();
			responseObject.setStatusCode(statusCode);
			responseObject.setResultString(resultString);
		}
	}

	/**
	 * 保存文件
	 */
	private void downloadResult() {
		if (response != null) {
			InputStream is = response.body().byteStream();
			if (is != null) {
				FileUtil.saveFile(downloadFile.getPath(), is);
				statusCode = response.code();
				responseObject.setStatusCode(statusCode);
				responseObject.setDownloadFile(downloadFile);
			}
		}
	}

	/**
	 * 执行
	 */
	private void execute() {
		call = client.newCall(request);
		try {
			response = call.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取消
	 */
	public boolean cancel() {
		if (call != null) {
			try {
				call.cancel();
				if (DEBUG) Log.d(TAG, call + " is canceled!");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
