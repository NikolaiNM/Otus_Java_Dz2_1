package components;

import com.google.inject.Inject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import scopeds.ScenarioCucumberScoped;

public class EventBlockComponent extends AbsBaseComponent {

  @Inject
  public EventBlockComponent(ScenarioCucumberScoped scenarioCucumberScoped) {
    super(scenarioCucumberScoped);
  }

  @FindBy(css = ".event_block")
  private WebElement blockTitle;

  public void clickBlockTitle() {
    blockTitle.click();
  }

}
