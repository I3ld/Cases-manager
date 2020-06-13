package model;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
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

  private int id;
  private String description;
  private Date createDate = Date.valueOf(LocalDate.now());
  private IssueStatusType status; // default NEW
  private Collection<EmployeeIssue> employeeIssues;
  private Project project;

  public Issue() { //Required by Hibernate
  }

  public Issue(String description) {
    this.description = description;

    status = IssueStatusType.New;
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
  public Collection<EmployeeIssue> getEmployeeIssues() {
    return employeeIssues;
  }

  public void setEmployeeIssues(Collection<EmployeeIssue> employeeIssues) {
    this.employeeIssues = employeeIssues;
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
