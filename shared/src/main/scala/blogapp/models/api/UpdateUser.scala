package blogapp.models.api

import blogapp.models.{UserRoles}

import zio.json.*

final case class UpdateUser(
    firstName: Option[String],
    lastName: Option[String],
    address: Option[String],
    phone: Option[String],
    email: Option[String],
    role: Option[UserRoles]
)

object UpdateUser {
  implicit val codec: JsonCodec[UpdateUser] =
    DeriveJsonCodec.gen[UpdateUser]
}
