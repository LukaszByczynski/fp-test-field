package fp.workshop.part1

import org.scalatest.FunSuite

class fp03_kindsTest extends FunSuite {
  import fp03_kinds._

  test("list size") {
    assert(SizedList.size(List(1 :: 2 :: 3 :: Nil)) == 3)
  }

  test("map size") {
    assert(SizedMap.size(Map(1 -> 2,  3 ->  4)) == 2)
    assert(SizedMap.size(Map(1 -> "a",  3 ->  "a")) == 2)
  }

  test("mapE size") {
    assert(SizedMapE.size(Map(1 -> 2,  3 ->  4)) == 2)
  }

}
