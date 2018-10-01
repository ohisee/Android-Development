/**
 * 
 */
package org.freeman.concurrency;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 *
 */
public class ExecutorPattern {

  /**
   * 
   * @param args
   */
  public static void main(String[] args) {
    /*
     * A single thread
     */
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    Runnable task = () -> {
      System.out.println("A task by " + Thread.currentThread().getName());
    }, onemoretask = () -> System.out.println("More task by " + Thread.currentThread().getName());
    executorService.execute(task);
    executorService.execute(onemoretask);
    executorService.shutdown();

    /*
     * Fixed number of threads
     */
    executorService = Executors.newFixedThreadPool(6);
    for (int i = 0; i < 10; i++) {
      executorService.execute(
          () -> System.out.println("Thread pool task by " + Thread.currentThread().getName()));
    }
    executorService.shutdown();
    
    /*
     * Future <code>Callable</code>
     */
    executorService = Executors.newFixedThreadPool(2);
    Callable<String> callableTask = () -> {
      Thread.sleep(3000);
      return "Callable thread " + Thread.currentThread().getName();
    };
    for (int i = 0; i < 10; i++) {
      Future<String> future = executorService.submit(callableTask);
      try {
        System.out.println("See " + future.get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
    executorService.shutdown();

    /*
     * Future <code>Callable</code>
     */
    executorService = Executors.newFixedThreadPool(2);
    Callable<String> callableTaskTimedOut = () -> {
      Thread.sleep(3000);
      return "Callable thread " + Thread.currentThread().getName();
    };
    for (int i = 0; i < 10; i++) {
      Future<String> future = executorService.submit(callableTaskTimedOut);
      try {
        System.out.println("See " + future.get(1000, TimeUnit.MILLISECONDS));
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (TimeoutException e) {
        e.printStackTrace();
      } finally {
        executorService.shutdown();
      }
    }
    executorService.shutdown();
  }

}
