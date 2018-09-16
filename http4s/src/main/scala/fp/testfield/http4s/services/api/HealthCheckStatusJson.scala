package fp.testfield.http4s.services.api

case class HealthCheckStatusJson(name: String, version: String)

object HealthCheckStatusJson {
  import io.circe.Encoder, io.circe.generic.semiauto._

  implicit val healthCheckStatusJsonEncoder: Encoder[HealthCheckStatusJson] = deriveEncoder[HealthCheckStatusJson]
}