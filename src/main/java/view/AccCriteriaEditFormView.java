package view;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import model.AcceptCriteria;
import model.Issue.IssueStatusType;

public class AccCriteriaEditFormView extends JFrame {

  private JPanel intoTopLabel;
  private JPanel rootJPanel;
  private JTextField descriptionTextField;
  private JButton editButton;
  private JButton cancelButton;
  private JComboBox comboBox1;
  private TaskEditFormView parenView;

  private AcceptCriteria selectedAcc;

  public AccCriteriaEditFormView(TaskEditFormView parenView) {
    this.parenView = parenView;
    selectedAcc = (AcceptCriteria) parenView.getAccCriteriaJList().getSelectedValue();
    setUpAccStatusComboBoxSource();
    setUpEditAccCriteriaEditButtonListeners();
    setUpCancelAccCriteriaEditButtonListeners();
    setUpEditForm();
    initView();
  }

  public void initView() {
    add(rootJPanel);
    setTitle("Accept Criteria");
    setSize(600, 200);
    setVisible(true);
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);
  }

  /**Sources*/
  //fil form data from selected acc
  public void setUpEditForm() {
    descriptionTextField.setText(selectedAcc.getDescription());
    comboBox1.setSelectedItem(selectedAcc.getStatus());
  }

  //Combo box status acc set mode
  private void setUpAccStatusComboBoxSource() {
    comboBox1.setModel(new DefaultComboBoxModel<>(IssueStatusType.values()));
  }
  /**Sources End*/

  /**Listeners*/
  //Edit button - accept changes - listeners
  private void setUpEditAccCriteriaEditButtonListeners() {
    editButton.addActionListener(e -> {
      if (!descriptionTextField.getText().isEmpty() && descriptionTextField.getText() != null) {
        selectedAcc.setDescription(descriptionTextField.getText());
        selectedAcc.setStatus((IssueStatusType) comboBox1.getSelectedItem());
        parenView.getAccCriteriaJList().repaint();
        dispose();
      }
    });
  }

  //Cancel button - listeners
  private void setUpCancelAccCriteriaEditButtonListeners() {
    cancelButton.addActionListener(e -> dispose());
  }
  /**Listeners End*/
}
