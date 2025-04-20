package components;

import annotations.Component;
import com.google.inject.Inject;
import org.openqa.selenium.WebDriver;
import scopeds.ScenarioCucumberScoped;

@Component("css:.ads")
public class AdsBlock extends AbsBaseComponent {

  @Inject
  public AdsBlock(ScenarioCucumberScoped scenarioCucumberScoped) {
    super(scenarioCucumberScoped);
  }
}