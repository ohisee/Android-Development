/**
 * 
 */
package org.freeman.concurrency;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 *
 */
public class Movie {

  private String title;
  private int releaseYear;

  private Set<Actor> actors = new HashSet<>();

  public Movie(String title, int releaseYear) {
    super();
    this.title = title;
    this.releaseYear = releaseYear;
  }

  /**
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * @return the releaseYear
   */
  public int getReleaseYear() {
    return releaseYear;
  }

  /**
   * @param releaseYear the releaseYear to set
   */
  public void setReleaseYear(int releaseYear) {
    this.releaseYear = releaseYear;
  }

  /**
   * @return the actors
   */
  public Set<Actor> getActors() {
    return actors;
  }

  /**
   * @param actors the actors to set
   */
  public void addActors(Actor actor) {
    this.actors.add(actor);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((title == null) ? 0 : title.hashCode());
    return result;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Movie other = (Movie) obj;
    if (title == null) {
      if (other.title != null)
        return false;
    } else if (!title.equals(other.title))
      return false;
    return true;
  }

}
