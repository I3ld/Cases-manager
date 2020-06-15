package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "Company")
public class Company {

  private int id;
  private String name;
  private String registrationNumber;
  private String contactEmail;
  private List<Contract> contracts = new ArrayList<>();

  public Company() { //Required by Hibernate
  }

  public Company(String name, String registrationNumber, String contactEmail) {
    this.name = name;
    this.registrationNumber = registrationNumber;
    this.contactEmail = contactEmail;
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
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    this.registrationNumber = registrationNumber;
  }

  @Basic
  public String getContactEmail() {
    return contactEmail;
  }

  public void setContactEmail(String contactEmail) {
    this.contactEmail = contactEmail;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Company company = (Company) o;
    return id == company.id &&
        registrationNumber == company.registrationNumber &&
        Objects.equals(name, company.name) &&
        Objects.equals(contactEmail, company.contactEmail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, registrationNumber, contactEmail);
  }

  @OneToMany(mappedBy = "company")
  public List<Contract> getContracts() {
    return contracts;
  }

  public void setContracts(List<Contract> contracts) {
    this.contracts = contracts;
  }

  public void addContract(Contract contract) {
    if (!contracts.contains(contract)) {
      contracts.add(contract);

      contract.setCompany(this); //reverse connection
    }
  }
}
