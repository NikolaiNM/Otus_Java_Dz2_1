package components.popups;

import com.google.inject.Inject;
import commons.AbsCommons;
import org.openqa.selenium.WebDriver;
import scopeds.ScenarioCucumberScoped;

public class AuthPopup extends AbsCommons implements IPopup<AuthPopup> {

  @Inject
  public AuthPopup(ScenarioCucumberScoped scenarioCucumberScoped) {
    super(scenarioCucumberScoped);
  }

  @Override
  public AuthPopup popupShouldNotBeVisible() {
    return null;
  }

  @Override
  public AuthPopup popupShouldBeVisible() {
    return null;
  }
}
