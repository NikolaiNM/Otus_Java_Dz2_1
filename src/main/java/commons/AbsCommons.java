package commons;

import com.google.inject.Inject;
import commons.waiters.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import scopeds.ScenarioCucumberScoped;
import java.util.List;

public abstract class AbsCommons {

  protected WebDriver driver;
  protected Waiters waiters;

  @Inject
  public AbsCommons(ScenarioCucumberScoped cucumberScoped) {
    this.driver = cucumberScoped.getDriver();
    this.waiters = new Waiters(cucumberScoped);

    PageFactory.initElements(driver, this);
  }




  public WebElement $(By locator) {
    return driver.findElement(locator);
  }

  public List<WebElement> $$(By locator) {
    return driver.findElements(locator);
  }
}