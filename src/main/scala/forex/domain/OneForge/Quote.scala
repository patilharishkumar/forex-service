package forex.domain.OneForge

import io.circe.Decoder
import io.circe.generic.semiauto._

case class Quote(symbol: String, price: BigDecimal, bid: BigDecimal, ask: BigDecimal, timestamp: Long)

object Quote {
  implicit val decoder: Decoder[Quote] = deriveDecoder[Quote]
}
