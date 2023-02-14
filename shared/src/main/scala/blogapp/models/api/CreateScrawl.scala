package blogapp.models.api

import zio.json.*
import blogapp.models.*

final case class CreateScrawl(
    title: String,
    body: String,
    createDate: java.time.LocalDate,
    status: PubStatus,
    userId: Uuid
)

object CreateScrawl {
  implicit val codec: JsonCodec[CreateScrawl] = DeriveJsonCodec.gen[CreateScrawl]
}
