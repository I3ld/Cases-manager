package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity(name = "DeputyHead")
public class DeputyHead extends RegularEmployee {
  private Date promotionDate;
  private Collection<Specialist> subordinates;

  public DeputyHead() { //Required by Hibernate
  }

  public DeputyHead(String firstName, String lastName, BigDecimal salary, Date employmentDate,
      List<String> phoneNumbers,
      RegularEmployeeContractType typeOfContract, Date promotionDate) {
    super(firstName, lastName, salary, employmentDate, phoneNumbers, typeOfContract);
    this.promotionDate = promotionDate;

    super.getRegularEmployeeType().add(RegularEmployeeType.DeputyHead); //add kind to discriminator
  }

  @OneToMany(mappedBy = "supervisor")
  public Collection<Specialist> getSubordinates() {
    return subordinates;
  }

  public void setSubordinates(Collection<Specialist> subordinates) {
    this.subordinates = subordinates;
  }
}
