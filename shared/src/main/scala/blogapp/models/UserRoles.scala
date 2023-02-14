package blogapp.models

import zio.json._

sealed trait UserRoles {
  def role: String
}

object UserRoles {

  case object Empty extends UserRoles {
    override def role: String = "Select..."
  }

  case object Author extends UserRoles  {
    override def role: String = "Author"
  }

  case object Reader extends UserRoles {
    override def role: String = "Reader"
  }

  case object Editor extends UserRoles {
    override def role: String = "Editor"
  }


  /** Converts a string to its Species representation. */
  def fromString(s: String): UserRoles = s match {
    case "Author"  => Author
    case "Reader"  => Reader 
    case "Editor"  => Editor 
    case _         => Empty
  }

  val all: List[UserRoles] = List(Empty, Author, Reader, Editor)

  implicit val codec: JsonCodec[UserRoles] = DeriveJsonCodec.gen[UserRoles]

}
