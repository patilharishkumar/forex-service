package forex.main

import forex.config._
import forex.services.oneforge.ForexCachedProxy
import forex.{services => s}
import forex.{processes => p}
import org.zalando.grafter.macros._

@readerOf[ApplicationConfig]
case class Processes(forexProxy: ForexCachedProxy) {

  implicit final lazy val _oneForge: s.OneForge[AppEffect] =
    s.OneForge.live[AppStack](forexProxy)

  final val Rates = p.Rates[AppEffect]

}
