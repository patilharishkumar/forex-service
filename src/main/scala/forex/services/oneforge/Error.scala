package forex.services.oneforge

import forex.domain.Rate

import scala.util.control.NoStackTrace

sealed trait Error extends Throwable with NoStackTrace
object Error {
  final case object Generic extends Error
  final case class System(underlying: Throwable) extends Error
  final case class RateTooOldError(lastRate: Rate) extends Error
}

