package sss.example

import cucumber.api.scala.{ ScalaDsl, EN }
import cucumber.api.PendingException

/**
 * @author alan
 */
class ExampleSteps extends ScalaDsl with EN {

  Given("""^a bad headcold$""") { () =>
    //// Write code here that turns the phrase above into concrete actions
    throw new PendingException()
  }
  Given("""^a super short deadline$""") { () =>
    //// Write code here that turns the phrase above into concrete actions
    throw new PendingException()
  }
  Then("""^bad news al round$""") { () =>
    //// Write code here that turns the phrase above into concrete actions
    throw new PendingException()
  }

}