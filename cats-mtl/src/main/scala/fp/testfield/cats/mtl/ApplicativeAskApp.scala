package scala.fp.testfield.cats.mtl

import cats._
import cats.effect.IO
import cats.effect.concurrent.Ref
import cats.implicits._
import cats.mtl.ApplicativeAsk

final case class FooConfig(id: Int)
final case class Config(fooConfig: FooConfig)

class Foo[F[_]: FlatMap] {

  def execute(implicit A: ApplicativeAsk[F, FooConfig]): F[String] = A.reader { config =>
    config.id.toString
  }

}

object Program {
  import com.olegpy.meow.hierarchy._

  def run[F[_]: Monad](foo: Foo[F])(implicit A: ApplicativeAsk[F, Config]): F[String] =
    for {
      res <- foo.execute
    } yield res
}

object ApplicativeAskApp extends App {
  import com.olegpy.meow.effects._

  val cfg = Config(FooConfig(1))

  val result = Ref[IO]
    .of(cfg)
    .flatMap(_.runAsk { implicit cfg =>
      Program.run[IO](new Foo[IO])
    })

  println(result.unsafeRunSync)
}
