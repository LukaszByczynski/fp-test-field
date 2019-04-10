package fp.testfield.cats

import cats._
import cats.implicits._

import scala.util.Try

object KindProjectorExample extends App {

//  {type λ[α] = Either[String, α]})#W

//  type E = ({type λ[α] = Either[String, α]})#λ

  def foldIt[F[_]: Foldable](input: F[String]) =
    input.foldM[({ type λ[α] = Either[Int, α] })#λ, Throwable](new Throwable) {
      case (_, s) => Try(s.toInt).toEither.swap
    }

  println(foldIt(List("d", "u", "p", "7", "g")))
  println(foldIt(Vector("d", "u", "p", "a")))
  println(foldIt(List.empty[String]))

}
