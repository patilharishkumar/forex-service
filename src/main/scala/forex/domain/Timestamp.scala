package forex.domain

import io.circe.java8.time._
import java.time.{OffsetDateTime, ZoneId}

import io.circe._
import io.circe.generic.extras.wrapped._

case class Timestamp(value: OffsetDateTime) extends AnyVal

object Timestamp {
  def now: Timestamp =
    Timestamp(OffsetDateTime.now(ZoneId.of("UTC")))

  implicit val decoder: Decoder[Timestamp] =
    deriveUnwrappedDecoder[Timestamp]
  implicit val encoder: Encoder[Timestamp] =
    deriveUnwrappedEncoder[Timestamp]
}
