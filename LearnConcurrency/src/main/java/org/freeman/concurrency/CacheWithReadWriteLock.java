/**
 * 
 */
package org.freeman.concurrency;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Cache with read write lock.
 *
 */
public class CacheWithReadWriteLock {

  private Map<Long, String> cache = new HashMap<>();
  private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
  private Lock readLock = readWriteLock.readLock();
  private Lock writeLock = readWriteLock.writeLock();

  public String put(Long key, String value) {
    writeLock.lock();
    try {
      return cache.put(key, value);
    } finally {
      System.out.println(String.format("Producer put Key %s Value %s by task %s", key, value,
          Thread.currentThread().getName()));
      writeLock.unlock();
    }
  }

  /**
   * Read operations can be made in Parallel, many read will not be blocked.
   * 
   * @param key
   * @return value or null
   */
  public String get(Long key) {
    readLock.lock();
    try {
      return cache.get(key);
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Main method.
   * 
   * @param args
   */
  public static void main(String[] args) {

    CacheWithReadWriteLock cache = new CacheWithReadWriteLock();

    class Producer implements Callable<String> {

      private Random rand = new Random();

      @Override
      public String call() throws Exception {
        int count = 0;
        while (count++ < 5) {
          long key = rand.nextInt(1_000);
          cache.put(key, Long.toString(key));
          if (cache.get(key) == null) {
            System.out.println(String.format("Key %s has not been put in the cache map", key));
          }
        }
        return "Done adding in cache map";
      }
    }

    ExecutorService executorService = Executors.newFixedThreadPool(4);

    System.out.println("Adding values in cache map");

    try {
      for (int i = 0; i < 4; i++) {
        executorService.submit(new Producer());
      }
    } finally {
      executorService.shutdown();
      System.out.println("Executor service shut down");
    }

  }

}
