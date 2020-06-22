package view;

import controller.TaskController;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import model.AcceptCriteria;
import model.Task;

public class TasksListView extends JFrame {

  private JPanel rootPane;
  private JList allTasksList;
  private JButton addButton;
  private JButton editButton;
  private JButton deleteButton;
  private JLabel infoUpLabel;
  private JPanel buttonsPane;
  private JList allAccCriteriaList;

  private TaskController taskController = new TaskController();
  private MyTaskListModel listTasksModel;
  private MyAccCriteriaListModel listAccCriteriaModel;

  public TasksListView() {
    setUpTasksListData();
    setUpAccCriteriaListData();
    setUpTasksListListener();
    setUpDeleteListeners();
    setUpAddButtonListeners();
    initView();
  }

  public void initView() {
    add(rootPane);
    setTitle("Task");
    setSize(850, 800);
    setVisible(true);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
  }

  //JList tasks - set source
  public void setUpTasksListData() {
    //Tasks
    listTasksModel = new MyTaskListModel();
    allTasksList.setModel(listTasksModel);
    allTasksList.setSelectedIndex(0);
  }

  //JList acc - set source
  public void setUpAccCriteriaListData() {
    Task currentTask = (Task) listTasksModel.getElementAt(0);
    List<AcceptCriteria> accCriteriaList = currentTask.getAcceptCriteriaById();

    listAccCriteriaModel = new MyAccCriteriaListModel(accCriteriaList);
    allAccCriteriaList.setModel(listAccCriteriaModel);
    allAccCriteriaList.repaint();
  }

  //Jlist tasks - value changed selected
  public void setUpTasksListListener() {
    allTasksList.addListSelectionListener(e -> {
      Task currentTask = (Task) allTasksList.getSelectedValue();
      List<AcceptCriteria> accCriteriaList = currentTask.getAcceptCriteriaById();

      listAccCriteriaModel = new MyAccCriteriaListModel(accCriteriaList);
      allAccCriteriaList.setModel(listAccCriteriaModel);
      allAccCriteriaList.repaint();
    });
  }

  //Add button - listeners - open new form for new task
  public void setUpAddButtonListeners(){
    addButton.addActionListener(e -> {
      NewTaskFormView mainTasksFram = new NewTaskFormView(this);
    });
  }

  //Delete button - listeners
  public void setUpDeleteListeners() {
    deleteButton.addActionListener(e -> deleteAction());

    deleteButton.addKeyListener(new KeyAdapter() {
      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          deleteAction();
        }
      }
    });
  }

  //Delete button - delete process with confirm JPane
  public void deleteAction() {
    List currentTasksList = allTasksList.getSelectedValuesList();
    String info = null;

    if (currentTasksList.size() == 1) { //one task selected
      info = currentTasksList.get(0).toString();
    } else if (currentTasksList.size() > 1) { //more than one task selected
      info = currentTasksList.size() + " tasks";
    }

    int result = JOptionPane.showConfirmDialog(null,
        "Are you sure to delete " + info + " ?", "Delete task",
        JOptionPane.YES_NO_OPTION);

    if (result == 0) {
      if (taskController.deleteTask(currentTasksList)) { //verify delete in DB
        allTasksList.repaint(); //refresh jlist
      } else {
        System.err.println("Error: Task delete action.");
      }
    }
  }

}
