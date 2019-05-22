package ru.sberbank;

public class Semaphore {

  private final int capacity;
  private int countThreads = 0;

  Semaphore(int capacity) {
    this.capacity = capacity;
  }

  synchronized void lock() {

    while (countThreads >= capacity) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    ++countThreads;
  }

  synchronized void release() {
    --countThreads;
    notify();
  }

  public static void main(String[] args) {
    Semaphore semaphore = new Semaphore(4);

    for (int i = 0; i < 10; ++i) {
      new Thread(() -> {
        semaphore.lock();

        System.out.println(Thread.currentThread().getName());
        try {
          Thread.sleep(8000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

        semaphore.release();
      }).start();
    }

  }
}
