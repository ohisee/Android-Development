/**
 * 
 */
package org.freeman.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 
 *
 */
public class CountDownLatchInAction {

  /**
   * Main method.
   * 
   * @param args
   */
  public static void main(String[] args) {

    class Friend implements Callable<Void> {

      private CountDownLatch countDownLatch;

      public Friend(CountDownLatch countDownLatch) {
        super();
        this.countDownLatch = countDownLatch;
      }

      @Override
      public Void call() throws Exception {
        try {
          Random rand = new Random();
          Thread.sleep(rand.nextInt(20) * 100 + 1000);
          System.out.println("Service completed, waiting for the others...");

          countDownLatch.countDown();

        } catch (InterruptedException e) {
          System.out.println(e.getMessage());
        }
        
        return null;
      }
    }

    ExecutorService executorService = Executors.newFixedThreadPool(4);

    CountDownLatch countDownLatch = new CountDownLatch(3);
    List<Future<Void>> futures = new ArrayList<>();

    try {
      for (int i = 0; i < 4; i++) {
        Friend friend = new Friend(countDownLatch);
        futures.add(executorService.submit(friend));
      }
      
      try {
        countDownLatch.await(10, TimeUnit.SECONDS);
        System.out.println("Count down latch done");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    } finally {
      executorService.shutdown();
      System.out.println("Executor service shut down");
    }

  }

}
