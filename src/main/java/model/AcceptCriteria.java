package model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity(name = "AcceptCriteria")
public class AcceptCriteria extends Issue {

  private Task taskByTaskId;

  public AcceptCriteria() { //Required by Hibernate
  }

  public AcceptCriteria(String description) {
    super(description);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    AcceptCriteria that = (AcceptCriteria) o;
    return taskByTaskId.equals(that.taskByTaskId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), taskByTaskId);
  }

  @ManyToOne
  public Task getTaskByTaskId() {
    return taskByTaskId;
  }

  public void setTaskByTaskId(Task taskByTaskId) {
    this.taskByTaskId = taskByTaskId;
  }
}
