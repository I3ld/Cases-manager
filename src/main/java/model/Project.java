package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;

@Entity
public class Project {

  private String name;
  private String description;
  private Date createDate;
  private BigDecimal budget;
  private Date expectedEndDate;
  private Date endDate;
  private Collection<Employee> employees;
  private Map<Integer, Issue> issuesQualified = new TreeMap<>();

  public Project() { //Required by Hibernate
  }

  public Project(String name, String description, Date createDate, BigDecimal budget,
      Date expectedEndDate) {
    this.name = name;
    this.description = description;
    this.createDate = createDate;
    this.budget = budget;
    this.expectedEndDate = expectedEndDate;
  }

  @Id
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  @Basic
  public BigDecimal getBudget() {
    return budget;
  }

  public void setBudget(BigDecimal budget) {
    this.budget = budget;
  }

  @Basic
  public Date getExpectedEndDate() {
    return expectedEndDate;
  }

  public void setExpectedEndDate(Date expectedEndDate) {
    this.expectedEndDate = expectedEndDate;
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
    Project project = (Project) o;
    return budget == project.budget &&
        Objects.equals(name, project.name) &&
        Objects.equals(description, project.description) &&
        Objects.equals(createDate, project.createDate) &&
        Objects.equals(expectedEndDate, project.expectedEndDate) &&
        Objects.equals(endDate, project.endDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, description, createDate, budget, expectedEndDate, endDate);
  }

  @OneToMany(mappedBy = "project")
  public Collection<Employee> getEmployees() {
    return employees;
  }

  public void setEmployees(Collection<Employee> employees) {
    this.employees = employees;
  }

  //qualified association Issue - Project
  //Required by hibernate
  @OneToMany(mappedBy = "project")
  @MapKey(name = "id")
  public Map<Integer, Issue> getIssuesQualified() {
    return issuesQualified;
  }

  public void setIssuesQualified(Map<Integer, Issue> issuesQualified) {
    this.issuesQualified = issuesQualified;
  }

  //qualified association Issue - Project
  //Methods - get/add issue to map extent
  public Issue getIssuesQualifiedById(int id) throws Exception {
    // Check if we have the info
    if (!issuesQualified.containsKey(id)) {
      throw new Exception("Unable to find a issue with id: " + id);
    }
    return issuesQualified.get(id);
  }

  public void addIssueQualified(Issue issue) {
    // Check if we already have the info
    if (!issuesQualified.containsKey(issue.getId())) {
      issuesQualified.put(issue.getId(), issue);

      // Add the reverse connection
      issue.setProject(this);
    }
  }
}
