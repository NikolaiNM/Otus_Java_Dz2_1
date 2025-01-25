package pages;

import annotations.Path;
import com.google.inject.Inject;
import commons.waiters.Waiters;
import org.assertj.core.api.SoftAssertions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Path("/catalog/courses")
public class CourseCatalogPage extends AbsBasePage<CourseCatalogPage> {



  private static final By SHOW_MORE_BUTTON = By.cssSelector("button.sc-mrx253-0.enxKCy.sc-prqxfo-0.cXVWAS");
  private static final By SEARCH_INPUT = By.cssSelector("input[type='search']");


  private static final By COURSE_DATES = By.cssSelector("#__next section.sc-o4bnil-0 div.sc-18q05a6-0 > div > a > div.sc-1x9oq14-0 > div > div\n"); //  селектор по дате
  private static final By COURSE_NAME = By.cssSelector("#__next section.sc-o4bnil-0 div.sc-18q05a6-0 > div > a > h6 > div");// селектор по названию

  private static final By COURSE_TITLES = By.cssSelector("a.sc-zzdkm7-0 h6.sc-1x9oq14-0");
  private static final String TITLE_NAME_COURSE = ".sc-1ddwpfq-1 h1";

  private static final String
      START_DATE_COURSE = "#__next > div.sc-1j17uuq-0.klmZDZ.sc-1b3dhyb-0.bzaXwp > main > div > section > div.sc-x072mc-0.sc-3cb1l3-1.hOtCic.galmep > div > div:nth-child(1) > p\n";
  private static final By LINKS = By.cssSelector("#__next > div.sc-1j17uuq-0.klmZDZ.sc-1u2d5lq-0.oYOFo > main > div > section.sc-o4bnil-0.riKpM > div.sc-18q05a6-0.incGfX > div a[href^='/']");

  private List<Integer> courseIndexes; // Поле для хранения индексов курсов


  private static final By CHECKBOXES_LOCATOR = By.cssSelector("input.sc-1fry39v-3.iDiEdJ[type='checkbox']");
  private static final By CATEGORY_NAMES_LOCATOR = By.cssSelector("label.sc-1x9oq14-0-label");


  private final Waiters waiters;

  @Inject
  public CourseCatalogPage(WebDriver driver, Waiters waiters) {
    super(driver);
    this.waiters = waiters;
  }

  public CourseCatalogPage searchForCourse(String courseName) {
    WebElement searchInput = driver.findElement(SEARCH_INPUT);
    searchInput.clear();
    searchInput.sendKeys(courseName);
    waiters.waitForElementVisibleByLocator(COURSE_TITLES);
    return this;
  }

  public CourseCatalogPage findAndClickCourseByName(String courseName) {
    WebElement course = driver.findElements(COURSE_NAME).stream()
        .filter(c -> c.getText().trim().equalsIgnoreCase(courseName))
        .findFirst()
        .orElseThrow(() -> new AssertionError("Курс с именем '" + courseName + "' не найден."));

    course.click();
    return this;
  }

