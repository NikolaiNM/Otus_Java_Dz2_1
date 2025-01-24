package pages;

import annotations.Path;
import com.google.inject.Inject;
import commons.waiters.Waiters;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

//@Path("catalog/courses?search=Нагрузочное+тестирование&categories=programming")
@Path("/catalog/courses")
public class CourseCatalogPage extends AbsBasePage<CourseCatalogPage> {



  private static final By SHOW_MORE_BUTTON = By.cssSelector("button.sc-mrx253-0.enxKCy.sc-prqxfo-0.cXVWAS");
  public static final By SEARCH_INPUT = By.cssSelector("input[type='search']");


  private static final By COURSE_DATES = By.cssSelector("#__next section.sc-o4bnil-0 div.sc-18q05a6-0 > div > a > div.sc-1x9oq14-0 > div > div\n"); //  селектор по дате
  private static final By COURSE_NAME = By.cssSelector("#__next section.sc-o4bnil-0 div.sc-18q05a6-0 > div > a > h6 > div");// селектор по названию

  private static final By COURSE_TITLES = By.cssSelector("a.sc-zzdkm7-0 h6.sc-1x9oq14-0");
  private static final String TITLE_NAME_COURSE = ".sc-1ddwpfq-1 h1";

  private static final String
      START_DATE_COURSE = "#__next > div.sc-1j17uuq-0.klmZDZ.sc-1b3dhyb-0.bzaXwp > main > div > section > div.sc-x072mc-0.sc-3cb1l3-1.hOtCic.galmep > div > div:nth-child(1) > p\n";
  private static final By LINKS = By.cssSelector("#__next > div.sc-1j17uuq-0.klmZDZ.sc-1u2d5lq-0.oYOFo > main > div > section.sc-o4bnil-0.riKpM > div.sc-18q05a6-0.incGfX > div a[href^='/']");

  private List<Integer> courseIndexes; // Поле для хранения индексов курсов



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

