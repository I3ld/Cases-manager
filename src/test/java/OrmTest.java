import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import javax.persistence.Query;
import model.AcceptCriteria;
import model.Boss;
import model.Company;
import model.Contract;
import model.DeputyHead;
import model.EmployeeIssue;
import model.Event;
import model.Project;
import model.RegularEmployee.RegularEmployeeContractType;
import model.RegularEmployee.RegularEmployeeType;
import model.Specialist;
import model.Task;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import util.HibernateUtil;

@TestInstance(Lifecycle.PER_CLASS)
public class OrmTest {

  Session session;

  @BeforeAll
  public void beforeClassFunction() {
    session = HibernateUtil.getSessionFactory().openSession();
  }

  @Test
  public void insertProject() {

    //new name every iteration because it's PK
    Long nr =
        (Long) session.createQuery("select count(*) from Project").uniqueResult() + 1;
    String projectName = "Name project" + nr;

    Project project = new Project(projectName, "Description project test",
        java.sql.Date.valueOf(LocalDate.now()), new BigDecimal("12456789.50"),
        java.sql.Date.valueOf(LocalDate.parse("2022-11-04")));

    session.beginTransaction();
    session.save(project);
    session.getTransaction().commit();

    Query query = session.createQuery("from Project where name = :projectName")
        .setParameter("projectName", projectName);

    Project lastProject = (Project) query.getSingleResult();

    assertEquals(project, lastProject);
  }

  @Test
  public void insertDeputyHead() {
    DeputyHead deputyHead = new DeputyHead("Alan", "Walker", BigDecimal.valueOf(2445.23),
        Date.valueOf(LocalDate.parse("2007-12-03")), Arrays.asList("652-352-156", "658-852-245"),
        RegularEmployeeContractType.Mandate, Date.valueOf(LocalDate.now()));

    session.beginTransaction();
    session.save(deputyHead);
    session.getTransaction().commit();

    Query query = session.createQuery("from DeputyHead where id = :deputyHeadID")
        .setParameter("deputyHeadID", deputyHead.getId());

    DeputyHead lastDeputyHead = (DeputyHead) query.getSingleResult();

    assertEquals(deputyHead, lastDeputyHead);
    assertTrue(lastDeputyHead.getRegularEmployeeType().contains(RegularEmployeeType.DeputyHead));
  }

  @Test
  public void insertSpecialist() {
    DeputyHead deputyHead = new DeputyHead("Alan", "Walker", BigDecimal.valueOf(2445.23),
        Date.valueOf(LocalDate.parse("2007-12-03")), Arrays.asList("652-352-156", "658-852-245"),
        RegularEmployeeContractType.Mandate, Date.valueOf(LocalDate.now()));

    Specialist specialist = new Specialist("Carl", "Johnson", BigDecimal.valueOf(4445.50),
        Date.valueOf(LocalDate.parse("2017-12-03")), Arrays.asList("625-856-963", "563-845-852"),
        RegularEmployeeContractType.Permanent, deputyHead);

    session.beginTransaction();
    session.save(deputyHead);
    session.save(specialist);
    session.getTransaction().commit();

    Query query = session.createQuery("from Specialist where id = :specialistID")
        .setParameter("specialistID", specialist.getId());

    Specialist lastSpecialist = (Specialist) query.getSingleResult();

    assertEquals(specialist, lastSpecialist);
    assertTrue(lastSpecialist.getRegularEmployeeType().contains(RegularEmployeeType.Specialist));
    assertEquals(specialist.getSupervisor(), deputyHead);
  }

  @Test
  public void insertBoss() {
    Boss boss = new Boss("Peter", "PArker", BigDecimal.valueOf(22500.11),
        Date.valueOf(LocalDate.parse("2019-12-03")), Arrays.asList("321-856-963", "563-753-852"),
        BigDecimal.valueOf(1435234.22));

    session.beginTransaction();
    session.save(boss);
    session.getTransaction().commit();

    Query query = session.createQuery("from Boss where id = :bossId")
        .setParameter("bossId", boss.getId());

    Boss lastBoss = (Boss) query.getSingleResult();

    assertEquals(boss, lastBoss);
  }

