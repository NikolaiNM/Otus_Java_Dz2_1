package commons.waiters;

import com.google.inject.Inject;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Waiters {

  private WebDriverWait webDriverWait;

  @Inject
  public Waiters(WebDriver driver) {
    this.webDriverWait = new WebDriverWait(
        driver,
        Duration.ofSeconds(Integer.parseInt(System.getProperty("webdriver.waiter.timeout")))
    );
  }

  public boolean waitForCondition(ExpectedCondition condition) {
    try {
      webDriverWait.until(condition);
      return true;
    } catch (TimeoutException ignored) {
      return false;
    }
  }

  public boolean waitForElementBylocator(By locator) {
    return this.waitForCondition(ExpectedConditions.presenceOfElementLocated(locator));
  }

  public boolean waitForElementVisibleElement(WebElement element) {
    return this.waitForCondition(ExpectedConditions.visibilityOf(element));
  }

  public boolean waitForElementVisibleByLocator(By locator) {
    return this.waitForCondition(ExpectedConditions.visibilityOfElementLocated(locator));
  }

  public boolean waitForElementClickableByLocator(By locator) {
    return this.waitForCondition(ExpectedConditions.elementToBeClickable(locator));
  }



}
