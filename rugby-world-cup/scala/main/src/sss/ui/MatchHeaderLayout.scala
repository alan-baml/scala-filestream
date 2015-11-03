package sss.ui

import com.vaadin.ui.Layout
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Notification
import akka.actor.ActorRef
import com.vaadin.ui.Component
import collection.JavaConversions._
import com.vaadin.server.ClassResource
import com.vaadin.ui.Image

class MatchHeaderLayout extends MatchUI {

  secondHalfProgress.setWidth("100%")
  firstHalfProgress.setWidth("100%")
  secondHalfProgress.setImmediate(true)
  firstHalfProgress.setImmediate(true)
  homeTeamLayout.setSpacing(false)
  homeTeamLayout.setMargin(false)
  //gametickerLayout.addStyleName("backColorBlue")

  //makeTicker(firstHalfLayout)
  //makeTicker(secondHalfLayout)

  //secondHalfLayout.addStyleName("backColorBlue")
  //firstHalfLayout.addStyleName("backColorGrey")

  def makeYellow: Component = {
    val png = new ClassResource("/yellowCard.png")
    println(s"${png.getFilename} ${png.getBufferSize}")
    val image = new Image(null, png);
    image.setWidth("20px")
    image.setHeight("20px")

    //image.addListener(eventType, target, method)
    image
  }

  def addHomeYellow {
    homeTeamYellow.addComponent(makeYellow)

  }

  def addAwayYellow {
    awayTeamYellow.addComponent(makeYellow)
  }

}
