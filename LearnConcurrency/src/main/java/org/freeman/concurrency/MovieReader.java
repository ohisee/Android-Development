/**
 * 
 */
package org.freeman.concurrency;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 *
 */
public class MovieReader {

  /**
   * Read movie file
   * 
   * @param uri
   * @return movie set
   */
  public Set<Movie> readMovies(String uri) {

    try (Stream<String> lines = Files.lines(Paths.get(uri), StandardCharsets.ISO_8859_1)) {

      Set<Actor> allActors = new HashSet<>();

      Set<Movie> movies = lines.map(

          (String line) -> {
            String[] elements = line.split("/");
            String title = extractTitle(elements[0]);
            int releaseYear = extractReleaseYear(elements[0]);
            Movie movie = new Movie(title, releaseYear);
            Arrays.stream(elements).skip(1).map(MovieReader::extractActor)
                .forEach(movie::addActors);
            Arrays.stream(elements).skip(1).map(MovieReader::extractActor).forEach(allActors::add);
            return movie;
          }

      ).collect(Collectors.toSet());

      System.out.println("# All actors " + allActors.size());

      return movies;

    } catch (IOException e) {
      e.printStackTrace();
    }

    return new HashSet<>();
  }

  /**
   * Add actors to map
   * 
   * @param map
   * @param uri
   */
  public void addActorsToMap(Map<Actor, Set<Movie>> map, String uri) {
    Set<Movie> movies = readMovies(uri);
    for (Movie movie : movies) {
      for (Actor actor : movie.getActors()) {
        map.computeIfAbsent(actor, value -> new HashSet<>()).add(movie);
      }
    }
  }

  /**
   * Extractor actor
   * 
   * @param elements
   * @return Actor
   */
  private static Actor extractActor(String elements) {
    String[] nameElements = elements.split(",[\\s]+");
    String lastName = extractLastName(nameElements);
    String firstName = extractFirstName(nameElements);
    return new Actor(lastName, firstName, elements);
  }

  /**
   * Extract last name
   * 
   * @param nameElements
   * @return last name
   */
  private static String extractLastName(String[] nameElements) {
    return nameElements[0];
  }

  /**
   * Extract first name
   * 
   * @param nameElements
   * @return first name
   */
  private static String extractFirstName(String[] nameElements) {
    if (nameElements.length < 2) {
      return nameElements[0];
    }
    return nameElements[1];
  }

  /**
   * Extract title
   * 
   * @param element
   * @return title
   */
  private String extractTitle(String element) {
    int i = StringUtils.lastIndexOf(element, "("), j = StringUtils.lastIndexOf(element, ")");
    if (i > -1 && j > -1 && i < j) {
      return StringUtils.substring(element, 0, i).trim();
    }
    return "No Title";
  }

  /**
   * Extract release year
   * 
   * @param element
   * @return release year
   */
  private int extractReleaseYear(String element) {
    int i = StringUtils.lastIndexOf(element, "("), j = StringUtils.lastIndexOf(element, ")");
    String y = "";
    if (i > -1 && j > -1 && i < j) {
      y = StringUtils.substring(element, i + 1, j);
    }
    try {
      return Integer.parseInt(y);
    } catch (NumberFormatException e) {
      return 0;
    }
  }

}
