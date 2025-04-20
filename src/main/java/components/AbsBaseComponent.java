package components;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import annotations.Component;
import com.google.inject.Inject;
import commons.AbsCommons;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import scopeds.ScenarioCucumberScoped;

public abstract class AbsBaseComponent extends AbsCommons {

  @Inject
  public AbsBaseComponent(ScenarioCucumberScoped scenarioCucumberScoped) {
    super(scenarioCucumberScoped);
  }

  {
    assertThat(waiters.waitForElementVisible(getByOfComponent()))
        .as("Ошибка: компонент не видим на странице")
        .isTrue();
  }

  private By getByOfComponent() {
    Class<?> clazz = getClass();
    if (clazz.isAnnotationPresent(Component.class)) {
      Component component = clazz.getDeclaredAnnotation(Component.class);
      String[] locator = component.value().split(":");
      switch (locator[0]) {
        case "css":
          return By.cssSelector(locator[1]);
        default:
          throw new IllegalArgumentException("Unsupported locator type: " + locator[0]);
      }
    }
    throw new IllegalStateException("Компонент не имеет аннотации @Component");
  }
}