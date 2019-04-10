package fp.testfield.cats

import cats._
import cats.implicits._

object encoding1 {

  trait Algebra1[F[_]] {
    def print: F[Unit]
  }

  object Algebra1 {

    def apply[F[_]: Applicative]: Algebra1[F] = new Algebra1[F] {
      println("alloc algebra1")
      def print: F[Unit] = Applicative[F].pure(println("algebra1"))
    }
  }
}

object encoding2 {

  trait Algebra2[F[_]] {
    def print: F[Unit]
  }

  object Algebra2 {
    def apply[F[_]: Applicative](implicit A: Algebra2[F]) = A
  }

  object impl {

    def apply[F[_]: Applicative](externalDependency: => String) = new Algebra2[F] {
      println("alloc algebra2")
      def print: F[Unit] = Applicative[F].pure(println(s"algebra2 $externalDependency"))
    }
  }
}

object ImplicitEncoding extends App {

  import encoding1.Algebra1
  import encoding2.Algebra2

  def prog1[F[_]: Monad] = {
    for {
      _ <- Algebra1[F].print
      _ <- Algebra1[F].print
    } yield ()
  }

  def prog2[F[_]: Algebra2: Monad] = {
    for {
      _ <- Algebra2[F].print
      _ <- Algebra2[F].print
    } yield ()
  }

  implicit val alg2impl = encoding2.impl[Id]("test ext")

  prog1[Id]
  prog2[Id]

}
