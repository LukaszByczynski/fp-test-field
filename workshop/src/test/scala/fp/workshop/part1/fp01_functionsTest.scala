package fp.workshop.part1

import org.scalatest.FunSuite

class fp01_functionsTest extends FunSuite {
  import fp01_functions._

  test("simple") {
    assert(simple.square(2) == 4)
  }

  test("highOrder") {
    assert(highOrder.filterSeq[Int](Seq(1, 2, 3), _ > 2) == Seq(3))
    assert(highOrder.filterSeq[String](Seq("a", "b", "aa"), _.contains("a")) == Seq("a", "aa"))
  }

  test("combinator") {
    assert(combinator.alt(Right(1), Right(2)) == Right(1))
    assert(combinator.alt(Right(1), Left("err")) == Right(1))
    assert(combinator.alt(Left("err"), Left("err")) == Left("err"))
    assert(combinator.alt(Left("err"), Right(1)) == Right(1))
  }

  test("polymorphic") {
    assert(polymorphic.identity(1) == 1)
  }
}
