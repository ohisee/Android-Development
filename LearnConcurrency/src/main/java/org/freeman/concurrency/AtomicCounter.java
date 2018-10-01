/**
 * 
 */
package org.freeman.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 *
 */
public class AtomicCounter {

  private static AtomicInteger counter = new AtomicInteger(0);

  public static void main(String[] arg) {

    class Incrementer implements Runnable {
      @Override
      public void run() {
        for (int i = 0; i < 1_000; i++) {
          // counter++;
          counter.incrementAndGet();
        }
      }
    }

    class Decrementer implements Runnable {
      @Override
      public void run() {
        for (int i = 0; i < 1_000; i++) {
          // counter--;
          counter.decrementAndGet();
        }
      }
    }

    ExecutorService executorService = Executors.newFixedThreadPool(8);
    List<Future<?>> futures = new ArrayList<>();

    try {
      for (int i = 0; i < 4; i++) {
        futures.add(executorService.submit(new Incrementer()));
      }

      for (int i = 0; i < 4; i++) {
        futures.add(executorService.submit(new Decrementer()));
      }

      futures.forEach(future -> {
        try {
          future.get();
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        }
      });

      System.out.println("Counter = " + counter);

    } finally {
      executorService.shutdown();
      System.out.println("Executor service shut down");
    }
  }

}
