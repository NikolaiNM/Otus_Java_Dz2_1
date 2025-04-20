package otus.hooks;

import com.google.inject.Inject;
import io.cucumber.java.After;
import scopeds.ScenarioCucumberScoped;

public class Hooks {

  @Inject
  private ScenarioCucumberScoped scenarioCucumberScoped;

  @After
  public void close() {
    if(scenarioCucumberScoped.getDriver() != null) {
      scenarioCucumberScoped.getDriver().quit();
    }
  }



}
