package controller;

import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import model.Project;
import org.hibernate.Session;
import util.HibernateUtil;

public class ProjectController {
  Session session;

  public List getAllProjects() {
    session = HibernateUtil.getSessionFactory().openSession();
    CriteriaQuery cq = session.getCriteriaBuilder().createQuery(Project.class);
    cq.from(Project.class);
    List<Project> tasks = session.createQuery(cq).getResultList();
    session.close();
    return tasks;
  }
}
