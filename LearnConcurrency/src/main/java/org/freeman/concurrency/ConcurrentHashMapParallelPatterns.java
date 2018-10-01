/**
 * 
 */
package org.freeman.concurrency;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

/**
 * 
 *
 */
public class ConcurrentHashMapParallelPatterns {

  public static void main(String[] args) {

    ConcurrentHashMap<Actor, Set<Movie>> map = new ConcurrentHashMap<>();

    MovieReader reader = new MovieReader();

    reader.addActorsToMap(map,
        "/Users/accenture/Downloads/jdk8-lambda-tour-master/files/movies-mpaa.txt");

    System.out.println("# Actor " + map.size());

    long start = System.currentTimeMillis();
    int maxMoviesForActor = map.reduce(100, new BiFunction<Actor, Set<Movie>, Integer>() {
      @Override
      public Integer apply(Actor t, Set<Movie> u) {
        return u.size();
      }
    }, new BiFunction<Integer, Integer, Integer>() {
      @Override
      public Integer apply(Integer t, Integer u) {
        return Integer.max(t, u);
      }
    });
    long stop = System.currentTimeMillis();
    System.out.println("Max movies for one actor " + maxMoviesForActor);
    System.out.println("Time " + (stop - start));

    Actor mostSeenActor = map.search(10, new BiFunction<Actor, Set<Movie>, Actor>() {
      @Override
      public Actor apply(Actor t, Set<Movie> u) {
        if (u.size() == maxMoviesForActor) {
          return t;
        }
        return null;
      }
    });
    System.out.println("Most seen actor is " + mostSeenActor);

    int allActorsMovieCount = map.reduce(10, (actor, movies) -> movies.size(), Integer::sum);
    System.out.println("Movie Count " + allActorsMovieCount);

    long a = System.currentTimeMillis();
    map.reduce(10, (actor, movies) -> movies.size(), Integer::sum);
    long b = System.currentTimeMillis();
    System.out.println("Time " + (b - a));

    long c = System.currentTimeMillis();
    map.reduce(30, (actor, movies) -> movies.size(), Integer::sum);
    long d = System.currentTimeMillis();
    System.out.println("Time " + (d - c));
  }

}
