package view;

import controller.TaskController;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import model.AcceptCriteria;
import org.hibernate.Session;

public class NewAccCriteriaFormView extends JFrame {

  private JTextField descriptionTextField;
  private JButton addButton;
  private JButton cancelButton;
  private JPanel intoTopLabel;
  private JPanel rootJPanel;
  private NewTaskFormView parentFrame;
  private TaskEditFormView parentFrameEdit;

  private TaskController taskController = new TaskController();
  private Session session;

  public NewAccCriteriaFormView(NewTaskFormView parentFrame) {
    this.parentFrame = parentFrame;
    setUpAddButtonListeners();
    setUpCancelButtonListeners();
    initView();
  }

  public NewAccCriteriaFormView(TaskEditFormView parentFrameEdit, Session session) {
    this.parentFrameEdit = parentFrameEdit;
    this.session = session;
    setUpAddButtonListeners();
    setUpCancelButtonListeners();
    initView();
  }

  public void initView() {
    add(rootJPanel);
    setTitle("Accept Criteria");
    setSize(600, 175);
    setVisible(true);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
  }

  /**Listeners*/
  public void setUpAddButtonListeners() {
    addButton.addActionListener(e -> {
      if (!descriptionTextField.getText().isEmpty() && descriptionTextField.getText() != null) {
        if (parentFrame != null) {
          parentFrame.getAcceptCriterias()
              .add(new AcceptCriteria(descriptionTextField.getText()));
          parentFrame.setUpAccCriteriaSource();
          dispose();
        } else if (parentFrameEdit != null) {
          //Add acc temporary to db - wai fot commit in update button action
          parentFrameEdit.getTaskToEdit()
              .addAcceptCriteria(new AcceptCriteria(descriptionTextField.getText()));

          taskController.updateTaskWithoutCommit(parentFrameEdit
              .getTaskToEdit(), session);

          parentFrameEdit.setUpAccCriteriaSource();
          dispose();
        }
      }
    });
  }

  //Cancel button - abandon edit - listener
  public void setUpCancelButtonListeners() {
    cancelButton.addActionListener(e -> dispose());
  }
  /**Listeners End*/
}