  public CourseCatalogPage clickShowMoreButtonUntilAllLoaded() {
    boolean buttonFound = true;
    while (buttonFound) {
      try {
        WebElement button = driver.findElement(SHOW_MORE_BUTTON);

        if (button.isDisplayed()) {
          waiters.waitForElementClickableByLocator(SHOW_MORE_BUTTON);
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
    List<String> courseDates = getCourseDates();
    List<LocalDate> parsedDates = parseCourseDates(courseDates);

    List<Integer> earliestIndexes = findCourseIndexesWithEarliestDate(parsedDates);
    List<Integer> latestIndexes = findCourseIndexesWithLatestDate(parsedDates);

    this.courseIndexes = new ArrayList<>();
    this.courseIndexes.addAll(earliestIndexes);
    this.courseIndexes.addAll(latestIndexes);

    return this;
  }

  private static List<Integer> findCourseIndexesWithEarliestDate(List<LocalDate> dates) {
    List<Integer> indexes = new ArrayList<>();
    if (dates == null || dates.isEmpty()) return indexes;

    List<LocalDate> nonNullDates = dates.stream()
        .filter(date -> date != null)
        .collect(Collectors.toList());

    if (nonNullDates.isEmpty()) return indexes;

    LocalDate earliestDate = nonNullDates.stream()
        .min(LocalDate::compareTo)
        .orElse(null);

    if (earliestDate != null) {
      for (int i = 0; i < dates.size(); i++) {
        if (dates.get(i) != null && dates.get(i).equals(earliestDate)) {
          indexes.add(i);
        }
      }
    }
    return indexes;
  }

  private static List<Integer> findCourseIndexesWithLatestDate(List<LocalDate> dates) {
    List<Integer> indexes = new ArrayList<>();
    if (dates == null || dates.isEmpty()) return indexes;

    List<LocalDate> nonNullDates = dates.stream()
        .filter(date -> date != null)
        .collect(Collectors.toList());

    if (nonNullDates.isEmpty()) return indexes;

    LocalDate latestDate = nonNullDates.stream()
        .max(LocalDate::compareTo)
        .orElse(null);

    if (latestDate != null) {
      for (int i = 0; i < dates.size(); i++) {
        if (dates.get(i) != null && dates.get(i).equals(latestDate)) {
          indexes.add(i);
        }
      }
    }
    return indexes;
  }

  private List<String> getCourseNames() {
    List<WebElement> courseElements = driver.findElements(COURSE_NAME);
    List<String> courseNames = new ArrayList<>();
    for (WebElement course : courseElements) {
      courseNames.add(course.getText());
    }
    return courseNames;
  }

  private List<String> getCourseDates() {
    List<WebElement> dateElements = driver.findElements(COURSE_DATES);
    List<String> courseDates = new ArrayList<>();
    for (WebElement date : dateElements) {
      courseDates.add(date.getText());
    }
    return courseDates;
  }

  private static List<LocalDate> parseCourseDates(List<String> courseDates) {
    List<LocalDate> parsedDates = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy");

    for (String date : courseDates) {
      if (date.contains("О дате старта будет объявлено позже")) {
        parsedDates.add(null);
      } else {
        try {
          String dateString = date.split(" · ")[0];
          LocalDate parsedDate = LocalDate.parse(dateString, formatter);
          parsedDates.add(parsedDate);
        } catch (Exception e) {
          parsedDates.add(null);
        }
      }
    }
    return parsedDates;
  }

  public String getCourseLinkByIndex(int index) {
    try {
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

    } catch (Exception e) {
      System.err.println("Ошибка при получении ссылки для индекса " + index + ": " + e.getMessage());
      return null;
    }
  }

  public CourseCatalogPage verifyCoursesOnLinks() {
    if (this.courseIndexes == null || this.courseIndexes.isEmpty()) {
      System.out.println("Индексы курсов не были найдены");
      return this;
    }
    System.out.println("Индексы курсов: " + courseIndexes);

    List<String> courseNames = getCourseNames();
    List<String> courseDates = getCourseDates();
    SoftAssertions softAssertions = new SoftAssertions();

    for (int index : courseIndexes) {
      try {
        String courseLink = getCourseLinkByIndex(index);

        if (courseLink != null && !courseLink.isEmpty()) {
          Document coursePage = Jsoup.connect(courseLink).get();
          String actualCourseTitle = coursePage.select(TITLE_NAME_COURSE).text();
          String expectedCourseTitle = courseNames.get(index);

          softAssertions.assertThat(actualCourseTitle)
              .as("Не прошла проверка названия курса для индекса " + index)
              .isEqualTo(expectedCourseTitle);
          if (actualCourseTitle.equals(expectedCourseTitle)) {
            System.out.println("Название курса совпадает с ожидаемым: " + expectedCourseTitle);
          }


          String actualCourseDate = coursePage.select(START_DATE_COURSE).text();

          if (actualCourseDate != null && !actualCourseDate.isEmpty()) {
            String expectedCourseDate = courseDates.get(index)
                .replaceAll(" · .*", "")
                .replaceAll(",\\s*\\d{4}", "");

            softAssertions.assertThat(actualCourseDate)
                .as("Не прошла проверка даты курса для индекса " + index)
                .isEqualTo(expectedCourseDate);
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
        e.printStackTrace();
      }
    }
    softAssertions.assertAll();
    return this;
  }

  //-------------------------------------------------------

  public int getCategoryIndex(String categoryName) {
    List<WebElement> categoryElements = $$(CATEGORY_NAMES_LOCATOR);

    for (int i = 0; i < categoryElements.size(); i++) {
      if (categoryElements.get(i).getText().equals(categoryName)) {
        return i;
      }
    }

    throw new NoSuchElementException("Категория '" + categoryName + "' не найдена.");
  }

  public boolean isCheckboxSelectedByIndex(int index) {
    List<WebElement> checkboxes = $$(CHECKBOXES_LOCATOR);

    if (index < 0 || index >= checkboxes.size()) {
      throw new IndexOutOfBoundsException("Индекс " + index + " выходит за пределы списка чекбоксов.");
    }

    return checkboxes.get(index).isSelected();
  }

}
