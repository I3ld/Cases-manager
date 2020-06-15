import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
import model.Project;
import model.RegularEmployee.RegularEmployeeContractType;
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
public class BusinessLogicTest {

  Session session;

  @BeforeAll
  public void beforeClassFunction() {
    session = HibernateUtil.getSessionFactory().openSession();
  }

  /**
   * TEST - qualified association Issue[0..1] - Project[*]: Insert new project, Insert new
   * Issue(Task+AccCriteria), update associations, Get project from db, verify data prom projectDb
   */
  @Test
  public void qualifiedAssociation() {
    //Insert new project
    //new name every iteration because it's PK
    Long nr =
        (Long) session.createQuery("select count(*) from Project").uniqueResult() + 1;
    String projectName = "Name project" + nr;

    Project project = new Project(projectName, "Description project test",
        java.sql.Date.valueOf(LocalDate.now()), new BigDecimal("12456789.50"),
        java.sql.Date.valueOf(LocalDate.parse("2022-11-04")));

    //Insert new task(assign project to task)
    Task task = new Task("task title description test", "task title test", 2,
        Date.valueOf(LocalDate.parse("2021-12-06")));
    AcceptCriteria acc = new AcceptCriteria("Acc criteria test description");

    session.beginTransaction();
    session.save(project);
    session.save(task);
    session.save(acc);
    task.getAcceptCriteriaById().add(acc);
    project.addIssueQualified(task);
    project.addIssueQualified(acc);
    session.save(project);
    session.save(task);
    session.save(acc);
    session.getTransaction().commit();

    Query query = session.createQuery("from Project where name = :projectName")
        .setParameter("projectName", projectName);

    Project projectDb = (Project) query.getSingleResult();

    try {
      Task taskDb = (Task) projectDb.getIssuesQualifiedById(task.getId());
      AcceptCriteria accDb = (AcceptCriteria) projectDb.getIssuesQualifiedById(acc.getId());

      assertEquals(task, taskDb);
      assertEquals(acc, accDb);
      assertEquals(acc, taskDb.getAcceptCriteriaById().get(0));
      assertEquals(taskDb.getProject(), projectDb);
      assertEquals(accDb.getProject(), projectDb);
    } catch (Exception e) {
      System.err.println(
          "Cannot get issue from project!  issue id:" + task.getId() + " project: " + project
              .getName());
      e.printStackTrace();
    }
  }

  /**
   * TEST - qualified association Issue[0..1] - Project[*]: Insert new project, Insert new
   * Issue(Task+AccCriteria), update associations, Get project from db, get non-existent issue from
   * project
   */
  @Test
  public void qualifiedAssociation_NegativePath() {
    //Insert new project
    //new name every iteration because it's PK
    Long nr =
        (Long) session.createQuery("select count(*) from Project").uniqueResult() + 1;
    String projectName = "Name project" + nr;

    Project project = new Project(projectName, "Description project test",
        java.sql.Date.valueOf(LocalDate.now()), new BigDecimal("12456789.50"),
        java.sql.Date.valueOf(LocalDate.parse("2022-11-04")));

    //Insert new task(assign project to task)
    Task task = new Task("task title description test", "task title test", 2,
        Date.valueOf(LocalDate.parse("2021-12-06")));
    AcceptCriteria acc = new AcceptCriteria("Acc criteria test description");

    session.beginTransaction();
    session.save(project);
    session.save(task);
    session.save(acc);
    task.getAcceptCriteriaById().add(acc);
    project.addIssueQualified(task);
    project.addIssueQualified(acc);
    session.save(project);
    session.save(task);
    session.save(acc);
    session.getTransaction().commit();

    Query query = session.createQuery("from Project where name = :projectName")
        .setParameter("projectName", projectName);

    Project projectDb = (Project) query.getSingleResult();

    try {
      Task taskDb = (Task) projectDb.getIssuesQualifiedById(100);
      fail("Fail: should be throw exe here: no issue in map");
    } catch (Exception e) {
      System.err.println(
          "Cannot get issue from project!  issue id:" + task.getId() + " project: " + project
              .getName());
      e.printStackTrace();
    }
  }

