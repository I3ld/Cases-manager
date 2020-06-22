package view;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
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
  private JButton editAccBtn;
  private JButton deleteAccBtn;
  private JButton cancelTaskBtn;
  private JButton saveTaskBtn;
  private JList accCriteriaJList;
  private JComboBox comboBox1;
  private JTextField textField1;
  private JRadioButton priority0RadioBtn;
  private JRadioButton priority1RadioBtn;
  private JRadioButton priority2RadioBtn;
  private JRadioButton priority3RadioBtn;
  private JTextField textField2;
  private JPanel mainPane;
  private JDatePanelImpl jDatePanelImpl;
  private JLabel projectLabel;
  private JLabel titleLabel;
  private JLabel descriptionLabel;
  private JLabel priorityLabel;
  private JLabel dueToDateLabel;
  private JDatePicker dueToDatePicker;
  private ButtonGroup priorityButtonGroup;

  private Task newTask;
  private List<AcceptCriteria> acceptCriterias = new ArrayList<>();
  private MyAccCriteriaListModel accListModel;

  public NewTaskFormView() {
    setUpComboBoxProjectSource();
    setUpAccCriteriaSource();
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

  private void setUpComboBoxProjectSource() {
    MyProjectComboBoxModel projectComboBoxModel = new MyProjectComboBoxModel();
    comboBox1.setModel(projectComboBoxModel);
  }

  //Validate data for new Task
  //Project,Title,Description,Priority,DueToDate
  public boolean verifyNewTaskDetails() {
    try {
      //project
      Project project = (Project) comboBox1.getSelectedItem();

      //Priority
      int priority = Integer.parseInt(priorityButtonGroup.getSelection().getActionCommand());

      //DueToDate
      Date selectedDate = (java.sql.Date) dueToDatePicker.getModel().getValue();

      //Title
      String titleTask;
      //Description
      String descriptionTask;

      if (!titleLabel.getText().isEmpty() && titleLabel.getText() != null) {
        titleTask = titleLabel.getText();

        if (!descriptionLabel.getText().isEmpty() && descriptionLabel.getText() != null) {
          descriptionTask = descriptionLabel.getText();

          //Crete new task and set project
          newTask = new Task(descriptionTask, titleTask, priority, selectedDate);
          newTask.setProject(project);
          return true;
        }
      }
    } catch (Exception e) {
      System.err.println("Create new task error validation!");
      return false;
    }
    return false;
  }

  //Button add new acc criteria - set listener to open new form
  private void setUpAddAccCriteriaListeners(){
    addAccBtn.addActionListener(e -> new NewAccCriteriaFormView(this));
  }

  public void setUpAccCriteriaSource(){
    accListModel = new MyAccCriteriaListModel(acceptCriterias);
    accCriteriaJList.setModel(accListModel);
    accCriteriaJList.repaint();
  }

  //DeleteAcc button - listeners
  private void setUpAccDeleteButtonListeners(){
    deleteAccBtn.addActionListener(e -> {
      if(!accCriteriaJList.isSelectionEmpty()){
        List<AcceptCriteria> selectedAcc = (List<AcceptCriteria>) accCriteriaJList.getSelectedValuesList();
        acceptCriterias.removeAll(selectedAcc);
        setUpAccCriteriaSource();
      }
    });
  }

  public List<AcceptCriteria> getAcceptCriterias() {
    return acceptCriterias;
  }
}
