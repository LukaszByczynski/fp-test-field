package fp.workshop.part1

import scala.annotation.tailrec

object fp11_monad {

  import cats.Monad

  val listMonad = new Monad[List] {
    override def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = {
      fa.flatMap(f)
    }

    override def pure[A](x: A): List[A] = List(x)

    override def tailRecM[A, B](a: A)(f: A => List[Either[A, B]]): List[B] = {
      val buf = List.newBuilder[B]

      @tailrec def go(lists: List[List[Either[A, B]]]): Unit = lists match {
        case (ab :: abs) :: tail => ab match {
          case Right(b) => buf += b; go(abs :: tail)
          case Left(a) => go(f(a) :: abs :: tail)
        }
        case Nil :: tail => go(tail)
        case Nil => ()
      }

      go(f(a) :: Nil)
      buf.result
    }
  }


}
