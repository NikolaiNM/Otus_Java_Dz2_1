package otus.steps.pages;

import com.google.inject.Inject;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Тогда;
import org.junit.jupiter.api.Assertions;
import pages.CourseCatalogPage;
import scopeds.ScenarioContext;

public class CourseCatalogSteps {

  @Inject
  private CourseCatalogPage courseCatalogPage;

  @Inject
  private ScenarioContext context;

  private String selectedCategory;

  @Тогда("чекбокс напротив выбранной категории должен быть активен")
  public void checkCheckboxIsActive() {
    String selectedCategory = context.getSelectedCategoryName();
    int categoryIndex = courseCatalogPage.getCategoryIndex(selectedCategory);
    boolean isChecked = courseCatalogPage.isCheckboxSelectedByIndex(categoryIndex);
    Assertions.assertTrue(isChecked, "Чекбокс напротив выбранной категории не активен.");
  }
}