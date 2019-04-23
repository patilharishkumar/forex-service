package forex.interfaces.api.rates

import java.time.{Instant, OffsetDateTime, ZoneId}

import forex.domain.OneForge.Quote
import forex.domain._
import forex.processes.rates.messages._

object Converters {
  import Protocol._

  def toGetRequest(
      request: GetApiRequest
  ): GetRequest =
    GetRequest(request.from, request.to)

  def toGetApiResponse(
      rate: Rate
  ): GetApiResponse =
    GetApiResponse(
      from = rate.pair.from,
      to = rate.pair.to,
      price = rate.price,
      timestamp = rate.timestamp
    )

  def toRates(quotes: List[Quote]): List[(Rate.Pair, Rate)] =
    quotes.map(el ⇒ {
      val pair = Rate.Pair(
        Currency.fromString(el.symbol.take(3)),
        Currency.fromString(el.symbol.drop(3))
      )
      val rate = Rate(
        pair,
        Price(el.price),
        milisToOffestDateTIme(el) // brr
      )
      pair → rate
    })

  def milisToOffestDateTIme(el: Quote): Timestamp = {
    Timestamp(OffsetDateTime.ofInstant(
      Instant.ofEpochMilli(el.timestamp*1000), ZoneId.of("UTC")
    ))
  }
}
