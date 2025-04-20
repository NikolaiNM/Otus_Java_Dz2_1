package otus.steps.pages;

import com.google.inject.Inject;
import io.cucumber.java.ru.Тогда;
import pages.EventsPage;

public class EventPageSteps {

  @Inject
  private EventsPage eventsPage;

  @Тогда("Открывается страница с заголовком {string}")
  public void pageShouldBeHeaderSameAs(String header) {
    eventsPage.pageHeaderShouldBeSameAs(header);
  }

  @Тогда("На странице мероприятия отображаются плитки событий")
  public void eventsTilesShouldBePresent() {
    eventsPage.eventsTilesShouldBePresent();
  }

}
