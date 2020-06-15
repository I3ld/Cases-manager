import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import javax.persistence.Query;
import model.AcceptCriteria;
import model.DeputyHead;
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
  public void associationProjectEmployee() {
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

  @AfterAll
  public void afterClassFunction() {
    session.close();
    HibernateUtil.shutdown();
  }
}
