import java.util.List;
import model.Employee;
import model.RegularEmployee;
import org.hibernate.Session;
import util.HibernateUtil;
import view.NewTaskFormView;
import view.TasksListView;

public class Main {

  public static void main(String[] args) {
    /*Session session = HibernateUtil.getSessionFactory().openSession();
    List<RegularEmployee> emps = session.createQuery("from RegularEmployee").list();*/

    //TasksListView mainTasksFrame = new TasksListView();
    NewTaskFormView mainTasksFram = new NewTaskFormView();


  }
}