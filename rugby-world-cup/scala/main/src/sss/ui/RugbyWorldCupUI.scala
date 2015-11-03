package sss.ui;

import java.lang.annotation._
import collection.JavaConversions._
import javax.servlet.annotation._
import javax.servlet.annotation.WebServlet._
import com.vaadin.annotations.Theme
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.Button
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.FormLayout
import com.vaadin.ui.TextField
import com.vaadin.data.fieldgroup.FieldGroup
import com.vaadin.data.util.PropertysetItem
import com.vaadin.data.util.ObjectProperty
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Notification
import com.vaadin.data.fieldgroup.FieldGroup.CommitException
import scala.util.control.NonFatal
import com.vaadin.ui.HorizontalLayout
import java.text.SimpleDateFormat
import com.vaadin.data.Property
import com.vaadin.ui.Slider
import com.vaadin.shared.ui.slider.SliderOrientation
import akka.actor.ActorSystem
import akka.actor.Actor
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.Cancellable
import akka.actor.Props
import akka.actor.ActorRef
import com.vaadin.annotations.Push
import com.vaadin.ui.Layout
import com.vaadin.ui.Alignment
import com.vaadin.ui.AbstractComponent
import com.vaadin.ui.Component
import com.vaadin.server.ClassResource
import com.vaadin.ui.Image
import java.io.File
import com.vaadin.ui.Table
import com.vaadin.ui.AbsoluteLayout
import protocol._

@Theme("rugby-world-cup")
@Push
class RugbyWorldCupUI extends UI with DefaultActorSystem with UpdateUI {

  val gameActor = actorSystem.actorOf(Props(classOf[GameActor], this))

  val eventTable = new Table
  eventTable.setEditable(false)
  eventTable.setWidth("80%")

  eventTable.addContainerProperty("Minute", classOf[Integer], null);
  eventTable.addContainerProperty("Event", classOf[String], null);
  eventTable.setColumnExpandRatio("Minute", 1)
  eventTable.setColumnExpandRatio("Event", 5)
  eventTable.setPageLength(10);

  val matchHeaderLayout = new MatchHeaderLayout()

  val id = "irelandfrance"
  val tests = Seq(SetupEvent(id, 0, "Ireland", "France"),
    NonEvent(id, 10),
    StandardEvent(id, 55, "GOOOOOOLAAAAALLLLL!!!!"),
    YellowEvent(id, 75, true))

  def createEventLayout: Layout = {
    val eventLayout = new VerticalLayout();

    eventLayout.setMargin(false)
    eventLayout.setWidth("100%")
    eventLayout.addStyleName("backColorGreen")
    eventLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER)

    val btn = new Button("Test")
    btn.addClickListener(new ClickListener {
      def buttonClick(ev: ClickEvent) {
        tests foreach (e => CoordinatingActor.coordinator ! e)
      }
    })
    eventLayout.addComponents(eventTable, btn)
    eventLayout

  }

  def addEvent(minute: Int, text: String) {

    val i = eventTable.addItem()
    val r = eventTable.getItem(i);
    val p = r.getItemProperty("Minute").asInstanceOf[Property[Integer]]
    p.setValue(minute)
    val p2 = r.getItemProperty("Event").asInstanceOf[Property[String]]
    p2.setValue(text)
    eventTable.setCurrentPageFirstItemIndex(eventTable.size())
  }

  @Override
  protected def init(request: VaadinRequest) {
    val gameId = Option(request.getParameter("gameId")).getOrElse("defaultGame")
    val layout = new VerticalLayout();
    layout.setMargin(false);
    layout.setSizeFull
    setContent(layout)

    layout.addStyleName("backColorGreen")
    val eventLayout = createEventLayout
    layout.addComponents(matchHeaderLayout, eventLayout)
    layout.setExpandRatio(matchHeaderLayout, 1)
    layout.setExpandRatio(eventLayout, 2)

    gameActor ! Init(gameId)
  }

  override protected def detach() {
    gameActor ! Detach
    super.detach()
  }

  def updateUI(f: => Unit) {
    getUI.access(new Runnable {
      def run = f
    })
  }

  def handle(e: SetupEvent) {
    updateUI {
      matchHeaderLayout.homeTeamLabel.setValue(e.home)
      matchHeaderLayout.awayTeamLabel.setValue(e.away)
    }
  }

  def handle(e: NonEvent) {
    updateUI {
      updateMinute(e.minute)
    }
  }

  private def updateMinute(minute: Int) {

    def scale(i: Float): Float = i / 40

    import matchHeaderLayout._
    if (minute > 40) {
      secondHalfProgress.setValue(scale(minute - 40))
    } else {
       firstHalfProgress.setValue(scale(minute))
       if (minute == 80) Notification.show("Game Over!!")
    }

  }

  def handle(e: YellowEvent) {
    updateUI {
      if (e.homeaway) matchHeaderLayout.addHomeYellow
      else matchHeaderLayout.addAwayYellow
      updateMinute(e.minute)
    }
  }

  def handle(e: StandardEvent) {
    updateUI {
      updateMinute(e.minute)
      addEvent(e.minute, e.text)
    }
  }

}

case object Detach
case class Init(gameId: String)

trait UpdateUI {
  def handle(e: SetupEvent)
  def handle(e: NonEvent)
  def handle(e: YellowEvent)
  def handle(e: StandardEvent)
}

class GameActor(ui: UpdateUI) extends Actor {

  import ui._

  def receive = initialising

  def initialising: Receive = {
    case Init(gameId) =>
      context.become(inGame(gameId))
      CoordinatingActor.coordinator ! Register(gameId)
  }

  def inGame(gameId: String): Receive = {
    case e: SetupEvent => handle(e)
    case e: NonEvent => handle(e)
    case e: StandardEvent => handle(e)
    case e: YellowEvent => handle(e)
    case Detach => CoordinatingActor.coordinator ! UnRegister(gameId)
  }
}
