package blogapp.models.api

import zio.json._

final case class UpdateUser(
    firstName: Option[String],
    lastName: Option[String],
    address: Option[String],
    phone: Option[String],
    email: Option[String]
)

object UpdateUser {
  implicit val codec: JsonCodec[UpdateUser] =
    DeriveJsonCodec.gen[UpdateUser]
}
