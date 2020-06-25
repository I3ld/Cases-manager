import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import model.AcceptCriteria;
import model.Employee;
import model.Issue;
import model.Project;
import model.RegularEmployee;
import org.hibernate.Session;
import util.HibernateUtil;
import view.NewTaskFormView;
import view.TasksListView;

public class Main {

  public static void main(String[] args) {
    new TasksListView();
  }
}