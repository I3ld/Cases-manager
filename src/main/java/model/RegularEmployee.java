package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity(name = "RegularEmployee")
public class RegularEmployee extends Employee {

  //Specialist attributes
  private static int minHoursToWork = 150; //for all specialists
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

  @ManyToOne
  public RegularEmployee getSupervisor() {
    return supervisor;
  }

  public void setSupervisor(RegularEmployee supervisor) {
    this.supervisor = supervisor;
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

  public enum RegularEmployeeType {DeputyHead, Specialist}

  public enum RegularEmployeeContractType {B2B, Mandate, Permanent}
}
