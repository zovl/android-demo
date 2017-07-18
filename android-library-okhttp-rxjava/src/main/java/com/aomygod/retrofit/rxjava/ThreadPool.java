package com.aomygod.retrofit.rxjava;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 网络请求线程池
 */
class ThreadPool {

	private static final boolean DEBUG = NetworkCenter.getInstance().getDebug();
	private static final String TAG = NetworkCenter.getInstance().getTag();

	private static ThreadPool instance;

	public static ThreadPool getInstance() {
		if (instance == null) {
			synchronized (ThreadPool.class) {
				if (instance == null) {
					instance = new ThreadPool();
				}
			}
		}
		return instance;
	}

	private ThreadPool() {
		if (executor == null) {
			executor = Creator.newService();
		}
	}

	// ---------------------------------------------------------------------------------

	private ExecutorService executor;

	public ExecutorService createService() {
		if (executor == null || executor.isShutdown()) {
			executor = Creator.newService();
			if (DEBUG) Log.d(TAG, "executor: " + executor);
		}
		return executor;
	}

	private static class Creator {
		private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
		private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
		private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;// IO密集型
		private static final int MAXIMUM_POOL_SIZE_ = CPU_COUNT + 1;// CPU密集型
		private static final int KEEP_ALIVE = 1;
		private static ExecutorService newService() {
			ThreadFactory threadFactory = new ThreadFactory() {

				private final AtomicInteger count = new AtomicInteger(1);

				public Thread newThread(Runnable r) {
					return new Thread(r, "ThreadPool-" + count.getAndIncrement());
				}
			};
			BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(128);
			ExecutorService executorService = new ThreadPoolExecutor(CORE_POOL_SIZE,
					MAXIMUM_POOL_SIZE,
					KEEP_ALIVE,
					TimeUnit.SECONDS,
					queue,
					threadFactory) {

				@Override
				public void execute(Runnable command) {
					super.execute(command);
				}
			};
			return executorService;
		}
	}
}
