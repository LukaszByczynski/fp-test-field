package fp.workshop.part1

object fp01_functions {

  object simple {
    val square: Int => Int = x => ???
  }

  object highOrder {
    def filterSeq[A](list: Seq[A], f: A => Boolean): Seq[A] = ???
  }

  object combinator {
    import cats.syntax.either._

    def alt(a: Either[String, Int], b: Either[String, Int]): Either[String, Int] = ???
  }

  object polymorphic {

    // we don't have polymorphic functions in scala :(
    //val identity[A] = (a: A) => a

    object identity {
      def apply[A](a: A): A = ???
    }

  }
}
