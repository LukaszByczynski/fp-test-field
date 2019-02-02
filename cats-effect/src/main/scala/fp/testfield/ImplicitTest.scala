package fp.testfield

trait Test[F] {
  def p(): Unit
}

object Test {
  def apply[F](implicit T: Test[F]): Test[F] = T
}

object TestInstance {
  implicit val testInstance: Test[Int] = {
    println("create")
    new Test[Int] {
      override def p(): Unit = { println("p") }
    }
  }
}

object ImplicitTest extends App {

  import TestInstance._

  Test[Int].p()
  Test[Int].p()
  Test[Int].p()
  Test[Int].p()

}
