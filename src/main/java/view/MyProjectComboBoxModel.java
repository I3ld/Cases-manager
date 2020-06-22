package view;

import controller.ProjectController;
import controller.TaskController;
import java.util.ArrayList;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.ListDataListener;
import model.Project;

public class MyProjectComboBoxModel extends DefaultComboBoxModel<Project> {

  ProjectController projectController = new ProjectController();

  @Override
  public int getSize() {
    return projectController.getAllProjects().size();
  }

  @Override
  public Project getElementAt(int index) {
    return (Project) projectController.getAllProjects().get(index);
  }

  @Override
  public Project getSelectedItem() {
    Project selectedProject= (Project) super.getSelectedItem();
    return selectedProject;
  }

}

