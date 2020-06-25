package model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Task extends Issue {

  private String title;
  private int priority;
  private Date dueToDate;
  private List<AcceptCriteria> acceptCriteriaById = new ArrayList<AcceptCriteria>();
  private Event eventByEventId;

  public Task() { //Required by Hibernate
  }

  public Task(String description, String title, int priority,
      Date dueToDate) {
    super(description);
    this.title = title;
    this.priority = priority;
    this.dueToDate = dueToDate;
  }

  @Basic
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Basic
  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  @Basic
  public Date getDueToDate() {
    return dueToDate;
  }

  public void setDueToDate(Date dueToDate) {
    this.dueToDate = dueToDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Task task = (Task) o;
    return priority == task.priority &&
        Objects.equals(title, task.title) &&
        Objects.equals(dueToDate, task.dueToDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, priority, dueToDate);
  }

  //deletes parent and orphans - delete all acc criteria with task
  @OneToMany(mappedBy = "taskByTaskId", cascade = CascadeType.ALL)
  public List<AcceptCriteria> getAcceptCriteriaById() {
    return acceptCriteriaById;
  }

  public void setAcceptCriteriaById(List<AcceptCriteria> acceptCriteriaById) {
    this.acceptCriteriaById = acceptCriteriaById;
  }

  public void addAcceptCriteria(AcceptCriteria acc){
    if(!acceptCriteriaById.contains(acc)){
      acceptCriteriaById.add(acc);

      acc.setTaskByTaskId(this); //reverse connection
    }
  }

  @ManyToOne
  public Event getEventByEventId() {
    return eventByEventId;
  }

  public void setEventByEventId(Event eventByEventId) {
    if (this.eventByEventId == null && eventByEventId != null) {
      this.eventByEventId = eventByEventId;
      eventByEventId.getTasksById().add(this); //reverse connection
    } else if (this.eventByEventId != null && !this.eventByEventId.equals(eventByEventId)) {
      this.eventByEventId.getTasksById().remove(this); //reverse connection
      this.eventByEventId = eventByEventId;
      eventByEventId.getTasksById().add(this); //reverse connection
    }
  }
}
