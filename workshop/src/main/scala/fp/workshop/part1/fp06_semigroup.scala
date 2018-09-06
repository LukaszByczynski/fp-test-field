package fp.workshop.part1

object fp06_semigroup {
  import cats.syntax.semigroup._
  import cats.instances.int._

  def combineInts(a: Int, b: Int): Int = ???

  def combineList[A](a: List[A], b: List[A]): List[A] = ???

  def combineMap(a: Map[Int, String], b: Map[Int, String]): Map[Int, String] = ???

}
