package fp.workshop.part1

object fp02_types {

  object products {
    type Point = (Int, Int) // unnamed terms (just indexed access)
    final case class Person(name: String, age: Int) // named terms

    // The following are isomorphic, which means you can go between one and the other without losing information.
    // This is because the only value available for Unit is an instance of unit.
    final case class A(foo: Int, unit: Unit)

    final case class B(foo: Int)

    val to: A => B = (a: A) => ???
    val from: B => A = (b: B) => ???

    val isomorphicIdentity: A => A = to andThen from // This is equivalent to identity
  }

  object sums {

    sealed trait Currency
    case object USD extends Currency
    case object EUR extends Currency
    case object ILS extends Currency

    // true sums are only possible with shapeless co-products and
    // will be available in Scala 3
  }
}