  // Метод для нахождения индекса курса с самой ранней датой
  private static List<Integer> findCourseIndexesWithEarliestDate(List<LocalDate> dates) {
    List<Integer> indexes = new ArrayList<>();
    if (dates == null || dates.isEmpty()) return indexes;

    // Фильтруем null значения
    List<LocalDate> nonNullDates = dates.stream()
        .filter(date -> date != null)
        .collect(Collectors.toList());

    if (nonNullDates.isEmpty()) return indexes;  // Если после фильтрации дат нет, возвращаем пустой список

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

  // Метод для нахождения индекса курса с самой поздней датой
  private static List<Integer> findCourseIndexesWithLatestDate(List<LocalDate> dates) {
    List<Integer> indexes = new ArrayList<>();
    if (dates == null || dates.isEmpty()) return indexes;

    // Фильтруем null значения
    List<LocalDate> nonNullDates = dates.stream()
        .filter(date -> date != null)
        .collect(Collectors.toList());

    if (nonNullDates.isEmpty()) return indexes;  // Если после фильтрации дат нет, возвращаем пустой список

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


  // Метод для получения названий курсов
  private List<String> getCourseNames() {
    List<WebElement> courseElements = driver.findElements(COURSE_NAME);
    List<String> courseNames = new ArrayList<>();
    for (WebElement course : courseElements) {
      courseNames.add(course.getText()); // Получаем название каждого курса
    }
    return courseNames; // Возвращаем список названий курсов
  }

  // Метод для получения дат курсов
  private List<String> getCourseDates() {
    List<WebElement> dateElements = driver.findElements(COURSE_DATES);
    List<String> courseDates = new ArrayList<>();
    for (WebElement date : dateElements) {
      courseDates.add(date.getText()); // Получаем дату для каждого курса
    }
    return courseDates; // Возвращаем список дат курсов
  }


  private static List<LocalDate> parseCourseDates(List<String> courseDates) {
    List<LocalDate> parsedDates = new ArrayList<>();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM, yyyy"); // Формат для парсинга дат

    for (String date : courseDates) {
      if (date.contains("О дате старта будет объявлено позже")) {
        parsedDates.add(null); // Если дата не указана, добавляем null
      } else {
        try {
          String dateString = date.split(" · ")[0]; // Извлекаем саму дату из строки
          LocalDate parsedDate = LocalDate.parse(dateString, formatter); // Преобразуем строку в LocalDate
          parsedDates.add(parsedDate); // Добавляем дату в список
        } catch (Exception e) {
          parsedDates.add(null); // Если ошибка парсинга, добавляем null
        }
      }
    }
    return parsedDates; // Возвращаем список дат
  }

  public String getCourseLinkByIndex(int index) {
    try {
      System.out.println("Попытка получить ссылку на курс для индекса: " + index);

      // Используем локатор для нахождения всех ссылок
      List<WebElement> courseElements = driver.findElements(LINKS); // LINKS - локатор для всех ссылок на курс
      if (index >= 0 && index < courseElements.size()) {
        WebElement courseElement = courseElements.get(index);

        // Извлекаем атрибут href, который является фактической ссылкой
        String href = courseElement.getAttribute("href");

        if (href != null && !href.isEmpty()) {
          // Проверка и дополнение URL, если нужно
          /*
          if (!href.startsWith("https://otus.ru/")) {
            href = "https://otus.ru" + href;
          }*/

          // Выводим полную ссылку в консоль
          System.out.println("Найдена ссылка для индекса " + index + ": " + href);
          return href;
        } else {
          System.out.println("Ссылка не найдена для индекса " + index + ".");
        }
      } else {
        System.out.println("Не удалось найти курс с индексом " + index + ".");
      }
    } catch (Exception e) {
      System.err.println("Ошибка при получении ссылки для индекса " + index + ": " + e.getMessage());
    }
    return null;
  }


  public CourseCatalogPage verifyCoursesOnLinks() {
    System.out.println("=== Старт проверки курсов ===");

    // Проверяем, инициализированы ли индексы курсов
    if (this.courseIndexes == null || this.courseIndexes.isEmpty()) {
      throw new IllegalStateException("Индексы курсов не были найдены. Сначала вызовите findCoursesWithEarliestAndLatestDates.");
    }
    System.out.println("Индексы курсов: " + courseIndexes);

    // Получаем список названий курсов
    List<String> courseNames = getCourseNames();
    System.out.println("Список названий курсов: " + courseNames);

    // Получаем список дат курсов
    List<String> courseDates = getCourseDates();
    System.out.println("Список дат курсов: " + courseDates);

    // Итерируемся по индексам
    for (int index : courseIndexes) {
      try {
        System.out.println("Обработка индекса: " + index);

        // Получаем ссылку на курс
        String courseLink = getCourseLinkByIndex(index);
        System.out.println("Ссылка на курс для индекса " + index + ": " + courseLink);

        if (courseLink != null && !courseLink.isEmpty()) {
          // Загружаем страницу курса
          System.out.println("Подключение к странице курса...");
          Document coursePage = Jsoup.connect(courseLink).get();
          System.out.println("Страница курса успешно загружена.");

          // Извлекаем название курса из страницы
          String actualCourseTitle = coursePage.select(TITLE_NAME_COURSE).text();
          System.out.println("Извлечённое название курса: " + actualCourseTitle);

          // Получаем ожидаемое название курса
          String expectedCourseTitle = courseNames.get(index);
          System.out.println("Ожидаемое название курса: " + expectedCourseTitle);

          // Сравниваем названия
          if (!expectedCourseTitle.equals(actualCourseTitle)) {
            System.err.println("Ошибка: Название курса не совпадает! Ожидалось: " + expectedCourseTitle + ", но на странице: " + actualCourseTitle);
          } else {
            System.out.println("Название курса совпадает с ожидаемым: " + expectedCourseTitle);
          }

          // Извлекаем дату курса
          String actualCourseDate = coursePage.select(START_DATE_COURSE).text(); // Используем переменную START_DATE_COURSE как селектор
          System.out.println("Извлечённая дата курса: " + actualCourseDate);

          // Проверим, если дата не пустая, иначе пропустим
          if (actualCourseDate != null && !actualCourseDate.isEmpty()) {
            // Убираем возможные пробелы и другие лишние символы
            actualCourseDate = actualCourseDate.trim();
            System.out.println("Дата курса (после обработки): " + actualCourseDate);

            // Удаляем лишние части из даты, например, " · 3 месяца"
            actualCourseDate = actualCourseDate.replaceAll(" · .*", "");
            System.out.println("Дата курса (после удаления лишнего): " + actualCourseDate);

            // Получаем ожидаемую дату из списка
            String expectedCourseDate = courseDates.get(index);

            // Убираем лишние части из ожидаемой даты, например, " · 3 месяца" и год с запятой
            expectedCourseDate = expectedCourseDate.replaceAll(" · .*", "").replaceAll(",\\s*\\d{4}", "");
            System.out.println("Сравниваем с ожидаемой датой (после удаления лишнего): " + expectedCourseDate);

            // Сравниваем даты
            if (!actualCourseDate.equals(expectedCourseDate)) {
              System.err.println("Ошибка: Дата курса не совпадает! Ожидалась: " + expectedCourseDate + ", но на странице: " + actualCourseDate);
            } else {
              System.out.println("Дата курса совпадает с ожидаемой: " + expectedCourseDate);
            }
          } else {
            System.err.println("Дата курса не найдена для индекса " + index);
          }

        } else {
          System.err.println("Ссылка на курс для индекса " + index + " пуста или null.");
        }
      } catch (Exception e) {
        System.err.println("Ошибка при проверке курса по ссылке для индекса " + index + ": " + e.getMessage());
        e.printStackTrace();
      }
    }

    System.out.println("=== Проверка курсов завершена ===");
    return this;
  }


}
