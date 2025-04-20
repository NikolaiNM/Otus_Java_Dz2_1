package otus.steps.pages;

import com.google.inject.Inject;
import io.cucumber.java.ru.*;
import org.junit.jupiter.api.Assertions;
import pages.CourseCatalogPage;
import scopeds.ScenarioContext;

public class CourseCatalogSteps {

  @Inject
  private CourseCatalogPage courseCatalogPage;

  @Inject
  private ScenarioContext context;

  private String selectedCategory;

  @Тогда("Чекбокс напротив выбранной категории должен быть активен")
  public void checkCheckboxIsActive() {
    String selectedCategory = context.getSelectedCategoryName();
    int categoryIndex = courseCatalogPage.getCategoryIndex(selectedCategory);
    boolean isChecked = courseCatalogPage.isCheckboxSelectedByIndex(categoryIndex);
    Assertions.assertTrue(isChecked, "Чекбокс напротив выбранной категории не активен.");
    System.out.println("Чекбокс напротив '" + selectedCategory + "' выбран.");
  }

  @Дано("Пользователь открывает страницу каталога курсов")
  public void openCatalogPage() {
    courseCatalogPage.open();
  }

  @Если("Пользователь ищет курс с названием (.*)$")
  public void searchCourseByName(String courseName) {
    courseCatalogPage.searchForCourse(courseName);
    context.setCurrentCourseName(courseName);
  }

  @И("Переходит на страницу найденного курса")
  public void openFoundCoursePage() {
    String courseName = context.getCurrentCourseName();
    courseCatalogPage.findAndClickCourseByName(courseName);
  }

  @Тогда("Заголовок страницы должен точно совпадать с (.*)$")
  public void verifyCourseHeader(String expectedCourseName) {
    String actualHeader = courseCatalogPage.getPageHeader();
    Assertions.assertEquals(expectedCourseName, actualHeader,
        "Ошибка: открыта страница неверного курса. Ожидалось: '" + expectedCourseName + "', но найдено: '" + actualHeader + "'");
    System.out.println("Название курса соответствует ожидаемому: " + expectedCourseName);
  }
}