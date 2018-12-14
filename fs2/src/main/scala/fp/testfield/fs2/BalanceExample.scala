package fp.testfield.fs2

import cats.effect._
import cats.implicits._
import fs2._

object BalanceExample extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {

    Stream
      .emits(List(1, 2, 3, 4, 33, 33,  44, 55, 77, 99, 88, 100, 220, 230, 450, 666))
      .covary[IO]
//      .balanceThrough(2,2)(in => {
//        in.map(i => println(s"${Thread.currentThread().getName} $i"))
//      })
      .balance(2)
      .take(2)
      .map(_.through(in => {
        in.map(i => println(s"${Thread.currentThread().getName} $i"))
      }))
      .parJoin(2)
      .compile
      .last
      .as(ExitCode.Success)


  }
}
