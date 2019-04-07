import scalaz.Apply
import scalaz.Applicative
import scalaz.Disjunction
import scalaz.Id._
import scalaz.syntax.apply._
import scalaz.syntax.either._

object ScalaZApp extends App {

  def applicative[F[_]: Applicative] =
    ^(Applicative[F].point(1), Applicative[F].point(2)) {
      case (a, b) =>
        a + b
    }.right

  println(applicative[Id])
}
