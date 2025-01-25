package pages;

import annotations.Path;
import com.google.inject.Inject;
import commons.waiters.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import java.util.Random;

@Path("/")
public class MainPage extends AbsBasePage<MainPage> {

  private static final By MENU_TEACHING_BUTTON = By.cssSelector("span[title='Обучение']");
  private static final By COURSE_CATEGORIES_LOCATOR = By.cssSelector("div.sc-1kjc6dh-2 a[href^='https://otus.ru/categories/']");
  private String selectedCategoryName;

  @Inject
  public MainPage(WebDriver driver, Waiters waiters) {
    super(driver);
    this.waiters = waiters;
  }

  public TeachPage clickNewTechButton() {
    By createTeacherButtonLocator = By.cssSelector("a[href='/teach']");

    WebElement createTeachButton = $(createTeacherButtonLocator);
    waiters.waitForCondition(ExpectedConditions.elementToBeClickable(createTeacherButtonLocator));
    createTeachButton.click();

    return (TeachPage) page(TeachPage.class);
  }

  public MainPage openTeachingMenu() {
    WebElement teachingButton = $(MENU_TEACHING_BUTTON);
    waiters.waitForCondition(ExpectedConditions.elementToBeClickable(MENU_TEACHING_BUTTON));
    teachingButton.click();
    return this;
  }

  private WebElement getRandomElement(List<WebElement> elements) {
    int randomIndex = new Random().nextInt(elements.size());
    return elements.get(randomIndex);
  }

  public MainPage selectRandomCourseCategory() {
    // Ждем, пока список категорий станет видимым
    waiters.waitForCondition(ExpectedConditions.visibilityOfElementLocated(COURSE_CATEGORIES_LOCATOR));

    // Получаем все элементы категорий
    List<WebElement> categories = $$(COURSE_CATEGORIES_LOCATOR);

    // Выбираем случайную категорию
    if (!categories.isEmpty()) {
      WebElement randomCategory = getRandomElement(categories); // Инициализируем переменную randomCategory
      selectedCategoryName = randomCategory.getText().replaceAll("\\(.*\\)", "").trim(); // Убираем скобки с цифрами
      randomCategory.click(); // Кликаем на случайную категорию
    } else {
      throw new RuntimeException("Категории курсов не найдены.");
    }

    return this;
  }

  public String getSelectedCategoryName() {
    return selectedCategoryName;
  }

}