package fp.workshop.part1

import org.scalatest.FunSuite

class fp11_monadTest extends FunSuite {
  import fp11_monad._

  test("monad") {
    val program = for {
      init <- listMonad.pure(List(1))
      result <- listMonad.pure(init :+ 2)
    } yield result

    assert(program.flatten == List(1,2))
  }

}
