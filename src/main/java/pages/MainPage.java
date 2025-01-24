package pages;

import annotations.Path;
import com.google.inject.Inject;
import commons.waiters.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

@Path("/")
public class MainPage extends AbsBasePage<MainPage> {

  private static final By MENU_TEACHING_BUTTON = By.cssSelector("span[title='Обучение']");

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





}

