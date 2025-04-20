package pages;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.google.inject.Inject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import scopeds.ScenarioCucumberScoped;
import java.util.List;

public class EventsPage extends AbsBasePage<EventsPage> {

  @Inject
  public EventsPage(ScenarioCucumberScoped scenarioCucumberScoped) {
    super(scenarioCucumberScoped);
  }

  @FindBy(css = ".event_tiles")
  public List<WebElement> eventsTiles;

  public void eventsTilesShouldBePresent() {
    assertThat(eventsTiles.size())
        .as("err")
        .isGreaterThan(0);
  }

}
