package fp.testfield.http4s.repository
import fp.testfield.http4s.repository.model.DbCustomer

class CustomerRepository[F[_]](implicit DB: DatabaseProvider[F]) {

  def findAll: fs2.Stream[F, DbCustomer] = {
    DB.runAsync(DB.customers.)
  }

}
