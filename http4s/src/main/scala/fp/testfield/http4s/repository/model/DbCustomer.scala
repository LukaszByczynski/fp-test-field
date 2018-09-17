package fp.testfield.http4s.repository.model

import java.util.UUID

import slick.jdbc.H2Profile.api._

class DbCustomer(tag: Tag) extends Table[(UUID, String)](tag, "customers") {
  def id: Rep[UUID] = column[UUID]("id", O.PrimaryKey)

  def email: Rep[String] = column[String]("email")

  def * = (id, email)
}
