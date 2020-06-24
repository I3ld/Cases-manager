package view;

import controller.TaskController;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Stream;
import javax.swing.AbstractButton;
import javax.swing.DefaultComboBoxModel;
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
import model.Issue.IssueStatusType;
import model.Project;
import model.Task;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;
import org.hibernate.Session;
import util.HibernateUtil;

public class TaskEditFormView extends JFrame {

  private JPanel mainPane;
  private JDatePanelImpl jDatePanelImpl;
  private JTextField descriptionTextField;
  private JLabel descriptionLabel;
  private JLabel dueToDateLabel;
  private JLabel priorityLabel;
  private JTextField titleTextField;
  private JLabel titleLabel;
  private JComboBox comboBox1;
  private JLabel projectLabel;
  private JLabel topInfoLabel;
  private JComboBox statusComboBox;
  private JButton updateTaskBtn;
  private JButton cancelTaskBtn;
  private JButton addAccBtn;
  private JButton editAccBtn;
  private JButton deleteAccBtn;
  private JList accCriteriaJList;
  private JRadioButton priority0RadioBtn;
  private JRadioButton priority1RadioBtn;
  private JRadioButton priority2RadioBtn;
  private JRadioButton priority3RadioBtn;
  private JDatePicker dueToDatePicker;

  private TasksListView parenView;
  private Task taskToEdit;
  private UtilDateModel jDateModel;
  private Session session = HibernateUtil.getSessionFactory()
      .openSession(); // Edit process around one transaction

  //Task details
  private Project project;
  private JRadioButton radioBtnSelected;
  private Date selectedDate;
  private String title;
  private String description;
  private MyAccCriteriaListModel accListModel;

