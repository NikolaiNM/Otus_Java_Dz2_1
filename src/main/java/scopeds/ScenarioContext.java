package scopeds;

import io.cucumber.guice.ScenarioScoped;

@ScenarioScoped
public class ScenarioContext {
  private String selectedCategoryName;

  public String getSelectedCategoryName() {
    return selectedCategoryName;
  }

  public void setSelectedCategoryName(String selectedCategoryName) {
    this.selectedCategoryName = selectedCategoryName;
  }
}