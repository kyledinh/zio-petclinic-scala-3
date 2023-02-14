package blogapp.models

import zio.*
import zio.json.*

import blogapp.models.{User, UserRoles}

final case class User(
    id: Uuid,
    firstName: String,
    lastName: String,
    address: String,
    phone: String,
    email: String
) {
  def fullName: String = firstName + " " + lastName
}

object User {

  def make(
    firstName: String, 
    lastName: String, 
    address: String, 
    phone: String, 
    email: String 
  ): UIO[User] =
    Uuid.random.map(User(_, firstName, lastName, address, phone, email ))

  implicit val codec: JsonCodec[User] =
    DeriveJsonCodec.gen[User]

}
