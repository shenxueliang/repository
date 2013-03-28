repository
==========

简单的java并行计算框架
/**
 * 并行计算框架
 * @author xueliang
 * 例子：
 * 创建10个线程
 * Executer e = new Executer(10);
 * for(...){
 *     Job<Result> job = new MyJob();
 *    //Job<List<Result>> job = new MyJob();
 * 	  e.fork(job);//将任务派发给executer
 * }
 * 将结果汇总后返回
 * List<Result> obj = e.join();
 */
