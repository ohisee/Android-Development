/**
 * 
 */
package org.freeman.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
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
public class Worker implements Callable<List<Integer>> {

  private CyclicBarrier cyclicBarrier;
  private List<Integer> inputList;

  public Worker(CyclicBarrier cyclicBarrier, List<Integer> inputList) {
    this.cyclicBarrier = cyclicBarrier;
    this.inputList = inputList;
  }

  public List<Integer> lookInto(List<Integer> inputList) {
    return inputList;
  }

  @Override
  public List<Integer> call() throws Exception {
    List<Integer> result = lookInto(inputList);
    try {
      cyclicBarrier.await();
    } catch (InterruptedException | BrokenBarrierException e) {
      e.printStackTrace();
    }
    return result;
  }

  /**
   * Main method.
   * 
   * @param args
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public static void main(String[] args) throws InterruptedException, ExecutionException {

    CyclicBarrier cyclicBarrier = new CyclicBarrier(4);
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    Worker worker1 = new Worker(cyclicBarrier, new ArrayList<>());

    Future<List<Integer>> futureResult1 = executorService.submit(worker1);

    List<Integer> result = new ArrayList<>();
    result.addAll(futureResult1.get());
    
    executorService.shutdown();
  }

}
