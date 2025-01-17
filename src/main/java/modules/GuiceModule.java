package modules;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import components.AdsBlock;
import factory.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import pages.CourseCardPage;
import pages.MainPage;
import pages.TeachPage;

public class GuiceModule extends AbstractModule {

  private WebDriver driver;

  public GuiceModule() {
    driver = new WebDriverFactory().create();
  }

  @Provides
  private WebDriver getDriver() {
    return this.driver;
  }

  @Singleton
  @Provides
  public MainPage getMainPage() {
    return new MainPage(driver);
  }

  @Singleton
  @Provides
  public TeachPage getTeachPage() {
    return new TeachPage(driver);
  }

  @Singleton
  @Provides
  public CourseCardPage getCourseCardPage() {
    return new CourseCardPage(driver);
  }

  @Singleton
  @Provides
  public AdsBlock getAdbBlock() {
    return new AdsBlock(driver);
  }

}
