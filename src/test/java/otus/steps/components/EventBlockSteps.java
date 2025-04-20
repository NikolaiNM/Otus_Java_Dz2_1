package otus.steps.components;

import com.google.inject.Inject;
import components.EventBlockComponent;
import io.cucumber.java.ru.Если;
import io.cucumber.java.ru.Тогда;

public class EventBlockSteps {

  @Inject
  private EventBlockComponent eventBlockComponent;

  @Если("Кликнуть на плитку Мероприятия")
  public void clickEventBlock(String blockTitle) {
    eventBlockComponent.clickBlockTitle();
  }

}
