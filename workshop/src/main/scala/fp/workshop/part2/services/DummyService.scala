package fp.workshop.part2.services

import cats.effect.Effect
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

class DummyService[F[_]: Effect] extends Http4sDsl[F] {

  val jsonService: HttpRoutes[F] = HttpRoutes.of[F] {

    case _ @POST -> Root / "hello" => Ok("")
  }
}
