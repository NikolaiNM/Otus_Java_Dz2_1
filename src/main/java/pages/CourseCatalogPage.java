package pages;

import annotations.Path;
import com.google.inject.Inject;
import commons.waiters.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@Path("/catalog/courses")
public class CourseCatalogPage extends AbsBasePage<CourseCatalogPage> {

  private static final By COURSE_TITLES = By.cssSelector("a.sc-zzdkm7-0 h6.sc-1x9oq14-0");

  private final Waiters waiters;

  @Inject
  public CourseCatalogPage(WebDriver driver, Waiters waiters) {
    super(driver);
    this.waiters = waiters;
  }

  public CourseCatalogPage searchForCourse(String courseName) {
    WebElement searchInput = driver.findElement(By.cssSelector("input[type='search']"));
    searchInput.clear();
    searchInput.sendKeys(courseName);

    waiters.waitForElementClickableByLocator(COURSE_TITLES);
    return this;
  }

  public CourseCatalogPage findAndClickCourseByName(String courseName) {
    WebElement course = driver.findElements(COURSE_TITLES).stream()
        .filter(c -> c.getText().trim().equalsIgnoreCase(courseName))
        .findFirst()
        .orElseThrow(() -> new AssertionError("Курс с именем '" + courseName + "' не найден."));

    course.click();
    return this;
  }

}
