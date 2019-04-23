package forex.processes.rates

import forex.domain._
import scala.util.control.NoStackTrace

package messages { // not sure if this code style was intentional
  sealed trait Error extends Throwable with NoStackTrace
  object Error {
    final case class BackgroundSyncError(msg: String) extends Error
    final case object Generic extends Error
    final case class System(underlying: Throwable) extends Error
  }

  final case class GetRequest(
      from: Currency,
      to: Currency
  )
}
