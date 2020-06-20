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

public class TasksListView extends JFrame {

  private TaskController taskController = new TaskController();
  private JPanel rootPane;
  private JList allTasksList;
  private JButton addButton;
  private JButton editButton;
  private JButton deleteButton;
  private JLabel infoUpLabel;
  private JPanel mainPane;
  private MyTaskListModel listModel;

  public TasksListView() {
    setUpListData();
    setUpDeleteListeners();
    initView();
  }

  public void initView() {
    add(rootPane);
    setTitle("Task");
    setSize(750, 800);
    setVisible(true);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
  }

  public void setUpListData() {
    listModel = new MyTaskListModel();
    allTasksList.setModel(listModel);
    allTasksList.setSelectedIndex(0);
  }

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
