package forex.interfaces.api

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import forex.config.ForexProxyConfig
import forex.domain.OneForge.Quote
import forex.interfaces.api.rates.Protocol.GetApiResponse
import forex.interfaces.api.utils.ApiMarshallers._
import forex.main.{Processes, Runners}
import forex.services.oneforge.{ForexCachedProxy, OneForgeClient, Probe}
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpec}

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration


class ProcessesSpec extends WordSpec with Matchers with ScalatestRouteTest with BeforeAndAfterAll{

  var route: Route = _
  var proxy: ForexCachedProxy = _

  override def beforeAll() {
    super.beforeAll()
    proxy = ForexCachedProxy(
      ForexProxyConfig(FiniteDuration(1, TimeUnit.MILLISECONDS), FiniteDuration(2, TimeUnit.SECONDS)),
      OneForgeClientStub
    )
    val ratesRoutes = rates.Routes(
      Processes(proxy),
      Runners()
    )
    route = Routes(ratesRoutes).route
    proxy.stopProxy() // FIXME reduce logs by logback config
  }

  override def afterAll(): Unit = {
    super.afterAll()
    proxy.stopProxy()
  }


  "The configured service" should {

    "return Rate with correct syntax" in new ProxyTest{
      Get(uri) ~> route ~> check {
        val given = responseAs[GetApiResponse]
        given.from shouldEqual from
        given.to shouldEqual to
      }
    }
    // time has only seconds precision, need to have better way of time testing
    "return second Rate with later time" ignore new ProxyTest{
      Get(uri) ~> route ~> check {
        val earlier = responseAs[GetApiResponse].timestamp.value
        Thread.sleep(1) // brr, better time manipulation?
        Get(uri) ~> route ~> check {
          val later = responseAs[GetApiResponse].timestamp.value
          assert( later.isAfter(earlier), s"later/earlier: $later/$earlier")
        }
      }

    }
  }

  private trait ProxyTest {
    val (from, to) = (Probe.from, Probe.to)

    val query = Uri.Query("from" → from.toString, "to" → to.toString)
    val uri = Uri("/").withQuery(query)
  }
}

object OneForgeClientStub extends OneForgeClient {
  override def getAll: Future[QuotesResult] = Future.successful{
    Right(List(Quote("EURJPY", BigDecimal(1), BigDecimal(1), BigDecimal(1),
      System.currentTimeMillis()/1000)
    ))
  }
}