package components;

import com.google.inject.Inject;
import commons.AbsCommons;
import org.openqa.selenium.By;
import scopeds.ScenarioCucumberScoped;

public class MenuCategoriesComponent extends AbsCommons {

  @Inject
  public MenuCategoriesComponent(ScenarioCucumberScoped scenarioCucumberScoped) {
    super(scenarioCucumberScoped);
  }

  public void  clickCategoryMenu(String category) {
    By locator = By.xpath("//*[text()='%s']");
    $(locator).click();
  }

}
