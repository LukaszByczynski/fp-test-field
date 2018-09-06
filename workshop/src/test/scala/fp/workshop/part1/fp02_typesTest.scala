package fp.workshop.part1

import org.scalatest.FunSuite

class fp02_typesTest extends FunSuite {

  test("product identity") {
    import fp02_types.products

    assert(products.isomorphicIdentity(products.A(1,())) == products.A(1,()))
  }

}
