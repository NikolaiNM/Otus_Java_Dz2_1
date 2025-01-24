package OtusPro;

import extensions.UIExtensions;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CourseCatalogPage;
import pages.MainPage;

@ExtendWith(UIExtensions.class)
public class MainPageTest {

  @Inject
  private MainPage mainPage;

  @Test
  public void checkingCourseCategory() {
    mainPage
        .open();

  }

}
