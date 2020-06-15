package model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "Issue")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Issue {

  private Integer id;
  private String description;
  private Date createDate = Date.valueOf(LocalDate.now());
  private IssueStatusType status = IssueStatusType.New; //default = NEW
  private List<EmployeeIssue> employeeIssues = new ArrayList<>();
  private Project project;

  public Issue() { //Required by Hibernate
  }

  public Issue(String description) {
    this.description = description;
  }

  @Id
  @GeneratedValue(generator = "increment")
  @GenericGenerator(name = "increment", strategy = "increment")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Basic
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Enumerated
  public IssueStatusType getStatus() {
    return status;
  }

  public void setStatus(IssueStatusType status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Issue issue = (Issue) o;
    return id == issue.id &&
        Objects.equals(description, issue.description) &&
        Objects.equals(createDate, issue.createDate) &&
        Objects.equals(status, issue.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, description, createDate, status);
  }

  @OneToMany(mappedBy = "issue")
  public List<EmployeeIssue> getEmployeeIssues() {
    return employeeIssues;
  }

  public void setEmployeeIssues(List<EmployeeIssue> employeeIssues) {
    this.employeeIssues = employeeIssues;
  }

  public void addEmployeeIssue(EmployeeIssue employeeIssue) {
    if (!employeeIssues.contains(employeeIssue)) {
      employeeIssues.add(employeeIssue);

      employeeIssue.setIssue(this); //reverse connection
    }
  }

  @ManyToOne
  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public enum IssueStatusType {New, Active, Canceled, Done}
}
