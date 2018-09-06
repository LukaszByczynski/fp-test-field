package fp.workshop.part1

import org.scalatest.FunSuite

class fp06_semigroupTest extends FunSuite {
  import fp06_semigroup._

  test("combine ints") {
    assert(combineInts(1, 4) == 5)
  }

  test("combine list") {
    assert(combineList(List(1,2,3,4), List(4, 6, 7, 5)) == List(1,2,3,4,4,6,7,5))
  }

  test("combine map") {
    val a = Map(1 -> "olaf", 2 -> "elsa")
    val b = Map(2 -> " anna", 3 -> "chris")
    assert(combineMap(a, b) == Map(
      1 -> "olaf",
      2 -> "elsa anna",
      3 -> "chris"
    ))
  }

}
