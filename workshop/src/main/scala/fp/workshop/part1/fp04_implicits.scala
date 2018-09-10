package fp.workshop.part1

object fp04_implicits {

  object ordering {

    class A

    object A {
      implicit val a = new A {
        override def toString = "A"
      }
    }

    class B

    object B {
      implicit val b = new B {
        override def toString = "B"
      }
    }

    class C

    object C {
      implicit def a2c(implicit a: A) =
        new C {
          override def toString = a.toString
        }

      implicit def b2c(implicit b: B) =
        new C {
          override def toString = b.toString
        }
    }

    // error: ambiguous implicit values
    // implicitly
  }

  object allocation {
    type Id[A] = A

    trait Ufo[F[_]] {
      def info[A](a: A): F[Unit]
    }

    object Ufo {
//      def apply[F[_]]: Ufo[Id] = new Ufo[Id] {
//        println("alloc")
//        override def info[A](a: A): Id[Unit] =  println(s"${a.getClass} $a")
//      }

      def apply[F[_]](implicit ufo: Ufo[F]): Ufo[F] = ufo
      implicit def deriveUfo: Ufo[Id] = new Ufo[Id] {
        println("alloc")
        override def info[A](a: A): Unit = println(s"${a.getClass} $a")
      }
    }
  }

}
