package com.boco.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 并行计算框架
 * @author xueliang
 * 例子：
 * 创建10个线程
 * Executer e = new Executer(10);
 * for(...){
 * 	  Job<Result> job = new MyJob();
 *    //Job<List<Result>> job = new MyJob();
 * 	  e.fork(job);//将任务派发给executer
 * }
 * 将结果汇总后返回
 * List<Result> obj = e.join();
 */
public class Executer {
	// 存储任务的执行结果
	private List<Future> futres = new ArrayList<Future>();
	// 条件队列锁,以及线程计数器
	private final Lock lock = new Lock();
	// 线程池
	private ExecutorService pool = null;

	public Executer() {
		this(1);
	}

	public Executer(int threadPoolSize) {
		pool = Executors.newFixedThreadPool(threadPoolSize);
	}

	/**
	 * 任务派发
	 * @param <E> 任务返回值类型
	 * @param job
	 */
	public <E> void fork(Job<E> job) {
		// 设置同步锁
		job.setLock(lock);
		// 将任务派发给线程池去执行
		futres.add(pool.submit(job));
		// 增加线程数
		synchronized (lock) {
			lock.thread_count++;
		}
	}

	/**
	 * 汇集任务结果
	 * @param <E>
	 * @return E类型的集合
	 */
	public <E> List<E> join() {
		synchronized (lock) {
			while (lock.thread_count > 0) {// 检查线程数，如果为0，则表示所有任务处理完成
				// System.out.println("threadCount: "+THREAD_COUNT);
				try {
					lock.wait();// 如果任务没有全部完成，则挂起。等待完成的任务给予通知
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		List list = new ArrayList();
		// 取出每个任务的处理结果，汇总后返回
		for (Future future : futres) {
			try {
				Object result = future.get();// 因为任务都已经完成，这里直接get
				if (result != null) {
					if (result instanceof Collection)
						list.addAll((Collection) result);
					else
						list.add(result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}