package commons;

import com.google.inject.Inject;
import com.google.inject.Injector;
import commons.waiters.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class AbsCommons {

  protected WebDriver driver;

  protected Waiters waiters;

  public AbsCommons(WebDriver driver) {
    this.driver = driver;
    this.waiters = new Waiters(driver);

    PageFactory.initElements(driver,this);
  }

  public WebElement $(By locator) {
    return driver.findElement(locator);
  }

}
