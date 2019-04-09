import scalaz._
import Scalaz._

import scalaz.zio._
import scalaz.zio.interop.scalaz72._

object Domain1 {

  sealed trait Domain1Error
  final case object UndefinedError extends Domain1Error

  // trait Domain1Algebra {

  /*_*/
  def testFunc[F[_]](throwError: Boolean)(implicit S: MonadState[F, Option[Int]]): F[\/[Domain1Error, String]] = {
    for {
      in <- S.get <* S.modify(_.map(_ + 1))
      res <- if (in < Some(3))
        S.point("ok".right)
      else
        S.point(UndefinedError.left)
    } yield res
    // Applicative[F].point(if (throwError) UndefinedError.left else "ok".right)
    // }

  }
}

class RefMonadState[S](ref: Ref[S]) extends MonadState[UIO, S] {
//  def get: UIO[S]                   = ref.get
//  def set(s: S): UIO[Unit]          = ref.set(s)
//  def inspect[A](f: S => A): UIO[A] = ref.get.map(f)
//  def modify(f: S => S): UIO[S]     = ref.update(f)
  override def init: UIO[S] = ref.get

  override def get: UIO[S] = ref.get

  override def put(s: S): UIO[Unit] = ref.set(s)

  override def bind[A, B](fa: UIO[A])(f: A => UIO[B]): UIO[B] = fa.flatMap(f)

  override def point[A](a: => A): UIO[A] = UIO.succeedLazy(a)
}

package object effects {
  implicit class RefEffects[A](val self: Ref[A]) {
    def runState[B](f: MonadState[UIO, A] => UIO[B]): UIO[B] =
      f(new RefMonadState(self))
  }
}

 object ex02_ft extends App {
   import effects._

   type AppIO[+A] = IO[Domain1.Domain1Error, A]

   def run(args: List[String]) = {
     val q = for {
       _ <- UIO(println("AAAA"))
       ref <- Ref.make[Option[Int]](None)
       z <- ref.runState { implicit state =>
         Domain1.testFunc[UIO](false)
       }
        _ <- UIO(println(z))
     } yield z

     q.fold(_ => -1, _ => 0)
   }
//
 }
