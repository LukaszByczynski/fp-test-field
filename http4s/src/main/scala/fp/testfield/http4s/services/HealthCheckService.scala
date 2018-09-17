package fp.testfield.http4s.services

import cats.effect.Sync
import fp.testfield.http4s.services.api.models._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class HealthCheckService[F[_] : Sync] extends Http4sDsl[F] {
  import io.circe.syntax._
  import org.http4s.circe._

  val service: HttpRoutes[F] = HttpRoutes.of[F]  {
    case _ @ GET -> Root / "ping" => Ok("pong")
    case _ @ GET -> Root / "info" => Ok(HealthCheckStatusJson("test", "SNAPSHOT").asJson)
  }
}