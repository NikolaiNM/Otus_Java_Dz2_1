package scopeds;

import io.cucumber.guice.ScenarioScoped;

@ScenarioScoped
public class ScenarioContext {
  private String selectedCategoryName;
  private String currentCourseName;

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
}