package forex.interfaces.api.utils

import akka.http.scaladsl._
import akka.http.scaladsl.model.HttpResponse
import forex.processes._
import forex.processes.rates.messages.Error.BackgroundSyncError

object ApiExceptionHandler {

  def apply(): server.ExceptionHandler =
    server.ExceptionHandler {
      case BackgroundSyncError(msg) ⇒
        ctx ⇒
          ctx.complete( HttpResponse(500, entity = "Proxy error." + msg))
      case err: RatesError ⇒
        ctx ⇒
          ctx.complete( HttpResponse(500, entity = "Undocumented API Error." + err.getMessage))
      case t: Throwable ⇒
        ctx ⇒
          ctx.complete(HttpResponse(500, entity = "Unexpected happend: " + t.getMessage))
    }

}
