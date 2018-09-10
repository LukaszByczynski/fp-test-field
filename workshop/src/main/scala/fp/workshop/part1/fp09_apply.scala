package fp.workshop.part1

object fp09_apply {
  import cats.Apply

  object ap {

    val eitherAp = new Apply[Either[String, ?]] {
      override def ap[A, B](ff: Either[String, A => B])(fa: Either[String, A]): Either[String, B] = ???

      override def map[A, B](fa: Either[String, A])(f: A => B): Either[String, B] = fa.map(f)
    }
  }

}
