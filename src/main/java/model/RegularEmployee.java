package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import model.Issue.IssueStatusType;

@Entity(name = "RegularEmployee")
public class RegularEmployee extends Employee {

  //Specialist attributes
  private static int minHoursToWork = 150; //for all regular emps
  private RegularEmployee supervisor;

  //Deputy Head attributes
  private Date promotionDate;
  private Collection<RegularEmployee> subordinates = new ArrayList<>();

  //RegularEmployee attributes
  private RegularEmployeeContractType typeOfContract;
  private Set<RegularEmployeeType> regularEmployeeType; //Discriminator


  public RegularEmployee() { //Required by Hibernate
  }

  public RegularEmployee(String firstName, String lastName, BigDecimal salary,
      Date employmentDate, List<String> phoneNumbers,
      RegularEmployeeContractType typeOfContract,
      List<RegularEmployeeType> regularEmployeeType) {
    super(firstName, lastName, salary, employmentDate, phoneNumbers);
    this.typeOfContract = typeOfContract;
    this.regularEmployeeType = EnumSet.copyOf(regularEmployeeType);
  }

  @Enumerated
  public RegularEmployeeContractType getTypeOfContract() {
    return typeOfContract;
  }

  public void setTypeOfContract(RegularEmployeeContractType typeOfContract) {
    this.typeOfContract = typeOfContract;
  }

  @Basic
  public Date getPromotionDate() {
    return promotionDate;
  }

  public void setPromotionDate(Date promotionDate) {
    this.promotionDate = promotionDate;
  }

  @ElementCollection(targetClass = RegularEmployeeType.class)
  @Enumerated
  public Set<RegularEmployeeType> getRegularEmployeeType() {
    return regularEmployeeType;
  }

  public void setRegularEmployeeType(
      Set<RegularEmployeeType> regularEmployeeType) {
    this.regularEmployeeType = regularEmployeeType;
  }

  @OneToMany(mappedBy = "supervisor")
  public Collection<RegularEmployee> getSubordinates() {
    return subordinates;
  }

  public void setSubordinates(Collection<RegularEmployee> subordinates) {
    this.subordinates = subordinates;
  }

  public void addSubordinate(RegularEmployee subordinate) {
    if (!subordinates.contains(subordinate)) {
      subordinates.add(subordinate);
      subordinate.setSupervisor(this); //reverse connection
    }
  }

  @ManyToOne
  public RegularEmployee getSupervisor() {
    return supervisor;
  }

  public void setSupervisor(RegularEmployee supervisor) {
    if (this.supervisor == null && supervisor != null) {
      this.supervisor = supervisor;
      supervisor.addSubordinate(this); //reverse connection
    } else if (this.supervisor != null && !this.supervisor.equals(supervisor)) {
      this.supervisor.getSubordinates().remove(this); //reverse connection
      this.supervisor = supervisor;
      supervisor.addSubordinate(this); //reverse connection
    }
  }

  @Transient
  public List<RegularEmployee> getSubordinatesOrderedByOpenCases() throws Exception {
    if (this.regularEmployeeType.contains(RegularEmployeeType.DeputyHead)) {
      Comparator<RegularEmployee> casesComparator = (e1, e2) -> Long.compare(e2.getEmployeeIssues()
              .stream()
              .filter(is -> is.getIssue().getStatus().equals(IssueStatusType.Done)).count(),
          e1.getEmployeeIssues().stream()
              .filter(is -> is.getIssue().getStatus().equals(IssueStatusType.Done)).count());

      return subordinates.stream()
          .filter(emp -> emp.getEmployeeIssues().size() > 0)
          .sorted(casesComparator)
          .collect(Collectors.toCollection(ArrayList::new));
    } else {
      throw new Exception("Employee must be Deputy Head to get subordinates!");
    }
  }

  //get deputy head seniority in years (i.a. needed to count extra bonus)
  @Transient
  public int getSeniorityYears() throws Exception {
    if (this.getRegularEmployeeType().contains(RegularEmployeeType.DeputyHead)) {
      LocalDate now = LocalDate.now();
      return Period.between(super.getEmploymentDate().toLocalDate(), now).getYears();
    } else {
      throw new Exception("Employee must be Deputy Head to get seniority!");
    }
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
    RegularEmployee that = (RegularEmployee) o;
    return Objects.equals(supervisor, that.supervisor) &&
        Objects.equals(promotionDate, that.promotionDate) &&
        Objects.equals(subordinates, that.subordinates) &&
        typeOfContract == that.typeOfContract &&
        Objects.equals(regularEmployeeType, that.regularEmployeeType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), supervisor, promotionDate, subordinates, typeOfContract,
        regularEmployeeType);
  }

  @Override
  public BigDecimal countExtraBonus(int workedHours) throws Exception {
    if (workedHours > minHoursToWork) {
      if (this.getRegularEmployeeType().contains(RegularEmployeeType.DeputyHead)) {
        return BigDecimal.valueOf(getSeniorityYears() * 250);
      } else if (this.getRegularEmployeeType().contains(RegularEmployeeType.Specialist) && !this
          .getRegularEmployeeType().contains(RegularEmployeeType.DeputyHead)) {
        return BigDecimal.valueOf(workedHours * 3);
      }
    }
    return BigDecimal.valueOf(0);
  }

  public enum RegularEmployeeType {DeputyHead, Specialist}

  public enum RegularEmployeeContractType {B2B, Mandate, Permanent}
}