  /**
   * TEST - association Employee[1..*] - Project[1]: Insert Project, Insert employees, assign emp to
   * project/Set project into emp, get project from db, verify data prom projectDb
   */
  @Test
  public void association_ProjectEmployee() {
    //Insert new project
    //new name every iteration because it's PK
    Long nr =
        (Long) session.createQuery("select count(*) from Project").uniqueResult() + 1;
    String projectName = "Name project" + nr;

    Project project = new Project(projectName, "Description project test",
        java.sql.Date.valueOf(LocalDate.now()), new BigDecimal("12456789.50"),
        java.sql.Date.valueOf(LocalDate.parse("2022-11-04")));

    DeputyHead deputyHead = new DeputyHead("Alan", "Walker", BigDecimal.valueOf(2445.23),
        Date.valueOf(LocalDate.parse("2007-12-03")), Arrays.asList("652-352-156", "658-852-245"),
        RegularEmployeeContractType.Mandate, Date.valueOf(LocalDate.now()));

    Specialist specialist = new Specialist("Carl", "Johnson", BigDecimal.valueOf(4445.50),
        Date.valueOf(LocalDate.parse("2017-12-03")), Arrays.asList("625-856-963", "563-845-852"),
        RegularEmployeeContractType.Permanent, deputyHead);

    session.beginTransaction();
    session.save(project);
    session.save(deputyHead);
    session.save(specialist);
    project.addEmployee(deputyHead);
    specialist.setProject(project);
    session.save(project);
    session.save(specialist);
    session.getTransaction().commit();

    Query query = session.createQuery("from Project where name = :projectName")
        .setParameter("projectName", projectName);

    Project projectDb = (Project) query.getSingleResult();

    assertEquals(deputyHead.getProject(), projectDb);
    assertEquals(specialist.getProject(), projectDb);
    assertTrue(projectDb.getEmployees().contains(deputyHead));
    assertTrue(projectDb.getEmployees().contains(specialist));
  }

  /**
   * TEST - association Issue[1..*] - EmployeeIssue - Employee[*..1]: Insert deputyHead, Insert
   * specialist, Insert task, Insert 2 empIssues to the same task with differences employees, verify
   * data from DB
   */
  @Test
  public void associationManyToMany_EmployeeIssue() {
    DeputyHead deputyHead = new DeputyHead("Alan", "Walker", BigDecimal.valueOf(2445.23),
        Date.valueOf(LocalDate.parse("2007-12-03")), Arrays.asList("652-352-156", "658-852-245"),
        RegularEmployeeContractType.Mandate, Date.valueOf(LocalDate.now()));

    Specialist specialist = new Specialist("Carl", "Johnson", BigDecimal.valueOf(4445.50),
        Date.valueOf(LocalDate.parse("2017-12-03")), Arrays.asList("625-856-963", "563-845-852"),
        RegularEmployeeContractType.Permanent, deputyHead);

    Task task = new Task("task title description test", "task title test", 2,
        Date.valueOf(LocalDate.parse("2021-12-06")));
    AcceptCriteria acc = new AcceptCriteria("Acc criteria test description");

    EmployeeIssue deputyHeadIssue = new EmployeeIssue("Suggest rewatch this task", deputyHead,
        task);
    EmployeeIssue deputySpecialistIssue = new EmployeeIssue("From my side done", specialist, task);

    session.beginTransaction();
    session.save(deputyHead);
    session.save(specialist);
    session.save(task);
    session.save(deputyHeadIssue);
    session.save(deputySpecialistIssue);
    session.save(acc);
    task.getAcceptCriteriaById().add(acc);
    session.save(task);
    session.getTransaction().commit();

    Query query = session.createQuery("from Task where id = :idTask")
        .setParameter("idTask", task.getId());
    Task taskDb = (Task) query.getSingleResult();

    Query query2 = session.createQuery("from DeputyHead where id = :idDeputyHead")
        .setParameter("idDeputyHead", deputyHead.getId());
    DeputyHead deputyHeadDb = (DeputyHead) query2.getSingleResult();

    assertTrue(taskDb.getEmployeeIssues().contains(deputyHeadIssue));
    assertTrue(taskDb.getEmployeeIssues().contains(deputySpecialistIssue));

    assertEquals(deputyHeadDb.getEmployeeIssues().get(0).getIssue(), task);
    assertEquals(specialist.getEmployeeIssues().get(0).getIssue(), task);
  }

