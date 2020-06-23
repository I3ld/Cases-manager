package view;

import java.time.LocalDate;
import java.util.Date;
import java.util.stream.Stream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import model.Issue.IssueStatusType;
import model.Task;
import net.sourceforge.jdatepicker.JDatePicker;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

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

  private MyAccCriteriaListModel accListModel;

  private Task taskToEdit;
  private UtilDateModel jDateModel;

  public TaskEditFormView(Task taskToEdit) {
    this.taskToEdit = taskToEdit;
    setUpTaskProjectComboBoxSource();
    setUpTaskStatusComboBoxSource();
    setUpCancelButtonListeners();
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

    //Due to date
    //TODO fix - debug
    Date date = taskToEdit.getDueToDate();
    jDateModel.setValue(date);
    jDateModel.setSelected(true);

    //Acc criteria list
    accListModel = new MyAccCriteriaListModel(taskToEdit.getAcceptCriteriaById());
    accCriteriaJList.setModel(accListModel);
    accCriteriaJList.repaint();

  }

  //Cancel button - listener - close window
  public void setUpCancelButtonListeners(){
    cancelTaskBtn.addActionListener(e -> dispose());
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
}
