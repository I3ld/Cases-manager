package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity(name="Specialist")
public class Specialist extends RegularEmployee {

  private static int minHoursToWork = 150;
  private DeputyHead supervisor;

  public Specialist() { //Required by Hibernate
  }

  public Specialist(String firstName, String lastName, BigDecimal salary,
      Date employmentDate, List<String> phoneNumbers,
      RegularEmployeeContractType typeOfContract, DeputyHead supervisor) {
    super(firstName, lastName, salary, employmentDate, phoneNumbers, typeOfContract);
    this.supervisor = supervisor;

    super.getRegularEmployeeType().add(RegularEmployeeType.Specialist); //add kind to discriminator
  }

  @ManyToOne
  public DeputyHead getSupervisor() {
    return supervisor;
  }

  public void setSupervisor(DeputyHead supervisor) {
    this.supervisor = supervisor;
  }
}
