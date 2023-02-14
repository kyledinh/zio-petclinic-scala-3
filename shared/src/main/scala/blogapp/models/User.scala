package blogapp.models

import zio._
import zio.json._

import blogapp.models.User
import blogapp.models.UserRoles

final case class User(
    id: Uuid,
    firstName: String,
    lastName: String,
    address: String,
    phone: String,
    email: String,
    role: UserRoles 
) {
  def fullName: String = firstName + " " + lastName
}

object User {

  def make(firstName: String, lastName: String, address: String, phone: String, email: String, role: UserRoles): UIO[User] =
    Uuid.random.map(User(_, firstName, lastName, address, phone, email, role))

  implicit val codec: JsonCodec[User] =
    DeriveJsonCodec.gen[User]

}
