package fp.workshop.part1

object fp08_functor {
  import cats.Functor

  object fmap {
    val listFunctor: Functor[List] = new Functor[List] {
      override def map[A, B](fa: List[A])(f: A => B): List[B] = ???
    }
  }

  object product {
    val productFunctor: Functor[Option] = ???
  }

  object coproduct {

    type ListOption[A] = List[Option[A]]
    val coproductFunctor: Functor[ListOption] = ???
  }

  object lift {
    val liftFunctor: List[Int] => List[Int] = ???
  }
}
