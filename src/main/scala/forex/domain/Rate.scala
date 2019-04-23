package forex.domain

import io.circe._
import io.circe.generic.semiauto._

case class Rate(
    pair: Rate.Pair,
    price: Price,
    timestamp: Timestamp
)

object Rate {
  final case class Pair(
      from: Currency,
      to: Currency
  )

  object Pair {
    val allPairs: Set[Pair] = // could ask only for half of pairs and to calculate the opositly ordered pair
      for {
        from ← Currency.all
        to ← Currency.all
        if from != to
      } yield Pair(from, to)

    implicit val encoder: Encoder[Pair] =
      deriveEncoder[Pair]
  }

  implicit val encoder: Encoder[Rate] =
    deriveEncoder[Rate]
}
