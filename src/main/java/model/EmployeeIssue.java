package model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "EmployeeIssue")
public class EmployeeIssue {

  private int id;
  private Date closeDate = Date.valueOf(LocalDate.now()); //default now
  private String comment;
  private Employee employee;
  private Issue issue;

  public EmployeeIssue() { //Required by Hibernate
  }

  public EmployeeIssue(String comment, Employee employee, Issue issue) {
    this.comment = comment;
    this.employee = employee;
    this.issue = issue;

    employee.addEmployeeIssue(this);
    issue.addEmployeeIssue(this);
  }

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  public Date getCloseDate() {
    return closeDate;
  }

  public void setCloseDate(Date closeDate) {
    this.closeDate = closeDate;
  }

  @Basic
  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmployeeIssue that = (EmployeeIssue) o;
    return
        Objects.equals(closeDate, that.closeDate) &&
            Objects.equals(comment, that.comment);
  }

  @Override
  public int hashCode() {
    return Objects.hash(closeDate, comment);
  }

  @ManyToOne
  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    if (this.employee == null && employee != null) {
      this.employee = employee;
      employee.addEmployeeIssue(this); //reverse connection
    } else if (employee != null && !this.employee.equals(employee)) {
      this.employee.getEmployeeIssues().remove(this); //reverse connection
      this.employee = employee;
      employee.addEmployeeIssue(this); //reverse connection
    }
  }

  @ManyToOne
  public Issue getIssue() {
    return issue;
  }

  public void setIssue(Issue issue) {
    if (this.issue == null && issue != null) {
      this.issue = issue;
      issue.addEmployeeIssue(this); //reverse connection
    } else if (issue != null && !this.issue.equals(issue)) {
      this.issue.getEmployeeIssues().remove(this); //reverse connection
      this.issue = issue;
      issue.addEmployeeIssue(this); //reverse connection
    }
  }
}
