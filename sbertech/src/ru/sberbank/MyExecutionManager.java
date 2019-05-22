package ru.sberbank;

import static java.lang.Thread.sleep;
import static java.lang.Thread.yield;


public class MyExecutionManager implements ExecutionManager {

  private final FixedThreadPool threadPool;

  MyExecutionManager(FixedThreadPool threadPool) {

    this.threadPool = threadPool;
    threadPool.start();
  }

  @Override
  public Context execute(Runnable callback, Runnable... tasks) {

    new Thread(() -> {
      for (Runnable task : tasks) {
        threadPool.execute(task);
      }

      while (threadPool.getCompletedTasks() + threadPool.getFailedTasks() < tasks.length) {
        yield();
      }

      threadPool.execute(callback);
    }).start();

    return new Context() {
      @Override
      public int getCompletedTaskCount() {
        return threadPool.getCompletedTasks();
      }

      @Override
      public int getFailedTaskCount() {
        return threadPool.getFailedTasks();
      }

      @Override
      public int getInterruptedTaskCount() {
        return threadPool.getQueueSize();
      }

      @Override
      public void interrupt() {
        threadPool.shutdown();
      }

      @Override
      public boolean isFinished() {
        return threadPool.getQueueSize() == 0;
      }
    };
  }

  public static void main(String[] args) {
    FixedThreadPool fixedThreadPool = new FixedThreadPool(3);
    MyExecutionManager myExecutionManager = new MyExecutionManager(fixedThreadPool);

    Runnable task = () -> {
      double sum = 0;
      for (int i = 0; i < 20_000_000; ++i) {
        sum += Math.random();
      }

      System.out.println(Thread.currentThread().getName());
      System.out.println(sum);
    };

    Context context = myExecutionManager
        .execute(() -> System.out.println("It is callback "), task, task, task);

    try {
      sleep(7000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    context.interrupt();

    System.out.println(context.getCompletedTaskCount());

    System.out.println(context.getFailedTaskCount());

  }
}
