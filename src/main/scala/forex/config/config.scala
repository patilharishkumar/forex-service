package forex.config

import org.zalando.grafter.macros._

import scala.concurrent.duration.FiniteDuration

@readers
case class ApplicationConfig(
    akka: AkkaConfig,
    api: ApiConfig,
    oneForge: OneForgeConfig,
    forexProxy: ForexProxyConfig,
    executors: ExecutorsConfig
)

case class AkkaConfig(
    name: String,
    exitJvmTimeout: Option[FiniteDuration]
)

case class ApiConfig(
    interface: String,
    port: Int
)

case class ExecutorsConfig(
    default: String
)

case class OneForgeConfig(
    apiKey: String, // FIXME for security could require it from env parameter
    baseUrl: String
)

case class ForexProxyConfig(
    ttl: FiniteDuration,
    limit: FiniteDuration
) {
  require(limit.compare(ttl) >= 0, s"ttl/limit: $ttl/$limit")
}
