package fp.workshop.part1

object fp10_applicative {
  import cats.Applicative

  object syntax {
    val listApplicative = new Applicative[List] {
      override def pure[A](x: A): List[A] = ???

      override def ap[A, B](ff: List[A => B])(fa: List[A]): List[B] = ???
    }

    val optionApplicative = new Applicative[Option] {
      override def pure[A](x: A): Option[A] = ???

      override def ap[A, B](ff: Option[A => B])(fa: Option[A]): Option[B] = ???
    }
  }

  object example {
    import cats.syntax.apply._
    import cats.instances.option._

    object Connection
    case class Config(user: Option[String], pass: Option[String])

    def createConnection(config: Config): Option[Connection.type] = {
      def connect(user: String, pass: String): Connection.type = Connection

      (config.user, config.pass).mapN(connect)
    }

  }
}
