package blogapp.models

import zio.json._

/** Species is a wrapper for a string which allows us to further explicitly
  * define what types of Species exist in our application.
  */
sealed trait UserRoles {
  def name: String
}

/** The companion object houses the definitive types of Species and wraps their
  * string representation in a case object.
  *
  * This conveniently allows Species to be matched on.
  */
object UserRoles {

  case object Empty extends UserRoles {
    override def name: String = "Select..."
  }

  case object Feline extends UserRoles  {
    override def name: String = "Feline"
  }

  case object Canine extends UserRoles {
    override def name: String = "Canine"
  }

  case object Avia extends UserRoles {
    override def name: String = "Avia"
  }

  case object Reptile extends UserRoles {
    override def name: String = "Reptile"
  }

  case object Suidae extends UserRoles {
    override def name: String = "Suidae"
  }

  /** Converts a string to its Species representation. */
  def fromString(s: String): UserRoles = s match {
    case "Feline"  => Feline
    case "Canine"  => Canine
    case "Avia"    => Avia
    case "Reptile" => Reptile
    case "Suidae"  => Suidae
    case _         => Empty
  }

  val all: List[UserRoles] = List(Empty, Feline, Canine, Avia, Reptile, Suidae)

  implicit val codec: JsonCodec[UserRoles] = DeriveJsonCodec.gen[UserRoles]

}