  public TaskEditFormView(TasksListView parenView) {
    this.parenView = parenView;
    taskToEdit = (Task) parenView.getAllTasksList().getSelectedValue();
    session.beginTransaction();
    setUpTaskProjectComboBoxSource();
    setUpTaskStatusComboBoxSource();
    setUpCancelButtonListeners();
    setUpAccCriteriaDeleteButtonListeners();
    windowCloseListeners();
    setUpUpdateTaskButtonListeners();
    setUpAddAccButtonListeners();
    setUpEditForm();

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

  //Set status combo box model - enum
  //! Must be call before setUpEditForm()
  private void setUpTaskProjectComboBoxSource() {
    comboBox1.setModel(new MyProjectComboBoxModel());
  }

  //Set status combo box model - enum
  //! Must be call before setUpEditForm()
  private void setUpTaskStatusComboBoxSource() {
    statusComboBox.setModel(new DefaultComboBoxModel<>(IssueStatusType.values()));
  }

  public void setUpAccCriteriaSource() {
    accListModel = new MyAccCriteriaListModel(taskToEdit.getAcceptCriteriaById());
    accCriteriaJList.setModel(accListModel);
    accCriteriaJList.repaint();
  }

  //Get selected task and fill data in edi form
  private void setUpEditForm() {
    //Project
    comboBox1.setSelectedItem(taskToEdit.getProject());

    //Title
    titleTextField.setText(taskToEdit.getTitle());

    //Description
    descriptionTextField.setText(taskToEdit.getDescription());

    //Priority
    int priority = taskToEdit.getPriority();
    JRadioButton radioBtnSelected = Stream
        .of(priority0RadioBtn, priority1RadioBtn, priority2RadioBtn, priority3RadioBtn)
        .filter(btn -> btn.getText().equals(String.valueOf(priority))).findFirst().orElse(null);

    if (radioBtnSelected != null) {
      radioBtnSelected.setSelected(true);
    }

    //Status
    statusComboBox.setSelectedItem(taskToEdit.getStatus());

    //Due to date
    //TODO fix - debug
    Date date = taskToEdit.getDueToDate();
    jDateModel.setValue(date);
    jDateModel.setSelected(true);

    //Acc criteria list
    setUpAccCriteriaSource();
  }

  //Add acc button - listeners
  private void setUpAddAccButtonListeners() {
    addAccBtn.addActionListener(e -> {
      new NewAccCriteriaFormView(this, session);
    });
  }

  //Cancel button - listener - close window
  public void setUpCancelButtonListeners() {
    cancelTaskBtn.addActionListener(e -> {
      session.close();
      dispose();
    });
  }

  private void createUIComponents() {
    LocalDate dateNow = LocalDate.now();

    //set up data picker model with init date now
    jDateModel = new UtilDateModel();
    jDateModel.setDate(dateNow.getYear(), dateNow.getMonthValue(), dateNow.getDayOfMonth());
    jDateModel.setSelected(true);

    jDatePanelImpl = new JDatePanelImpl(jDateModel);
    dueToDatePicker = new JDatePickerImpl(jDatePanelImpl);
  }

  //Delete acc criteria button - listeners
  private void setUpAccCriteriaDeleteButtonListeners(){
    deleteAccBtn.addActionListener(e -> {
      AcceptCriteria accSelected = null;
      if(!accCriteriaJList.isSelectionEmpty()){
       accSelected = (AcceptCriteria) accCriteriaJList.getSelectedValue();
        session.remove(accSelected);
        accListModel.source.remove(accSelected); //workaround - acc remove not committed yet
        accCriteriaJList.repaint();
      }
    });
  }

  //Update button - listener - save updated task
  private void setUpUpdateTaskButtonListeners() {
    updateTaskBtn.addActionListener(e -> {
      if (validateNewTaskDetails() && validateNewAccDetails()) {
        //New acc add process is in new acc form
        //Set Project
        taskToEdit.setProject(project);
        //Title
        taskToEdit.setTitle(title);
        //Description
        taskToEdit.setDescription(description);
        //Priority
        taskToEdit.setPriority(Integer.parseInt(radioBtnSelected.getText()));
        //Status
        taskToEdit.setStatus((IssueStatusType) statusComboBox.getSelectedItem());
        //DueToDate
        taskToEdit.setDueToDate(new java.sql.Date(selectedDate.getTime()));

        new TaskController().updateTask(taskToEdit);

        session.getTransaction().commit(); //acc changes session
        session.close();
        parenView.getAllTasksList().repaint(); //refresh changes acc list in tasks list view
        dispose();
      }
    });
  }

  //Validate data for selected Task
  //Project,Title,Description,Priority,DueToDate
  public boolean validateNewTaskDetails() {
    String errorInfo = null;
    try {
      //Project
      project = (Project) comboBox1.getSelectedItem();
      if (project == null) {
        errorInfo = "Validation Error. Project = null";
        throw new Exception(errorInfo);
      }

      //Priority
      radioBtnSelected = Stream
          .of(priority0RadioBtn, priority1RadioBtn, priority2RadioBtn, priority3RadioBtn)
          .filter(AbstractButton::isSelected).findFirst().orElse(null);

      if (radioBtnSelected == null) {
        errorInfo = "Validation Error. Priority = null";
        throw new Exception(errorInfo);
      }

      //DueToDate
      selectedDate = (Date) (dueToDatePicker.getModel().getValue());
      if (selectedDate == null) {
        errorInfo = "Validation Error. Due to date = null";
        throw new Exception(errorInfo);
      } else if (!selectedDate.after(new Date())) {
        errorInfo = "Validation Error. Due to date before today";
        throw new Exception(errorInfo);
      }

      //Title
      title = titleTextField.getText();
      if (title.isEmpty()) {
        errorInfo = "Validation Error. Title = null";
        throw new Exception(errorInfo);
      }

      //Description
      description = descriptionTextField.getText();
      if (description.isEmpty()) {
        errorInfo = "Validation Error. Description = null";
        throw new Exception(errorInfo);
      }

      return true;
    } catch (Exception e) {
      JOptionPane.showMessageDialog(
          null,
          errorInfo,
          "Error",
          JOptionPane.ERROR_MESSAGE);
      System.err.println("Create new task error validation!");
      e.printStackTrace();
      return false;
    }
  }

  //Validate data for edited Acc
  private boolean validateNewAccDetails() {
    if (accCriteriaJList.getModel().getSize() > 0) {
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

  //Close window by X in corner - listeners
  private void windowCloseListeners() {
    this.addWindowListener(new java.awt.event.WindowAdapter() {
      @Override
      public void windowClosed(java.awt.event.WindowEvent windowEvent) {
        session.close();
      }
    });
  }

  public Task getTaskToEdit() {
    return taskToEdit;
  }
}
