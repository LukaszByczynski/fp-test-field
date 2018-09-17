package fp.testfield.http4s.services.api
import java.util.UUID

object models {
  import io.circe.Encoder, io.circe.generic.semiauto._

  final case class HealthCheckStatusJson(name: String, version: String)
  final case class CustomerJson(uuid: UUID, email: String)

  implicit val healthCheckStatusJsonEncoder: Encoder[HealthCheckStatusJson] = deriveEncoder[HealthCheckStatusJson]
  implicit val customerJsonEncoder: Encoder[CustomerJson] = deriveEncoder[CustomerJson]
}