package com.aomygod.retrofit.eventbus;

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
class RequestExecutor {

	protected static boolean DEBUG = NetworkManager.getInstance().getDebug();
	private static final String TAG = NetworkManager.getInstance().getTAG();

	public static ExecutorService executor;

	public static ExecutorService getExecutor() {
		if (executor == null || executor.isShutdown()) {
			synchronized (RequestExecutor.class) {
				if (executor == null || executor.isShutdown()) {
					executor = newExecutor();
				}
			}
		}
		return executor;
	}

	private RequestExecutor() {}

	// ---------------------------------------------------------------------------------

	private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
	public static final int CORE_POOL_SIZE = CPU_COUNT + 1;
	public static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;// IO密集型
	public static final int MAXIMUM_POOL_SIZE_ = CPU_COUNT + 1;// CPU密集型
	private static final int KEEP_ALIVE = 1;

	private static ExecutorService newExecutor() {
		ThreadFactory threadFactory = new ThreadFactory() {

			private final AtomicInteger count = new AtomicInteger(1);

			public Thread newThread(Runnable r) {
				return new Thread(r, "RequestExecutor-" + count.getAndIncrement());
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

	/**
	 * 线程池执行
	 */
	public static void execute(Runnable r) {
		if (DEBUG) Log.d(TAG, "r: " + r);
		try {
			getExecutor().execute(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 关闭线程池
	 */
	public static void shutdown() {
		if (executor != null && !executor.isShutdown()) {
			try {
				executor.shutdown();
				if (DEBUG) Log.d(TAG, executor + " is shutdown!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		executor = null;
	}

	/**
	 * 立即关闭线程池（挂起所有正在执行的线程，不接受新任务）
	 */
	public static void shutdownNow() {
		if (executor != null && !executor.isShutdown()) {
			try {
				executor.shutdownNow();
				if (DEBUG) Log.d(TAG, executor + " is shutdownNow!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		executor = null;
	}
}
