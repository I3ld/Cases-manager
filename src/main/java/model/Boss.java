package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity(name = "Boss")
public class Boss extends Employee {

  private BigDecimal budget;
  private List<Contract> contracts = new ArrayList<>();

  public Boss() { //Required by Hibernate
  }

  public Boss(String firstName, String lastName, BigDecimal salary, Date employmentDate,
      List<String> phoneNumbers, BigDecimal budget) {
    super(firstName, lastName, salary, employmentDate, phoneNumbers);
    this.budget = budget;
  }

  @Basic
  public BigDecimal getBudget() {
    return budget;
  }

  public void setBudget(BigDecimal budget) {
    this.budget = budget;
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
    Boss boss = (Boss) o;
    return Objects.equals(budget, boss.budget) &&
        Objects.equals(contracts, boss.contracts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), budget, contracts);
  }

  //Boss extra bonus - 2% of current budget
  @Override
  public BigDecimal countExtraBonus(int workedHours) {
    if (workedHours > 80 && budget.compareTo(BigDecimal.ZERO) > 0) {
      return budget.multiply(new BigDecimal(2)).movePointLeft(2);
    }
    return BigDecimal.valueOf(0);
  }

  @OneToMany(mappedBy = "boss")
  public List<Contract> getContracts() {
    return contracts;
  }

  public void setContracts(List<Contract> contracts) {
    this.contracts = contracts;
  }

  public void addContract(Contract contract) {
    if (!contracts.contains(contract)) {
      contracts.add(contract);

      contract.setBoss(this); //reverse connection
    }
  }
}
