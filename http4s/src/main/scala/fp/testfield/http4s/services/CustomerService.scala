package fp.testfield.http4s.services

import cats.Monad
import cats.effect.Sync
import fp.testfield.http4s.repository.CustomerRepository
import fp.testfield.http4s.services.api.models._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

class CustomerService[F[_]: Sync](
  customerRepository: CustomerRepository[F]
) extends Http4sDsl[F] {
  import io.circe.syntax._

  val service: HttpRoutes[F] = HttpRoutes.of[F] {
    case _ @GET -> Root =>
      Ok(
        fs2.Stream.emit("[") ++
          customerRepository.findAll.map { case (id, email) => CustomerJson(id, email).asJson.noSpaces }.intersperse(",") ++
          fs2.Stream.emit("]")
      )
  }

}
