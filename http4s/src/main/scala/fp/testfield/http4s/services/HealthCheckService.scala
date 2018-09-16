package fp.testfield.http4s.services

import cats.Monad
import fp.testfield.http4s.services.api.HealthCheckStatusJson
import org.http4s.HttpService
import org.http4s.dsl.Http4sDsl

class HealthCheckService[F[_] : Monad] extends Http4sDsl[F] {
  import io.circe.syntax._
  import org.http4s.circe._

  val service: HttpService[F] = HttpService[F] {
    case GET -> Root / "ping" => Ok("pong")
    case GET -> Root / "info" => Ok(HealthCheckStatusJson("test", "SNAPSHOT").asJson)
  }
}