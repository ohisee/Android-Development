/**
 * 
 */
package org.freeman.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 *
 */
public class BarrierInAction {

  /**
   * Main method.
   * 
   * @param args
   */
  public static void main(String[] args) {

    class Friend implements Callable<String> {

      private CyclicBarrier cyclicBarrier;

      public Friend(CyclicBarrier cyclicBarrier) {
        super();
        this.cyclicBarrier = cyclicBarrier;
      }

      @Override
      public String call() throws Exception {
        try {
          Random rand = new Random();
          Thread.sleep(rand.nextInt(20) * 100 + 100);
          System.out.println("I just arrived, waiting for the others...");

          // cyclicBarrier.await(5, TimeUnit.SECONDS);

          cyclicBarrier.await();

          System.out.println("Let us go");
          return "Done";
        } catch (InterruptedException e) {
          System.out.println(e.getMessage());
        }
        return "Interrupted, not Done";
      }
    }

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    CyclicBarrier cyclicBarrier = new CyclicBarrier(4, new Runnable() {
      @Override
      public void run() {
        System.out.println("Cyclic Barrier opening");
      }
    });
    List<Future<String>> futures = new ArrayList<>();

    try {
      for (int i = 0; i < 4; i++) {
        Friend friend = new Friend(cyclicBarrier);
        futures.add(executorService.submit(friend));
      }

      futures.forEach(future -> {
        try {
          // System.out.println("See " + future.get(200, TimeUnit.MICROSECONDS));
          future.get();
        } catch (InterruptedException | ExecutionException e) {
          e.printStackTrace();
        } /*
           * catch (TimeoutException e) { System.out.println("Timed out"); future.cancel(true); //
           * interrupt }
           */
      });

    } finally {
      executorService.shutdown();
      System.out.println("Executor service shut down");
    }
  }

}
