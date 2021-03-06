package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.hibernate.Session;
import org.hibernate.annotations.GenericGenerator;
import util.HibernateUtil;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Employee {

  private int id;
  private String firstName;
  private String lastName;
  private BigDecimal salary;
  private Date employmentDate;
  private List<String> phoneNumbers;

  private Project project;
  private List<EmployeeIssue> employeeIssues = new ArrayList<>();

  public Employee() { //Required by Hibernate
  }

  public Employee(String firstName, String lastName, BigDecimal salary, Date employmentDate,
      List<String> phoneNumbers) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.salary = salary;
    this.employmentDate = employmentDate;
    this.phoneNumbers = phoneNumbers;
  }

  @Transient
  public static List<Employee> getEmploeesByProject(Project project) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    return session.createQuery("from Employee where project = :project")
        .setParameter("project", project).list();
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
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Basic
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Basic
  public BigDecimal getSalary() {
    return salary;
  }

  public void setSalary(BigDecimal salary) {
    this.salary = salary;
  }

  @Basic
  public Date getEmploymentDate() {
    return employmentDate;
  }

  public void setEmploymentDate(Date employmentDate) {
    this.employmentDate = employmentDate;
  }

  @ElementCollection
  public List<String> getPhoneNumber() {
    return phoneNumbers;
  }

  public void setPhoneNumber(List<String> phoneNumbers) {
    this.phoneNumbers = phoneNumbers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Employee employee = (Employee) o;
    return id == employee.id &&
        salary == employee.salary &&
        Objects.equals(firstName, employee.firstName) &&
        Objects.equals(lastName, employee.lastName) &&
        Objects.equals(employmentDate, employee.employmentDate) &&
        Objects.equals(phoneNumbers, employee.phoneNumbers);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, firstName, lastName, salary, employmentDate, phoneNumbers);
  }

  @ManyToOne
  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    if (this.project == null && project != null) {
      this.project = project;
      project.addEmployee(this); //reverse connection
    } else if (project != null && !this.project.equals(project)) {
      this.project.getEmployees().remove(this); //reverse connection
      this.project = project;
      project.addEmployee(this); //reverse connection
    }
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

      employeeIssue.setEmployee(this); //reverse connection
    }
  }

  abstract public BigDecimal countExtraBonus(int workedHours) throws Exception;
}
