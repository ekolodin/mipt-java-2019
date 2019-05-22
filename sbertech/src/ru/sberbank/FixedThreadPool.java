package ru.sberbank;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FixedThreadPool implements ThreadPool {

  private final Queue<Runnable> workQueue = new ConcurrentLinkedQueue<>();
  private volatile boolean isRunning = true;
  private final int numberOfThreads;

  private int completed = 0;
  private int failed = 0;


  private synchronized void incCompleted() {
    ++completed;
  }


  int getCompletedTasks() {
    return completed;
  }


  private synchronized void incFailed() {
    ++failed;
  }


  int getFailedTasks() {
    return failed;
  }


  int getQueueSize() {
    return workQueue.size();
  }


  public FixedThreadPool(int numberOfThreads) {
    this.numberOfThreads = numberOfThreads;
  }


  @Override
  public void start() {
    for (int i = 0; i < numberOfThreads; i++) {
      new Thread(new Worker()).start();
    }
  }


  @Override
  public void execute(Runnable command) {
    if (isRunning) {
      workQueue.offer(command);
    }
  }


  public void shutdown() {
    isRunning = false;
  }


  private final class Worker implements Runnable {

    @Override
    public void run() {

      while (isRunning) {
        Runnable nextTask = workQueue.poll();
        if (nextTask != null) {
          try {
            nextTask.run();
            incCompleted();
          } catch (Exception e) {
            e.printStackTrace();
            incFailed();
          }
        }
      }
    }


  }


  public static void main(String[] args) {
    FixedThreadPool threadPool = new FixedThreadPool(5);
    threadPool.start();

    for (int j = 0; j < 10; ++j) {
      threadPool.execute(() -> {
        double sum = 0;
        for (int i = 0; i < 10_000_000; ++i) {
          sum += Math.random();
        }

        System.out.println(Thread.currentThread().getName());
        System.out.println(sum);
      });
    }

    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    threadPool.shutdown();


  }
}