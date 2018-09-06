package fp.workshop.part1

import fp.workshop.part1.fp09_apply.ap.eitherAp
import org.scalatest.FunSuite

class fp09_applyTest extends FunSuite {

  test("apply") {
    val ef: Either[String, String => Int] = Right(_.length)

    assert(eitherAp.ap(ef)(Right("aaa")) == Right(3))
    assert(eitherAp.map2(Right("aaa"), Left("a"))((a, b) => a + b) == Left("a"))

  }

}
