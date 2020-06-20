package controller;

import java.util.List;
import javax.persistence.criteria.CriteriaQuery;
import model.Task;
import org.hibernate.Session;
import util.HibernateUtil;


public class TaskController {

  Session session;

  public TaskController() {
  }

  public List getAllTasks() {
    session = HibernateUtil.getSessionFactory().openSession();
    CriteriaQuery cq = session.getCriteriaBuilder().createQuery(Task.class);
    cq.from(Task.class);
    List<Task> tasks = session.createQuery(cq).getResultList();
    session.close();
    return tasks;

  }

  public boolean deleteTask(List<Task> tasks) {
    session = HibernateUtil.getSessionFactory().openSession();
    try {
      session.beginTransaction();
      for (Task task : tasks) {
        session.remove(task);
      }
      session.getTransaction().commit();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      session.close();
    }
  }

  public boolean updateTask(Task task) {
    session = HibernateUtil.getSessionFactory().openSession();
    try {
      session.beginTransaction();
      session.update(task);
      session.getTransaction().commit();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      session.close();
    }
  }

  public boolean addTask(Task task) {
    session = HibernateUtil.getSessionFactory().openSession();
    try {
      session.beginTransaction();
      session.save(task);
      session.getTransaction().commit();
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    } finally {
      session.close();
    }
  }
}
