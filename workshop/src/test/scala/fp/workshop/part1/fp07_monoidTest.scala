package fp.workshop.part1

import org.scalatest.FunSuite

class fp07_monoidTest extends FunSuite {
  import fp07_monoid._

  test("combine") {
    assert(combine(Some(1), None) == 1)
    assert(combine(None, Some(4)) == 4)
    assert(combine(Some(2), Some(4)) == 6)
    assert(combine(None, None) == 0)
  }

}
