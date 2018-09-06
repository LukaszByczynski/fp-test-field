package fp.workshop.part1

import org.scalatest.FunSuite

class fp10_applicativeTest extends FunSuite {
  import fp10_applicative._

  test("applicative compose") {
    assert((syntax.listApplicative compose syntax.optionApplicative).pure(1) == List(Option(1)))
  }

  test("connection") {
    assert(example.createConnection(example.Config(Some("user"), None)).isEmpty)
    assert(example.createConnection(example.Config(Some("user"), Some("pass"))).contains(example.Connection))
  }
}
