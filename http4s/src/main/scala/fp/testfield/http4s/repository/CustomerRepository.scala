package fp.testfield.http4s.repository
import java.util.UUID

import cats.effect.ConcurrentEffect

class CustomerRepository[F[_]](implicit DB: DatabaseProvider[F], C: ConcurrentEffect[F]) {
  import slick.jdbc.H2Profile.api._

  def findAll: fs2.Stream[F, (UUID, String)] = {
    DB.runStream(DB.customers.result)
  }

}
