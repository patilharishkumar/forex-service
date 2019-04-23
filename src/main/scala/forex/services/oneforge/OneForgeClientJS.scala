package forex.services.oneforge

import com.typesafe.scalalogging.LazyLogging
import forex.config.{ApplicationConfig, OneForgeConfig}
import forex.domain.OneForge.Quote
import forex.domain.Rate.Pair.allPairs
import fr.hmil.roshttp.HttpRequest
import io.circe
import io.circe.parser._
import monix.execution.Scheduler.Implicits.global
import org.zalando.grafter.macros.{defaultReader, readerOf}

import scala.concurrent.Future

@defaultReader[OneForgeClientJS]
trait OneForgeClient {
  type QuotesResult = circe.Error Either List[Quote]

  val allAsRequestStr: String =
    allPairs
      .map(p ⇒ p.from.toString + p.to.toString)
      .mkString(",")

  def getAll: Future[QuotesResult]
}

@readerOf[ApplicationConfig]
case class OneForgeClientJS( // TODO could use akka HTTP which was already in dependencies
    config: OneForgeConfig
) extends OneForgeClient with LazyLogging {
  import config.{apiKey, baseUrl}

  private def request(pairs: String) = HttpRequest(s"$baseUrl/quotes?pairs=$pairs&api_key=$apiKey")

  def getAll: Future[QuotesResult] =
    request(allAsRequestStr)
      .send()
      .map(res ⇒ {
        logger.debug("1Forge client fired")
        decode[List[Quote]](res.body)
      })
}
