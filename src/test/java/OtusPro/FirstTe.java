package OtusPro;

import extensions.UIExtensions;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CourseCardPage;
import pages.MainPage;

@ExtendWith(UIExtensions.class)
public class FirstTe {

  @Inject
  private MainPage mainPage;

  @Inject
  private CourseCardPage courseCardPage;

  @Test
  public void clickCreateTeacherButton() {
    mainPage
        .open()
//        .open("urlWithCategory","clickhouse");
        .clickNewTechButton()
        .pageHeaderShouldBeSameAs("Делись знаниями")
        .technologyBlockShouldBeVisible();
  }

}
