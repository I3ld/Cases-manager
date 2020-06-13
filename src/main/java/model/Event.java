package model;

import java.sql.Date;
import java.util.Collection;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Event extends Issue {

  private String title;
  private Date endDate;
  private Collection<Task> tasksById;

  public Event() { //Required by Hibernate
  }

  public Event(String description, String title, Date endDate) {
    super(description);
    this.title = title;
    this.endDate = endDate;
  }

  @Basic
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Basic
  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Event event = (Event) o;
    return Objects.equals(title, event.title) &&
        Objects.equals(endDate, event.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, endDate);
  }

  @OneToMany(mappedBy = "eventByEventId")
  public Collection<Task> getTasksById() {
    return tasksById;
  }

  public void setTasksById(Collection<Task> tasksById) {
    this.tasksById = tasksById;
  }
}
