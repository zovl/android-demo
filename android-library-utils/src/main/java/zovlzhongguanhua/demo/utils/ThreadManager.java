package zovlzhongguanhua.demo.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池管理类
 * 使用: ThreadManager.proxy().execute(runnable);
 */
public class ThreadManager {

    private static final String TAG = ThreadManager.class.getSimpleName();
    private static ThreadPoolProxy sThreadPoolProxy;

    public static ThreadPoolProxy proxy() {
        if (sThreadPoolProxy == null) {
            synchronized (ThreadManager.class) {
                if (sThreadPoolProxy == null) {
                    int processorCount = Runtime.getRuntime().availableProcessors();
                    int maxAvailable = Math.max(processorCount * 3, 10);
                    sThreadPoolProxy = new ThreadPoolProxy(processorCount, maxAvailable, 15000);
                }
            }
        }
        return sThreadPoolProxy;
    }

    public static class ThreadPoolProxy {
        private ThreadPoolExecutor threadPoolExecutor;
        private int corePoolSize;
        private int maximumPoolSize;
        private int keepAliveTime;

        public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, int keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        public void execute(Runnable runnable) {
            if (runnable != null) {
                if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
                    synchronized (ThreadManager.class) {
                        if (threadPoolExecutor == null || threadPoolExecutor.isShutdown()) {
                            threadPoolExecutor = createExecutor();
                            threadPoolExecutor.allowCoreThreadTimeOut(false);
                        }
                    }
                }
                threadPoolExecutor.execute(runnable);
            }
        }

        public void remove(Runnable runnable) {
            if (runnable != null && threadPoolExecutor != null) {
                threadPoolExecutor.remove(runnable);
            }
        }

        public void shutdown() {
            if (threadPoolExecutor != null && !threadPoolExecutor.isShutdown()) {
                synchronized (ThreadManager.class) {
                    if (threadPoolExecutor != null && !threadPoolExecutor.isShutdown()) {
                        threadPoolExecutor.shutdown();
                    }
                }
            }
        }

        public void shutdownNow() {
            if (threadPoolExecutor != null && !threadPoolExecutor.isShutdown()) {
                synchronized (ThreadManager.class) {
                    if (threadPoolExecutor != null && !threadPoolExecutor.isShutdown()) {
                        threadPoolExecutor.shutdownNow();
                    }
                }
            }
        }

        private ThreadPoolExecutor createExecutor() {
            return new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize, keepAliveTime,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    new DefaultThreadFactory(Thread.NORM_PRIORITY),
                    new ThreadPoolExecutor.AbortPolicy());
        }

        public static class DefaultThreadFactory implements ThreadFactory {

            static final AtomicInteger groupNumber = new AtomicInteger(0);
            final AtomicInteger threadNumber = new AtomicInteger(0);

            private final ThreadGroup group;
            private final String prefix;
            private final int threadPriority;

            DefaultThreadFactory(int threadPriority) {
                this.threadPriority = threadPriority;
                this.group = new ThreadGroup("ThreadGroup-" + groupNumber.incrementAndGet());
                this.prefix = "Thread-" + groupNumber.incrementAndGet() + "-";
            }

            @Override
            public Thread newThread(Runnable runnable) {
                Thread t = new Thread(group, runnable, prefix + threadNumber.incrementAndGet(), 0);
                if (t.isDaemon()) {
                    t.setDaemon(false);
                }
                t.setPriority(threadPriority);
                return t;
            }
        }
    }
}