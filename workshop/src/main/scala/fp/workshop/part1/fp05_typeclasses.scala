package fp.workshop.part1

object fp05_typeclasses {

  object show {

    trait Show[A] {
      def show(v: A): String = ???
    }

    object Show {
      def apply[A](implicit show: Show[A]): Show[A] = show
    }

    //  Show[Int].show(1)
  }

  object complex {

    object oop {

      // 3rdParty
      trait Moto {
        val fuelLevel: Int = 33

        def drive: Unit = println("I'm driving on the road")
      }

      // 3rdParty
      trait Glider {

        def fly: Unit = println("i'm flying")
      }

      trait MotoGlider extends Moto with Glider

      val motoGlider: MotoGlider = new MotoGlider {}
//      motoGlider.park
    }

    object typeclasses {

      trait Moto {
        val fuelLevel: Int = 33
        def drive(): Unit = println("I'm driving on the road")
      }

      trait Glider {
        def fly(): Unit = println("i'm flying")
      }

      trait MotoGlider extends Glider with Moto

      // syntax called extension method can be added to type
      // to make it easier to use typeclasses

      val motoGlider = new MotoGlider {}
//      motoGlider.park()

      val moto = new Moto {}
//      moto.park()
    }
  }
}