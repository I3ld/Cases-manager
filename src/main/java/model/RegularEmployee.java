package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Transient;

@Entity(name = "RegularEmployee")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class RegularEmployee extends Employee {

  private RegularEmployeeContractType typeOfContract;
  private Integer supervisorId;
  private EnumSet<RegularEmployeeType> regularEmployeeType = EnumSet.of(RegularEmployeeType.RegularEmployee); //Discriminator

  public RegularEmployee() { //Required by Hibernate
  }

  public RegularEmployee(String firstName, String lastName, BigDecimal salary, Date employmentDate,
      List<String> phoneNumbers,
      RegularEmployeeContractType typeOfContract) {
    super(firstName, lastName, salary, employmentDate, phoneNumbers);
    this.typeOfContract = typeOfContract;
  }

  @Basic
  public RegularEmployeeContractType getTypeOfContract() {
    return typeOfContract;
  }

  public void setTypeOfContract(RegularEmployeeContractType typeOfContract) {
    this.typeOfContract = typeOfContract;
  }

  @Basic
  public Integer getSupervisorId() {
    return supervisorId;
  }

  public void setSupervisorId(Integer supervisorId) {
    this.supervisorId = supervisorId;
  }

  @Transient
  public EnumSet<RegularEmployeeType> getRegularEmployeeType() {
    return regularEmployeeType;
  }

  public void setRegularEmployeeType(
      EnumSet<RegularEmployeeType> regularEmployeeType) {
    this.regularEmployeeType = regularEmployeeType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RegularEmployee that = (RegularEmployee) o;
    return Objects.equals(typeOfContract, that.typeOfContract) &&
        Objects.equals(supervisorId, that.supervisorId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(typeOfContract, supervisorId);
  }

  public enum RegularEmployeeType {DeputyHead, Specialist, RegularEmployee}
  public enum RegularEmployeeContractType {B2B, Mandate, Permanent}
}
