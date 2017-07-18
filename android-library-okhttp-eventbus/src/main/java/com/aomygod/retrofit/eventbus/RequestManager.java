package com.aomygod.retrofit.eventbus;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 网络请求页面管理类
 */
class RequestManager {

    protected static boolean DEBUG = NetworkManager.getInstance().getDebug();
    private static final String TAG = NetworkManager.getInstance().getTAG();

    private List<RequestRunnable> tasks = new ArrayList<>();

    /**
     * 添加网络任务
     */
    private void addTask(RequestRunnable task) {
        if (tasks != null && !tasks.contains(task)) {
            tasks.add(task);
            if (DEBUG) Log.d(TAG, task + " is added!");
        }
    }

    /**
     * 取消所有网络任务
     */
    public void cancelAllTask() {
        if (tasks != null) {
            for (int i = 0; i < tasks.size(); ++i) {
                RequestRunnable t = tasks.get(i);
                t.cancel();
                tasks.clear();
            }
        }
    }

    /**
     * 取消网络任务
     */
    public void cancelTask(RequestRunnable task) {
        if (task != null) {
            task.cancel();
            tasks.remove(task);
            if (DEBUG) Log.d(TAG, task + " is canceled!");
        }
    }

    /**
     * 生产网络任务
     */
    public RequestRunnable createTask(RequestObject request) {
        if (request != null) {
            RequestRunnable task = new RequestRunnable(request);
            addTask(task);
            if (DEBUG) Log.d(TAG, task + " is created!");
            return task;
        }
        return null;
    }

    /**
     * 移除所有网络任务
     */
    public void clearAllTask() {
        if (tasks != null) {
            tasks.clear();
        }
    }

    /**
     * 打印网络任务
     */
    public void printAllTask() {
        if (tasks != null) {
            for (RequestRunnable task : tasks) {
                if (DEBUG) Log.d(TAG, "task: " + task);
            }
        }
    }
}
