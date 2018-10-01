/**
 * 
 */
package org.freeman.concurrency;

/**
 *
 *
 */
public class Actor {

  private String lastName;
  private String firstName;
  private String actorFullName;

  public Actor(String lastName, String firstName, String actorFullName) {
    super();
    this.lastName = lastName;
    this.firstName = firstName;
    this.actorFullName = actorFullName;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the actorFullName
   */
  public String getActorFullName() {
    return actorFullName;
  }

  /**
   * @param actorFullName the actorFullName to set
   */
  public void setActorFullName(String actorFullName) {
    this.actorFullName = actorFullName;
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
    result = prime * result + ((actorFullName == null) ? 0 : actorFullName.hashCode());
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
    Actor other = (Actor) obj;
    if (actorFullName == null) {
      if (other.actorFullName != null)
        return false;
    } else if (!actorFullName.equals(other.actorFullName))
      return false;
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Actor [lastName=" + lastName + ", firstName=" + firstName + ", actorFullName="
        + actorFullName + "]";
  }

}