  /**
   * TEST - association Issue[1..*] - EmployeeIssue - Employee[*..1]: Insert deputyHead, Insert
   * specialist, Insert task, Insert the same empIssues more the one time, verify
   * data from DB
   */
  @Test
  public void associationManyToMany_EmployeeIssue_VerifyValidation() {
    DeputyHead deputyHead = new DeputyHead("Alan", "Walker", BigDecimal.valueOf(2445.23),
        Date.valueOf(LocalDate.parse("2007-12-03")), Arrays.asList("652-352-156", "658-852-245"),
        RegularEmployeeContractType.Mandate, Date.valueOf(LocalDate.now()));

    Task task = new Task("task title description test", "task title test", 2,
        Date.valueOf(LocalDate.parse("2021-12-06")));
    AcceptCriteria acc = new AcceptCriteria("Acc criteria test description");

    EmployeeIssue deputyHeadIssue = new EmployeeIssue("Suggest rewatch this task", deputyHead,
        task);

    session.beginTransaction();
    session.save(deputyHead);
    session.save(task);
    session.save(deputyHeadIssue);
    session.save(acc);
    task.getAcceptCriteriaById().add(acc);
    session.save(task);
    deputyHead.addEmployeeIssue(deputyHeadIssue);
    session.save(deputyHead);
    deputyHead.addEmployeeIssue(deputyHeadIssue);
    session.save(deputyHead);
    deputyHead.addEmployeeIssue(deputyHeadIssue);
    session.save(deputyHead);
    deputyHead.addEmployeeIssue(deputyHeadIssue);
    session.save(deputyHead);
    session.getTransaction().commit();

    Query query = session.createQuery("from DeputyHead where id = :idDeputyHead")
        .setParameter("idDeputyHead", deputyHead.getId());
    DeputyHead deputyHeadDb = (DeputyHead) query.getSingleResult();

    assertEquals(deputyHeadDb.getEmployeeIssues().size(),1);
  }

  /**
   * TEST - association Company[1..*] - Contract - Boss[*..1]: Insert companies, Insert
   * boss, Insert contracts assigned to one boss with difference company, verify
   * data from DB
   */
  @Test
  public void associationManyToMany_CompanyBoss() {
    Company company = new Company("Ministerstwo Finansów - Cło i Transport",
        "5260250274", "Sekretariat.CiT@mf.gov.pl");

    Company company2 = new Company("Ministerstwo Finansów - Krajowa Administracja Skarbowa",
        "5260250274", "Sekretariat.DC@mf.gov.pl");

    Boss boss = new Boss("Peter", "Parker", BigDecimal.valueOf(22500.11),
        Date.valueOf(LocalDate.parse("2019-12-03")), Arrays.asList("321-856-963", "563-753-852"),
        BigDecimal.valueOf(1435234.22));

    Contract contract = new Contract(Date.valueOf(LocalDate.now()),
        "SYSTEM MONITOROWANIA DROGOWEGO I KOLEJOWEGO PRZEWOZU TOWARÓW ORAZ OBROTU PALIWAMI OPAŁOWYMI",
        Date.valueOf(LocalDate.parse("2023-05-05")),company,boss);

    Contract contract2 = new Contract(Date.valueOf(LocalDate.now()),
        "SYSTEM MONITOROWANIA DROGOWEGO I KOLEJOWEGO PRZEWOZU TOWARÓW ORAZ OBROTU PALIWAMI OPAŁOWYMI2",
        Date.valueOf(LocalDate.parse("2021-01-01")),company2,boss);

    session.beginTransaction();
    session.save(company);
    session.save(company2);
    session.save(boss);
    session.save(contract);
    session.save(contract2);
    session.getTransaction().commit();

    Query query = session.createQuery("from Boss where id = :bossId")
        .setParameter("bossId", boss.getId());

    Boss bossDb = (Boss) query.getSingleResult();

    Query query2 = session.createQuery("from Company where id = :companyId")
        .setParameter("companyId", company.getId());

    Company companyDb = (Company) query2.getSingleResult();

    assertTrue(bossDb.getContracts().contains(contract));
    assertTrue(bossDb.getContracts().contains(contract2));
    assertTrue(companyDb.getContracts().contains(contract));
    assertTrue(companyDb.getContracts().get(0).getBoss().equals(boss));
    assertTrue(companyDb.getContracts().get(0).getCompany().equals(company));
  }

  @AfterAll
  public void afterClassFunction() {
    session.close();
    HibernateUtil.shutdown();
  }
}
