package org.androidpn.client.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/*
 * Ïß³Ì³Ø
 */
public class ExecutorThreadPool {

	
	private static ThreadPoolExecutor threadPoolExecutor;

	
	public static synchronized ThreadPoolExecutor getThreadPool () {
		if (threadPoolExecutor == null) {
			synchronized (ExecutorThreadPool.class) {
				threadPoolExecutor = new ThreadPoolExecutor(5, 10, 1000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5));
			}
		}
		return threadPoolExecutor;
	}
}
