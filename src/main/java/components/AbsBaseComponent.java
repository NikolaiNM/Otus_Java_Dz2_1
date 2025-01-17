package components;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import annotations.Component;
import commons.AbsCommons;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;


public abstract class AbsBaseComponent extends AbsCommons {

  public AbsBaseComponent(WebDriver driver) {
    super(driver);
  }

  {
    assertThat(waiters.waitForElementVisibleByLocator(getByOfComponent()))
        .as("err")
        .isTrue();
  }

  private By getByOfComponent() {
    Class clazz = getClass();
    if(clazz.isAnnotationPresent(Component.class)) {
      Component component = (Component) clazz.getDeclaredAnnotation(Component.class);
      String[] locator = component.value().split(":");
      switch (locator[0]) {
        case "css": {
          return  By.cssSelector(locator[1]);
        }
      }
    }
    return null;
  }

}
