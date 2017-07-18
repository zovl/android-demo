package com.aomygod.retrofit.eventbus;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求对象
 */
@SuppressWarnings("serial")
public class RequestObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//请求方法
	protected int type;
	
	//请求地址
	protected String url;
	protected String newUrl;
	
	// 请求文本
	protected String json;
	
	// 请求参数
	protected Map<String, Object> params;

	// 请求头
	protected Map<String, Object> headers;

	// 请求文件
	protected Map<String, File> files;

	// 下载的文件
	private File downloadFile;

	// 请求文件
	protected String path;

	// 请求数据结构
	protected Object entity;

	private WeakReference<EventBusActivity> weakReference;

	public RequestObject() {
		super();
	}

	public RequestObject(int type, String url, Map<String, Object> params, Object entity) {
		super();
		setType(type);
		setUrl(url);
		setParams(params);
		setHeaders(headers);
		setFiles(files);
		setEntity(entity);
		newUrl = Util.getNewUrl(url, params);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		if (url != null) {
			url = url.trim();
		}
		this.url = url;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		Map<String, Object> map = new HashMap<>();
		if (params != null) {
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				String key = "";
				String value = "";
				try {
					key = entry.getKey().trim();
					value = entry.getValue().toString().trim();
				} catch (Exception e) {
					e.printStackTrace();
				}
				map.put(key, value);
			}
		}
		this.params = map;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public Map<String, File> getFiles() {
		return files;
	}

	public void setFiles(Map<String, File> files) {
		this.files = files;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getNewUrl() {
		return newUrl;
	}

	public void setNewUrl(String newUrl) {
		this.newUrl = newUrl;
	}

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public File getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(File downloadFile) {
		this.downloadFile = downloadFile;
	}

	public EventBusActivity getActivity() {
		if (weakReference != null && weakReference.get() != null) {
			return weakReference.get();
		}
		return null;
	}

	public void setActivity(EventBusActivity activity) {
		if (activity != null) {
			weakReference = new WeakReference<EventBusActivity>(activity);
		}
	}

	@Override
	public String toString() {
		return "RequestObject{" +
				"type=" + type +
				", url='" + url + '\'' +
				", json='" + json + '\'' +
				", params=" + params +
				", headers=" + headers +
				", files=" + files +
				", path='" + path + '\'' +
				", entity=" + entity +
				", activity=" + getActivity() +
				'}';
	}
}
