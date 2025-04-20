package otus.steps.pages;

import com.google.inject.Inject;
import io.cucumber.java.ru.Пусть;
import pages.MainPage;

public class MainPageSteps {

  @Inject
  private MainPage mainPage;

  @Пусть("Открываем главную страницу")
  public void openMainePage() {
    mainPage.open("/");
  }

}
