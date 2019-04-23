package forex.interfaces.api.rates

import forex.domain._
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object Protocol { // why as object and not just a package?

  final case class GetApiRequest(
      from: Currency,
      to: Currency
  )

  final case class GetApiResponse(
      from: Currency,
      to: Currency,
      price: Price,
      timestamp: Timestamp
  )

  object GetApiResponse {
    implicit val decoder: Decoder[GetApiResponse] = deriveDecoder[GetApiResponse]
    implicit val encoder: Encoder[GetApiResponse] = deriveEncoder[GetApiResponse]
  }

}
