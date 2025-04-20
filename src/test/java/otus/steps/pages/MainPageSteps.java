package otus.steps.pages;

import com.google.inject.Inject;
import io.cucumber.java.ru.*;
import pages.CourseCatalogPage;
import pages.MainPage;

public class MainPageSteps {

  @Inject
  private MainPage mainPage;

  @Inject
  private CourseCatalogPage courseCatalogPage;

  @Дано("Открыть главную страницу OTUS")
  public void openMainePage() {
    mainPage.open();
  }

  @Если("Открыть меню 'Обучение'")
  public void openTeachingMenu() {
    mainPage.openTeachingMenu();
  }

  @И("Выбирать случайную категорию курсов")
  public void selectRandomCourseCategory() {
    mainPage.selectRandomCourseCategory();
  }
}
