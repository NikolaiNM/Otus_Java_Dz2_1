package pages;

import annotations.Path;
import com.google.inject.Inject;
import commons.waiters.Waiters;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import services.CourseService;
import services.CategoryService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Path("/catalog/courses")
public class CourseCatalogPage extends AbsBasePage<CourseCatalogPage> {

  // Локаторы
  private static final By SHOW_MORE_BUTTON = By.cssSelector("button.sc-mrx253-0.enxKCy.sc-prqxfo-0.cXVWAS");
  private static final By SEARCH_INPUT = By.cssSelector("input[type='search']");
  private static final By COURSE_DATES = By.cssSelector("#__next section.sc-o4bnil-0 div.sc-18q05a6-0 > div > a > div.sc-1x9oq14-0 > div > div");
  private static final By COURSE_NAME = By.cssSelector("#__next section.sc-o4bnil-0 div.sc-18q05a6-0 > div > a > h6 > div");
  private static final By COURSE_TITLES = By.cssSelector("a.sc-zzdkm7-0 h6.sc-1x9oq14-0");
  private static final By LINKS = By.cssSelector("#__next > div.sc-1j17uuq-0.klmZDZ.sc-1u2d5lq-0.oYOFo > main > div > section.sc-o4bnil-0.riKpM > div.sc-18q05a6-0.incGfX > div a[href^='/']");
  private static final By CHECKBOXES_LOCATOR = By.cssSelector("input.sc-1fry39v-3.iDiEdJ[type='checkbox']");
  private static final By CATEGORY_NAMES_LOCATOR = By.cssSelector("label.sc-1x9oq14-0-label");

  // Селекторы для Jsoup
  private static final String TITLE_NAME_COURSE = ".sc-1ddwpfq-1 h1";
  private static final String
      START_DATE_COURSE = "#__next > div.sc-1j17uuq-0.klmZDZ.sc-1b3dhyb-0.bzaXwp > main > div > section > div.sc-x072mc-0.sc-3cb1l3-1.hOtCic.galmep > div > div:nth-child(1) > p";

  // Сервисы
  private final CourseService courseService;
  private final CategoryService categoryService;
  private final Waiters waiters;

  // Поле для хранения индексов курсов
  private List<Integer> courseIndexes;

  @Inject
  public CourseCatalogPage(WebDriver driver, Waiters waiters) {
    super(driver);
    this.waiters = waiters;
    this.courseService = new CourseService(driver, waiters);
    this.categoryService = new CategoryService(driver);
  }

  public String getPageHeader() {
    return driver.findElement(By.tagName("h1")).getText();
  }

  // Методы для работы с курсами
  public CourseCatalogPage searchForCourse(String courseName) {
    driver.findElement(SEARCH_INPUT).sendKeys(courseName);
    waiters.waitForElementVisible(COURSE_TITLES);
    return this;
  }

  public CourseCatalogPage findAndClickCourseByName(String courseName) {
    courseService.clickCourseByName(COURSE_NAME, courseName);
    return this;
  }

  public CourseCatalogPage clickShowMoreButtonUntilAllLoaded() {
    boolean buttonFound = true;
    while (buttonFound) {
      try {
        WebElement button = driver.findElement(SHOW_MORE_BUTTON);
        if (button.isDisplayed()) {
          waiters.waitForElementClickable(button); // Используем исправленный метод
          button.click();
          waiters.waitForCondition(webDriver -> {
            try {
              WebElement newButton = driver.findElement(SHOW_MORE_BUTTON);
              return newButton.isDisplayed();
            } catch (StaleElementReferenceException e) {
              return false;
            }
          });
        }
      } catch (Exception e) {
        buttonFound = false;
      }
    }
    System.out.println("Показан весь список курсов");
    return this;
  }

  public CourseCatalogPage findCoursesWithEarliestAndLatestDates() {
    List<String> courseDates = courseService.getCourseDates(COURSE_DATES);
    List<LocalDate> parsedDates = parseCourseDates(courseDates);

    this.courseIndexes = new ArrayList<>();
    this.courseIndexes.addAll(findCourseIndexesWithEarliestDate(parsedDates));
    this.courseIndexes.addAll(findCourseIndexesWithLatestDate(parsedDates));

    return this;
  }

  private List<Integer> findCourseIndexesWithEarliestDate(List<LocalDate> dates) {
    return findCourseIndexesByCondition(dates, LocalDate::compareTo, true);
  }

  private List<Integer> findCourseIndexesWithLatestDate(List<LocalDate> dates) {
    return findCourseIndexesByCondition(dates, LocalDate::compareTo, false);
  }

