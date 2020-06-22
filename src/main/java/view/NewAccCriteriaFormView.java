package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import model.AcceptCriteria;

public class NewAccCriteriaFormView extends JFrame {

  private JTextField descriptionTextField;
  private JButton addButton;
  private JButton cancelButton;
  private JPanel intoTopLabel;
  private JPanel rootJPanel;
  private NewTaskFormView parentFrame;

  public NewAccCriteriaFormView(NewTaskFormView parentFrame) {
    this.parentFrame = parentFrame;
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

  public void setUpAddButtonListeners() {
    addButton.addActionListener(e -> {
      if (!descriptionTextField.getText().isEmpty() && descriptionTextField.getText() != null) {
        parentFrame.getAcceptCriterias().add(new AcceptCriteria(descriptionTextField.getText()));
        parentFrame.setUpAccCriteriaSource();
        dispose();
      }
    });
  }

  public void setUpCancelButtonListeners() {
    cancelButton.addActionListener(e -> dispose());
  }

}
