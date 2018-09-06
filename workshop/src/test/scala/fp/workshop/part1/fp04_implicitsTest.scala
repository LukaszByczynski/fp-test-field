package fp.workshop.part1

import org.scalatest.FunSuite

class fp04_implicitsTest extends FunSuite {

  import fp04_implicits.allocation._

  test("allocation") {
    def program: Unit = {
      Ufo[Id].info("test")
      Ufo[Id].info("test")
      Ufo[Id].info("test")
    }

    program
    Ufo[Id].info(2)
  }

}
