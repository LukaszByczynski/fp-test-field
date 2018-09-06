package fp.workshop.part1

import org.scalatest.FunSuite

class fp08_functorTest extends FunSuite {
  import fp08_functor._

  test("fmap") {
    assert(fmap.listFunctor.map(List("test", "go"))(_.length) == List(4, 2))
  }

  test("product") {
    assert(product.productFunctor.fproduct(Some(1))(_ + 2) == Some(1, 3))
  }

  test("coproduct") {
    assert(coproduct.coproductFunctor.map(List(Some(1), None, Some(3)))(_ * 2) == List(Some(2), None, Some(6)))
  }

  test("lift") {
    assert(lift.liftFunctor(List(1, 2, 3)) == List(1, 4, 9))
  }
}
