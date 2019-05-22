package ru.sberbank;

import java.util.concurrent.Callable;


public class Task<T> {

  private final Callable<? extends T> callable;
  private volatile RuntimeException taskException;
  private volatile T result;

  public Task(Callable<? extends T> callable) {
    this.callable = callable;
  }

  public T get() {
    T probableResult = checkResult();

    if (probableResult == null) {
      synchronized (this) {
        probableResult = checkResult();

        if (probableResult == null) {
          try {
            result = callable.call();
          } catch (Exception e) {
            taskException = new RuntimeException("Exception in callable.call()");
            throw taskException;
          }
        }

      }
    }

    return result;
  }

  private T checkResult() {
    if (result != null) {
      return result;
    }

    if (taskException != null) {
      throw taskException;
    }

    return null;
  }


}
