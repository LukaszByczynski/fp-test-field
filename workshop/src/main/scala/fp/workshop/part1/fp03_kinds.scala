package fp.workshop.part1

import scala.language.existentials

object fp03_kinds {

  // Type    Kind
  // Int     *
  // Option  * => *
  // Either  [*, *] => *

  // Scala does not have a kind annotation that looks like this: def foo[F: * => *, A: *](a: A).
  // All types default to kind of * (star), so we use this:
  def foo[F[_], A](fa: F[A]): F[A] = ???
  def bar[F[_, _], A, B](fa: F[A, B]): F[A, B] = ???

  trait Sized[F[_]] {
    def size[A](fa: F[A]): Int
  }

  lazy val SizedList: Sized[List] = ???

  // Partial application of the Map type, so that its kind turns from [*, *] => * into * => *
  type MapT[K] = Map[K, Any]
  lazy val SizedMap: Sized[MapT] = ???

  // we can use existential type
  type MapE[K] = Map[K,  T forSome { type T}]
  lazy val SizedMapE: Sized[MapE] = ???
}
