package model;

import java.sql.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "Contract")
public class Contract {

  private int id;
  private Date startDate;
  private String scope;
  private Date deliveryProductDate;
  private Company company;
  private Boss boss;

  public Contract() { //Required by Hibernate
  }

  public Contract(Date startDate, String scope, Date deliveryProductDate, Company company,
      Boss boss) {
    this.startDate = startDate;
    this.scope = scope;
    this.deliveryProductDate = deliveryProductDate;
    this.company = company;
    this.boss = boss;

    company.addContract(this);
    boss.addContract(this);
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
  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  @Basic
  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }

  @Basic
  public Date getDeliveryProductDate() {
    return deliveryProductDate;
  }

  public void setDeliveryProductDate(Date deliveryProductDate) {
    this.deliveryProductDate = deliveryProductDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Contract contract = (Contract) o;
    return
        Objects.equals(startDate, contract.startDate) &&
            Objects.equals(scope, contract.scope) &&
            Objects.equals(deliveryProductDate, contract.deliveryProductDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(startDate, scope, deliveryProductDate);
  }

  @ManyToOne
  public Company getCompany() {
    return company;
  }

  public void setCompany(Company company) {
    if (this.company == null && company != null) {
      this.company = company;
      company.addContract(this); //reverse connection
    } else if (company != null && !this.company.equals(company)) {
      this.company.getContracts().remove(this); //reverse connection
      this.company = company;
      company.addContract(this); //reverse connection
    }
  }

  @ManyToOne
  public Boss getBoss() {
    return boss;
  }

  public void setBoss(Boss boss) {
    if (this.boss == null && boss != null) {
      this.boss = boss;
      boss.addContract(this); //reverse connection
    } else if (boss != null && !this.boss.equals(boss)) {
      this.boss.getContracts().remove(this); //reverse connection
      this.boss = boss;
      boss.addContract(this); //reverse connection
    }
  }
}
