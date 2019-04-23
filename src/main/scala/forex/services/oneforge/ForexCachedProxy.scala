package forex.services.oneforge
import cats.Eval
import com.typesafe.scalalogging.LazyLogging
import forex.config.{ApplicationConfig, ForexProxyConfig}
import forex.domain.{Currency, Rate, Timestamp}
import forex.interfaces.api.rates.Converters.toRates
import forex.services.oneforge.Error.RateTooOldError
import org.zalando.grafter._
import org.zalando.grafter.macros.readerOf

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

@readerOf[ApplicationConfig]
case class ForexCachedProxy(
    config: ForexProxyConfig,
    client: OneForgeClient
) extends Start
    with Stop
    with LazyLogging {

  def rate(pair: Rate.Pair): Error Either Rate = {
    val candidate = quotes(pair)
    val now = Timestamp.now.value
    if(candidate.timestamp.value.plusSeconds(config.limit.toSeconds)
      .isBefore( now)
    ) {
      logger.warn(s"Service do not meet the time limit requirements: given: ${candidate.timestamp}; limit: ${config.limit}; now: $now")
      Left(RateTooOldError(candidate))
    }
    else {
      Right(candidate)
    }
  }

  private var quotes: Rate.Pair Map Rate = Map()

  private val task = {
    val t = new java.util.Timer()
    val task = new java.util.TimerTask {
      def run(): Unit = synchronizeWithOneForge()
    }
    t.schedule(task, config.ttl.toMillis, config.ttl.toMillis)
    task
  }
  private def synchronizeWithOneForge(): Unit = {
    logger.info("Synchronizing with 1forge started")
    client.getAll.onComplete {
      case Success(value) => value match {
        case Right(result) ⇒
          if (result.nonEmpty) {
            quotes = toRates(result).toMap
            logger.info(
              "Synchronizing finished. Updated pair example: " + quotes(Probe.pair)
            )
          } else logger.error("Synchronizing finished. Empty results detected")

        case Left(err) ⇒
          logger.error(err.getMessage)
      }
      case Failure(e) =>
        logger.error("Synchronizing failed with Future exception: " + e.getMessage)
    }

  }
  def stopProxy(): Unit = task.cancel()

  private val className = this.getClass.getSimpleName

  override def start: Eval[StartResult] = StartResult.eval(className) {
    synchronizeWithOneForge()
    var it = 4
    while (it > 0 && quotes.isEmpty) {
      Thread.sleep(100)
    }
    if (quotes.nonEmpty)
      StartOk
    else
      StartFailure("Didn't retrieve data from OneForge on time.")
  }

  override def stop: Eval[StopResult] = StopResult.eval("OneForgeHttpClient") {
    stopProxy()
  }
}

object Probe {
  val from: Currency = Currency.fromString("EUR")
  val to: Currency = Currency.fromString("JPY")
  val pair = Rate.Pair(from, to)
}
