package com.aomygod.retrofit.eventbus;

/**
 * 网络请求任务类（在主线程执行）
 */
public class RequestTarget {

    protected static boolean DEBUG = NetworkManager.getInstance().getDebug();
    private static final String TAG = NetworkManager.getInstance().getTAG();

    /**
     * 功能：网络访问返回实体
     */
    public Object postEntity(RequestObject request) {
        OkMethod ok = new OkMethod();
        ResponseObject response = null;
        try {
            if (request.getType() == NetworkObject.Method.GET) {
                response = ok.doExecute(request);
            } else if (request.getType() == NetworkObject.Method.POST) {
                response = ok.doExecute(request);
            } else if (request.getType() == NetworkObject.Method.UPLOAD) {
                response = ok.doExecute(request);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response != null) {
            return response.getResultBean();
        }
        return null;
    }
}
