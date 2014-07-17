package com.xyhui.utils;

import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Process;

import com.mslibs.utils.VolleyLog;

public class ServerThreadPool extends Object {

	public static final String TAG = ServerThreadPool.class.getSimpleName();

	public static final int TASK_LEVEL_LOW = 0;
	public static final int TASK_LEVEL_NORMAL = 1;
	public static final int TASK_LEVEL_HIGH = 2;

	private PriorityBlockingQueue<Runnable> mWorkQueue = new PriorityBlockingQueue<Runnable>();

	private ThreadPoolExecutor mExecutor = new ThreadPoolExecutor(2, 4, 10, TimeUnit.MICROSECONDS,
			mWorkQueue);

	/*
	 * 任务加入到队列时的次序
	 */
	private static final AtomicInteger mOrder = new AtomicInteger(1);

	/*
	 * 单例对象
	 */
	private static ServerThreadPool sInstance;

	/*
	 * 私化构造方法
	 */
	private ServerThreadPool() {

	}

	/**
	 * 得到当前类的单例对象
	 * 
	 * @return 单例对象
	 */
	public static ServerThreadPool getInstance() {
		if (null == sInstance) {
			synchronized (ServerThreadPool.class) {
				if (null == sInstance) {
					sInstance = new ServerThreadPool();
				}
			}
		}
		return sInstance;
	}

	/**
	 * 向线程池中投递一个runnable
	 * 
	 * @param task
	 *            任务
	 * @param priority
	 *            任务的优先级
	 * @return alway true
	 */
	public boolean add(Runnable task, int priority) {
		if (null == task) {
			throw new RuntimeException("params is error when call fetch()");
		}

		// 自动生成优先级与次序
		priority = mOrder.getAndIncrement();
		mExecutor.execute(new TaskWithPriority(task, priority, priority));
		VolleyLog.d("ServerThreadPool queue:%s", mWorkQueue.toString());
		return true;
	}

	/**
	 * 向线程池中投递一个runnable
	 * 
	 * @param task
	 *            任务
	 * @return always true
	 */
	public boolean add(Runnable task) {
		return add(task, 0);
	}

	/**
	 * 移除之前向线程池中投递的runnable
	 * 
	 * @param task
	 *            任务
	 * @return always true
	 */
	public boolean remove(Runnable task) {
		return true;
	}

	/**
	 * Initiates an orderly shutdown in which previously submitted tasks are executed, but no new
	 * tasks will be accepted. Invocation has no additional effect if already shut down. This
	 * method does not wait for previously submitted tasks to complete execution. Use
	 * awaitTermination to do that.
	 */
	public void shutDown() {
		mExecutor.shutdown();
	}

	/**
	 * Attempts to stop all actively executing tasks, halts the processing of waiting tasks, and
	 * returns a list of the tasks that were awaiting execution. These tasks are drained (removed)
	 * from the task queue upon return from this method. This method does not wait for actively
	 * executing tasks to terminate. Use awaitTermination to do that. There are no guarantees
	 * beyond best-effort attempts to stop processing actively executing tasks. This implementation
	 * cancels tasks via Thread.interrupt, so any task that fails to respond to interrupts may
	 * never terminate.
	 * 
	 * @return list of tasks that never commenced execution.
	 */
	public List<Runnable> shutDownNow() {
		return mExecutor.shutdownNow();
	}

	/**
	 * Blocks until all tasks have completed execution after a shutdown request, or the timeout
	 * occurs, or the current thread is interrupted, whichever happens first.
	 * 
	 * @param timeout
	 *            the maximum time to wait
	 * @param unit
	 *            the time unit of the timeout argument
	 * @return true if this executor terminated and false if the timeout elapsed before termination
	 * @throws InterruptedException
	 *             if interrupted while waiting
	 */
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return mExecutor.awaitTermination(timeout, unit);
	}

	/*
	 * 有优先级别的任务
	 */
	class TaskWithPriority implements Comparable<TaskWithPriority>, Runnable {

		private int mPriority;
		private Runnable mRunnable;
		private int mOrder;

		public TaskWithPriority(Runnable runnable, int level) {
			mRunnable = runnable;
			mPriority = level;
		}

		public TaskWithPriority(Runnable runnable, int level, int order) {
			mRunnable = runnable;
			mPriority = level;
			mOrder = order;
		}

		@Override
		public void run() {
			// 设置后台线程的优先级
			Process.setThreadPriority(Process.myTid(), Process.THREAD_PRIORITY_BACKGROUND);

			// long startTime = System.currentTimeMillis();
			mRunnable.run();
		}

		@Override
		public int compareTo(TaskWithPriority o) {
			VolleyLog.v("compareTo method()");
			// 降序排列
			return -(mPriority - o.mPriority);
		}

		@Override
		public String toString() {
			return "priority:" + mPriority + ";order:" + mOrder;
		}
	}
}
