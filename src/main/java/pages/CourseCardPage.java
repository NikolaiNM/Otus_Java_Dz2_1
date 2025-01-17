package pages;

import annotations.Path;
import annotations.UrlData;
import annotations.UrlTemplate;
import org.openqa.selenium.WebDriver;

@Path("/lessons")
@UrlData({
    @UrlTemplate(name = "urlWithCategory", template = "/$1"),
//    @UrlTemplate(name = "urlWithDataPublication", template = "/$1/$2"),
})
public class CourseCardPage extends AbsBasePage<CourseCardPage> {

  public CourseCardPage(WebDriver driver) {
    super(driver);
  }

}