  private List<Integer> findCourseIndexesByCondition(List<LocalDate> dates, Comparator<LocalDate> comparator, boolean isEarliest) {
    if (dates == null || dates.isEmpty()) return Collections.emptyList();

    LocalDate targetDate = isEarliest
        ? dates.stream().filter(Objects::nonNull).min(comparator).orElse(null)
        : dates.stream().filter(Objects::nonNull).max(comparator).orElse(null);

    if (targetDate == null) return Collections.emptyList();

    return IntStream.range(0, dates.size())
        .filter(i -> targetDate.equals(dates.get(i)))
        .boxed()
        .collect(Collectors.toList());
  }

  private List<LocalDate> parseCourseDates(List<String> courseDates) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy");
    return courseDates.stream()
        .map(date -> {
          if (date.contains("О дате старта будет объявлено позже")) {
            return null;
          }
          try {
            String dateString = date.split(" · ")[0];
            return LocalDate.parse(dateString, formatter);
          } catch (Exception e) {
            return null;
          }
        })
        .collect(Collectors.toList());
  }

  public String getCourseLinkByIndex(int index) {
    List<WebElement> courseElements = driver.findElements(LINKS);
    if (index < 0 || index >= courseElements.size()) {
      System.out.println("Не удалось найти курс с индексом " + index + ".");
      return null;
    }

    WebElement courseElement = courseElements.get(index);
    String href = courseElement.getAttribute("href");

    if (href == null || href.isEmpty()) {
      System.out.println("Ссылка для индекса " + index + " не найдена.");
      return null;
    }

    System.out.println("Ссылка на курс с индексом " + index + ": " + href);
    return href;
  }

  public CourseCatalogPage verifyCoursesOnLinks() {
    if (this.courseIndexes == null || this.courseIndexes.isEmpty()) {
      System.out.println("Индексы курсов не были найдены");
      return this;
    }

    List<String> courseNames = courseService.getCourseNames(COURSE_NAME);
    List<String> courseDates = courseService.getCourseDates(COURSE_DATES);
    SoftAssertions softAssertions = new SoftAssertions();

    for (int index : courseIndexes) {
      try {
        String courseLink = getCourseLinkByIndex(index);
        if (courseLink != null && !courseLink.isEmpty()) {
          Document coursePage = Jsoup.connect(courseLink).get();
          String actualCourseTitle = coursePage.select(TITLE_NAME_COURSE).text();
          String expectedCourseTitle = courseNames.get(index);

          // Проверка названия курса
          softAssertions.assertThat(actualCourseTitle)
              .as("Не прошла проверка названия курса для индекса " + index)
              .isEqualTo(expectedCourseTitle);

          // Логирование успешной проверки названия
          if (actualCourseTitle.equals(expectedCourseTitle)) {
            System.out.println("Название курса совпадает с ожидаемым: " + expectedCourseTitle);
          }

          // Проверка даты курса
          String actualCourseDate = coursePage.select(START_DATE_COURSE).text();
          if (actualCourseDate != null && !actualCourseDate.isEmpty()) {
            String expectedCourseDate = courseDates.get(index)
                .replaceAll(" · .*", "")
                .replaceAll(",\\s*\\d{4}", "");

            softAssertions.assertThat(actualCourseDate)
                .as("Не прошла проверка даты курса для индекса " + index)
                .isEqualTo(expectedCourseDate);

            // Логирование успешной проверки даты
            if (actualCourseDate.equals(expectedCourseDate)) {
              System.out.println("Дата начала курса совпадает с ожидаемой: " + expectedCourseDate);
            }
          } else {
            System.err.println("Дата курса для индекса " + index + " не найдена");
          }
        } else {
          System.err.println("Ссылка на курс для индекса " + index + " пуста или null.");
        }
      } catch (Exception e) {
        System.err.println("Ошибка при проверке курса по ссылке для индекса " + index + ": " + e.getMessage());
      }
    }
    softAssertions.assertAll();
    return this;
  }

  // Методы для работы с категориями
  public int getCategoryIndex(String categoryName) {
    return categoryService.getCategoryIndex(CATEGORY_NAMES_LOCATOR, categoryName);
  }

  public boolean isCheckboxSelectedByIndex(int index) {
    List<WebElement> checkboxes = driver.findElements(CHECKBOXES_LOCATOR);
    if (index < 0 || index >= checkboxes.size()) {
      throw new IndexOutOfBoundsException("Индекс " + index + " выходит за пределы списка чекбоксов.");
    }
    return checkboxes.get(index).isSelected();
  }
}