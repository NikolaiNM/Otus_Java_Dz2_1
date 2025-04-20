package scopeds;

import io.cucumber.guice.ScenarioScoped;
import java.util.List;

@ScenarioScoped
public class ScenarioContext {
  private String selectedCategoryName;
  private String currentCourseName;
  private List<Integer> courseIndexes;

  public String getSelectedCategoryName() {
    return selectedCategoryName;
  }

  public void setSelectedCategoryName(String selectedCategoryName) {
    this.selectedCategoryName = selectedCategoryName;
  }

  public String getCurrentCourseName() {
    return currentCourseName;
  }

  public void setCurrentCourseName(String currentCourseName) {
    this.currentCourseName = currentCourseName;
  }

  public List<Integer> getCourseIndexes() {
    return courseIndexes;
  }

  public void setCourseIndexes(List<Integer> courseIndexes) {
    this.courseIndexes = courseIndexes;
  }

}