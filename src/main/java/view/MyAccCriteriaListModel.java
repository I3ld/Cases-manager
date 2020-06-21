package view;

import controller.TaskController;
import java.util.List;
import javax.swing.AbstractListModel;
import model.AcceptCriteria;

public class MyAccCriteriaListModel extends AbstractListModel {

  List<AcceptCriteria> source;

  public MyAccCriteriaListModel(List<AcceptCriteria> source) {
    this.source = source;
  }

  @Override
  public int getSize() {
    return source.size();
  }

  @Override
  public Object getElementAt(int index) {
    return source.get(index);
  }
}
