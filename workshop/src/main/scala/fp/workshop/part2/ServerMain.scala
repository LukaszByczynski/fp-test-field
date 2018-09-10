package fp.workshop.part2

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext

object ServerMain extends IOApp {

  private implicit val ec: ExecutionContext = ExecutionContext.global

  override def run(args: List[String]): IO[ExitCode] = {
    ServerStream.stream[IO].compile.last.map {
      case Some(ExitCode.Success) => ExitCode.Success
      case _ => ExitCode.Error
    }
  }
}

object ServerStream {

  def jsonService[F[_]: Effect]: HttpRoutes[F] =
    new services.DummyService[F].jsonService

  def stream[F[_]: ConcurrentEffect](implicit ec: ExecutionContext): fs2.Stream[F, ExitCode] = {
    BlazeBuilder[F]
      .bindHttp(8080, "0.0.0.0")
      .mountService(jsonService, "/")
      .serve
  }
}
