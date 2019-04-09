import scalaz.Apply
import scalaz.Monad
import scalaz.MonadError
import scalaz.Applicative
import scalaz.Disjunction
import scalaz.Id._
// import scalaz.syntax.apply._
// import scalaz.syntax.either._
// import scalaz.syntax.bifunctor._

import scalaz._
import Scalaz._

import scalaz.zio.{App, IO, UIO, ZIO}
import scalaz.zio.interop.scalaz72._

object ScalaZApp extends App {

  import scalaz.zio.DefaultRuntime
  type AppIO[A] = IO[Nothing, A]

  def applicative[F[_]: Applicative] =
    ^(1.point[F], 2.point[F]) {
      case (a, b) =>
        a + b
    }

  sealed trait Domain1Error
  final case class SimpleError(msg: String) extends Domain1Error

  sealed trait Domain2Error
  final case object SubsytemError extends Domain2Error

  type Domain2IO[A] = IO[Domain2Error, A]
  type Domain1IO[A] = IO[Domain1Error, A]

  def runDomain2(isError: Boolean): Domain2IO[String] =
    if (isError)
      IO.fail(SubsytemError)
    else
      IO.succeed("ok!")

  def domian1Pure[F[_]](isError: Boolean)(implicit ME: MonadError[F, Domain1Error]): F[Int] = {
    if (isError) ME.raiseError(SimpleError("pure error")) else 1.pure[F]
  }

  def runDomain1: Domain1IO[String] =
    for {
      _ <- domian1Pure[Domain1IO](true)
      _ <- runDomain2(true).leftMap {
        case SubsytemError => SimpleError("Subsystem error")
      }
      res <- IO.succeed("ok")
    } yield res

  def run(args: List[String]) = {
    runDomain1.either
      .flatMap { result =>
        IO(println(result))
      }
      .fold(_ => -1, _ => 0)
  }
}
