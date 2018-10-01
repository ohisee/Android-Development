/**
 * 
 */
package org.freeman.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 *
 */
public class LockPattern {

  /**
   * Synchronization pattern, passing an instance of object
   *
   */
  @SuppressWarnings("unused")
  private class Person {
    private final Object key = new Object();

    public String init() {
      synchronized (key) {
        return "";
      }
    }
  }

  public void lll() {
    Lock lock = new ReentrantLock();
    // new ReentrantLock(true); fair lock
    try {
      lock.lock();
      // do thing here
    } finally {
      lock.unlock();
    }

    // timed lock acquisition
    if (lock.tryLock()) {
      try {
        // guarded block of code
      } finally {
        lock.unlock();
      }
    } else {

    }

    // timed lock acquisition
    try {
      if (lock.tryLock(1, TimeUnit.SECONDS)) {
        try {
          // guarded block of code
        } finally {
          lock.unlock();
        }
      } else {

      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  // public void demo() {
  // Lock lock = new ReentrantLock();
  // Condition notFull = lock.newCondition();
  // Condition notEmpty = lock.newCondition();
  // String[] buffer = new String[100];
  // class Producer {
  // public void produce() throws InterruptedException {
  // try {
  // lock.lock();
  // while (isFull()) {
  // notFull.await();
  // }
  // notEmpty.signal();
  // } finally {
  // lock.unlock();
  // }
  // }
  //
  // public boolean isFull() {
  // return false;
  // }
  // }
  //
  // class Consumer {
  // public void consume() throws InterruptedException {
  // try {
  // lock.lock();
  // while (isEmpty()) {
  // notEmpty.await();
  // }
  // notFull.signal();
  // } finally {
  // lock.unlock();
  // }
  // }
  //
  // public boolean isEmpty() {
  // return false;
  // }
  // }
  // }

  /**
   * Main method.
   * 
   * @param args
   */
  public static void main(String[] args) {

    List<Integer> buffer = new ArrayList<Integer>();

    Lock lock = new ReentrantLock();
    Condition isEmpty = lock.newCondition();
    Condition isFull = lock.newCondition();

    /**
     * 
     * Consumer
     *
     */
    class Consumer implements Callable<String> {

      boolean isEmpty(List<Integer> buffer) {
        return buffer.isEmpty();
      }

      @Override
      public String call() throws InterruptedException, TimeoutException {
        int count = 0;

        while (count++ < 50) {
          try {

            lock.lock();
            System.out.println(String.format("\nConsumer task by %s after lock",
                Thread.currentThread().getName()));
            while (isEmpty(buffer)) {
              // wait - not releasing executing thread
              // If there is an exception from Producer, Consumer thread stops and continues waiting
              // at this point. So, must set a time out in await method.
              // isEmpty.await()
              if (!isEmpty.await(10, TimeUnit.MILLISECONDS)) {
                throw new TimeoutException("Consume timed out");
              }
            }
            buffer.remove(buffer.size() - 1);
            // signal
            isFull.signalAll();

          } finally {
            System.out.println(String.format("Consumer task by %s before unlock\n",
                Thread.currentThread().getName()));
            lock.unlock();
          }
        }

        return String.format("Consumed %s", (count - 1));
      }
    }

    /**
     * 
     * Producer
     *
     */
    class Producer implements Callable<String> {

      boolean isFull(List<Integer> buffer) {
        boolean result = (buffer.size() == 50);
        if (result) {
          System.out.println(String.format("Producer buffer full [%s] size %s",
              Thread.currentThread().getName(), buffer.size()));
        } else {
          System.out.println(String.format("Producer buffer not full [%s] size %s ",
              Thread.currentThread().getName(), buffer.size()));
        }
        return result;
      }

      @Override
      public String call() throws Exception {
        int count = 0;

        while (count++ < 50) {
          try {

            lock.lock();
            System.out.println(String.format("Producer task by [%s] after lock at count %s",
                Thread.currentThread().getName(), count));
            // throw some exception
            while (isFull(buffer)) {
              // wait - not releasing executing thread
              isFull.await();
            }
            buffer.add(1);
            // signal
            isEmpty.signalAll();

          } finally {
            System.out.println(String.format("Producer task by [%s] before unlock\n",
                Thread.currentThread().getName()));
            lock.unlock();
          }
        }
        System.out.println(String.format("Producer task by [%s] out of while less than\n",
            Thread.currentThread().getName()));
        return String.format("Produced %s", (count - 1));
      }
    }

    List<Producer> producers = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      producers.add(new Producer());
    }

    List<Consumer> consumers = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
      consumers.add(new Consumer());
    }

    System.out.println("Producers and Consumers launched");

    List<Callable<String>> producersAndConsumers = new ArrayList<>();
    producersAndConsumers.addAll(producers);
    producersAndConsumers.addAll(consumers);

    ExecutorService executorService = Executors.newFixedThreadPool(8);

    try {

      List<Future<String>> futures = executorService.invokeAll(producersAndConsumers);

      futures.forEach(future -> {
        try {
          System.out.println("See Future " + future.get());
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (ExecutionException e) {
          e.printStackTrace();
        }
      });

    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      executorService.shutdown();
      System.out.println("Executor service shut down");
    }
  }

}
