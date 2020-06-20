package view;

import controller.TaskController;
import javax.swing.AbstractListModel;
import javax.swing.event.ListDataListener;

public class MyTaskListModel extends AbstractListModel {

  TaskController taskController = new TaskController();

  @Override
  public int getSize() {
    return taskController.getAllTasks().size();
  }

  @Override
  public Object getElementAt(int index) {
    return taskController.getAllTasks().get(index);
  }
}
