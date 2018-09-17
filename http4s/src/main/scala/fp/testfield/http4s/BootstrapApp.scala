package fp.testfield.http4s

import cats.effect.{ExitCode, IO, IOApp}
import fp.testfield.http4s.repository.{CustomerRepository, DatabaseProvider}
import fp.testfield.http4s.services.{CustomerService, HealthCheckService}
import org.http4s.server.blaze.BlazeBuilder

object BootstrapApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    DatabaseProvider[IO].flatMap { implicit dbProvider =>
      BlazeBuilder[IO]
        .bindHttp(8080, "0.0.0.0")
        .mountService(new HealthCheckService[IO].service, "/status")
        .mountService(new CustomerService[IO](new CustomerRepository[IO]).service, "/customer")
        .serve
        .compile
        .last
        .map {
          case Some(ExitCode.Success) => ExitCode.Success
          case _ => ExitCode.Error
        }
    }
  }
}