  @Test
  public void insertCompany() {
    Company company = new Company("Ministerstwo Finansów - Krajowa Administracja Skarbowa",
        "5260250274", "Sekretariat.DC@mf.gov.pl");

    session.beginTransaction();
    session.save(company);
    session.getTransaction().commit();

    Query query = session.createQuery("from Company where id = :companyId")
        .setParameter("companyId", company.getId());

    Company lastCompany = (Company) query.getSingleResult();

    assertEquals(company, lastCompany);
  }

  @Test
  public void insertContract() {
    Company company = new Company("Ministerstwo Finansów - Krajowa Administracja Skarbowa",
        "5260250274", "Sekretariat.DC@mf.gov.pl");

    Boss boss = new Boss("Peter", "Parker", BigDecimal.valueOf(22500.11),
        Date.valueOf(LocalDate.parse("2019-12-03")), Arrays.asList("321-856-963", "563-753-852"),
        BigDecimal.valueOf(1435234.22));

    Contract contract = new Contract(Date.valueOf(LocalDate.now()),
        "SYSTEM MONITOROWANIA DROGOWEGO I KOLEJOWEGO PRZEWOZU TOWARÓW ORAZ OBROTU PALIWAMI OPAŁOWYMI",
        Date.valueOf(LocalDate.parse("2023-05-05")),company,boss);

    session.beginTransaction();
    session.save(company);
    session.save(boss);
    session.save(contract);
    session.getTransaction().commit();

    Query query = session.createQuery("from Contract where id = :contractId")
        .setParameter("contractId", contract.getId());

    Contract lastContract = (Contract) query.getSingleResult();

    assertEquals(contract, lastContract);
  }

  @Test
  public void insertEvent() {
    Event event = new Event("Progress of development despite time differences",
        "Progress review",
        Date.valueOf(LocalDate.parse("2020-12-06")));

    session.beginTransaction();
    session.save(event);
    session.getTransaction().commit();

    Query query = session.createQuery("from Event where id = :eventId")
        .setParameter("eventId", event.getId());

    Event lastEvent = (Event) query.getSingleResult();

    assertEquals(event, lastEvent);
  }

  @Test
  public void insertTaskWithAccCriteria() {
    Task task = new Task("task title description test", "task title test", 2,
        Date.valueOf(LocalDate.parse("2021-12-06")));
    AcceptCriteria acc = new AcceptCriteria("Acc criteria test description");

    task.getAcceptCriteriaById().add(acc);

    session.beginTransaction();
    session.save(task);
    session.getTransaction().commit();

    Query query = session.createQuery("from Task where id = :taskId")
        .setParameter("taskId", task.getId());

    Task lastTask = (Task) query.getSingleResult();

    assertEquals(task, lastTask);
    assertEquals(acc, lastTask.getAcceptCriteriaById().get(0));
  }

  @Test
  public void insertEmployeeIssue() {
    Task task = new Task("task title description test", "task title test", 2,
        Date.valueOf(LocalDate.parse("2021-12-06")));
    AcceptCriteria acc = new AcceptCriteria("Acc criteria test description");

    DeputyHead deputyHead = new DeputyHead("Alan", "Walker", BigDecimal.valueOf(2445.23),
        Date.valueOf(LocalDate.parse("2007-12-03")), Arrays.asList("652-352-156", "658-852-245"),
        RegularEmployeeContractType.Mandate, Date.valueOf(LocalDate.now()));

    Specialist specialist = new Specialist("Carl", "Johnson", BigDecimal.valueOf(4445.50),
        Date.valueOf(LocalDate.parse("2017-12-03")), Arrays.asList("625-856-963", "563-845-852"),
        RegularEmployeeContractType.Permanent, deputyHead);

    task.getAcceptCriteriaById().add(acc);
    EmployeeIssue empIssue = new EmployeeIssue("Workaround - need to rewatch",deputyHead,task);
    empIssue.setIssue(task);
    empIssue.setEmployee(specialist);

    session.beginTransaction();
    session.save(task);
    session.save(deputyHead);
    session.save(specialist);
    session.save(empIssue);
    session.getTransaction().commit();

    Query query = session.createQuery("from EmployeeIssue where id = :empIssueId")
        .setParameter("empIssueId", empIssue.getId());

    EmployeeIssue lastEmpIssue = (EmployeeIssue) query.getSingleResult();

    assertEquals(empIssue, lastEmpIssue);
  }

  @AfterAll
  public void afterClassFunction() {
    session.close();
    HibernateUtil.shutdown();
  }
}
