package view;

import controller.TaskController;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import model.AcceptCriteria;
import model.Project;
import model.Task;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

public class NewTaskFormView extends JFrame {

  private JLabel topInfoLabel;
  private JButton addAccBtn;
  private JButton deleteAccBtn;
  private JButton cancelTaskBtn;
  private JButton saveTaskBtn;
  private JList accCriteriaJList;
  private JComboBox comboBox1;
  private JTextField titleTextField;
  private JRadioButton priority0RadioBtn;
  private JRadioButton priority1RadioBtn;
  private JRadioButton priority2RadioBtn;
  private JRadioButton priority3RadioBtn;
  private JTextField descriptionTextField;
  private JPanel mainPane;
  private JDatePanelImpl jDatePanelImpl;
  private JLabel projectLabel;
  private JLabel titleLabel;
  private JLabel descriptionLabel;
  private JLabel priorityLabel;
  private JLabel dueToDateLabel;
  private JDatePicker dueToDatePicker;

  private List<AcceptCriteria> acceptCriterias = new ArrayList<>();
  private MyAccCriteriaListModel accListModel;
  private TasksListView parentFrame;

  //New task data
  private Project project;
  private int priority;
  private Date selectedDate;
  private String titleTask;
  private String descriptionTask;

  public NewTaskFormView(TasksListView parentFrame) {
    this.parentFrame = parentFrame;
    setUpComboBoxProjectSource();
    setUpAccCriteriaSource();
    setUpCancelButtonListeners();
    setUpSaveTaskButtonListeners();
    setUpAccDeleteButtonListeners();
    setUpAddAccCriteriaListeners();
    initView();
  }

  public void initView() {
    add(mainPane);
    setTitle("Task");
    setSize(950, 800);
    setVisible(true);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
  }

  /**
   * Sources
   */
  //Needed for gui form - non palette component - added mvn dependency
  private void createUIComponents() {
    LocalDate dateNow = LocalDate.now();

    //set up data picker model with init date now
    UtilDateModel model = new UtilDateModel();
    model.setDate(dateNow.getYear(), dateNow.getMonthValue(), dateNow.getDayOfMonth());
    model.setSelected(true);

    jDatePanelImpl = new JDatePanelImpl(model);
    dueToDatePicker = new JDatePickerImpl(jDatePanelImpl);
  }

  public void setUpAccCriteriaSource() {
    accListModel = new MyAccCriteriaListModel(acceptCriterias);
    accCriteriaJList.setModel(accListModel);
    accCriteriaJList.repaint();
  }
  /**Sources End*/

  /**
   * Listeners
   */
  //Save button - listener(with process )
  public void setUpSaveTaskButtonListeners() {
    saveTaskBtn.addActionListener(e -> {
      if (validateNewAccDetails() && validateNewTaskDetails()) {
        //Crete new task and set project
        Task newTask = new Task(descriptionTask, titleTask, priority,
            new java.sql.Date(selectedDate.getTime()));

        TaskController taskController = new TaskController();
        taskController.persist(newTask);
        newTask.setProjectQualif(project);

        for (AcceptCriteria acc : acceptCriterias) {
          taskController.persist(acc);
          acc.setProjectQualif(project);
          newTask.addAcceptCriteria(acc);
        }

        taskController.addTask(newTask); //save new task to DB
        dispose();
        parentFrame.setUpTasksListData(); //refresh parent - tasks list
      } else {
        JOptionPane.showMessageDialog(
            null,
            "Validation error! Please correct task input details.",
            "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    });
  }

  //Button add new acc criteria - set listener to open new form
  private void setUpAddAccCriteriaListeners() {
    addAccBtn.addActionListener(e -> new NewAccCriteriaFormView(this));
  }

  //DeleteAcc button - listeners
  private void setUpAccDeleteButtonListeners() {
    deleteAccBtn.addActionListener(e -> {
      if (!accCriteriaJList.isSelectionEmpty()) {
        List<AcceptCriteria> selectedAcc = (List<AcceptCriteria>) accCriteriaJList
            .getSelectedValuesList();
        acceptCriterias.removeAll(selectedAcc);
        setUpAccCriteriaSource();
      }
    });
  }

  //Cancel button action - Close window
  private void setUpCancelButtonListeners() {
    cancelTaskBtn.addActionListener(e -> dispose());
  }

  /**
   * Listeners End
   */

  private void setUpComboBoxProjectSource() {
    MyProjectComboBoxModel projectComboBoxModel = new MyProjectComboBoxModel();
    comboBox1.setModel(projectComboBoxModel);
  }

  //Validate data for new Task
  //Project,Title,Description,Priority,DueToDate
  public boolean validateNewTaskDetails() {
    try {
      //Project
      project = (Project) comboBox1.getSelectedItem();

      //Priority
      JRadioButton radioBtnSelected = Stream
          .of(priority0RadioBtn, priority1RadioBtn, priority2RadioBtn, priority3RadioBtn)
          .filter(AbstractButton::isSelected).findFirst().orElse(null);

      priority = Integer.parseInt(radioBtnSelected.getText());

      //DueToDate
      selectedDate = (Date) (dueToDatePicker.getModel().getValue());

      if (selectedDate.after(new Date())) {
        //Title
        if (!titleTextField.getText().isEmpty() && titleTextField.getText() != null) {
          titleTask = titleTextField.getText();

          //Description
          if (!descriptionTextField.getText().isEmpty() && descriptionTextField.getText() != null) {
            descriptionTask = descriptionTextField.getText();
            return true;
          }
        }
      }
    } catch (Exception e) {
      JOptionPane.showMessageDialog(
          null,
          "Validation error! Please correct task input details.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
      e.printStackTrace();
      return false;
    }
    return false;
  }

  //Validate data for new Acc
  private boolean validateNewAccDetails() {
    if (!acceptCriterias.isEmpty()) {
      return true;
    } else {
      JOptionPane.showMessageDialog(
          null,
          "Validation error! Acc list cannot be empty.",
          "Error",
          JOptionPane.ERROR_MESSAGE);
      System.err.println("Validation error. Cannot save Task. Acc list is empty.");
      return false;
    }
  }

  public List<AcceptCriteria> getAcceptCriterias() {
    return acceptCriterias;
  }
}
