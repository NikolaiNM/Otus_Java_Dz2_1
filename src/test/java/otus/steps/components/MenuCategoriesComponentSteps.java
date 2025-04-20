package otus.steps.components;

import com.google.inject.Inject;
import components.MenuCategoriesComponent;
import io.cucumber.java.ru.Если;

public class MenuCategoriesComponentSteps {

  @Inject
  private MenuCategoriesComponent menuCategoriesComponent;

  @Если("Кликнуть по меню (.*)")
  public void clickByMenuCategory(String category) {

  }

}
