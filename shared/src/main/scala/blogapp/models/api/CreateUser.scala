package blogapp.models.api

import blogapp.models.UserRoles
import zio.json.*

final case class CreateUser(
  firstName: String, 
  lastName: String, 
  email: String, 
  phone: String, 
  address: String, 
  role: UserRoles
)

object CreateUser {
  implicit val codec: JsonCodec[CreateUser] =
    DeriveJsonCodec.gen[CreateUser]
}
