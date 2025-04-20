package otus.steps.pages;

import com.google.inject.Inject;
import io.cucumber.java.ru.Когда;
import io.cucumber.java.ru.Пусть;
import pages.CourseCatalogPage;
import pages.MainPage;

public class MainPageSteps {

  @Inject
  private MainPage mainPage;

  @Inject
  private CourseCatalogPage courseCatalogPage;

  @Пусть("я открываю главную страницу OTUS")
  public void openMainePage() {
    mainPage.open();
  }

  @Когда("я открываю меню 'Обучение'")
  public void openTeachingMenu() {
    mainPage.openTeachingMenu();
  }

  @Когда("выбираю случайную категорию курсов")
  public void selectRandomCourseCategory() {
    mainPage.selectRandomCourseCategory();
  }
}
