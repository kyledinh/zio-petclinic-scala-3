package blogapp.services

import blogapp.models.{User, UserRoles, Uuid}
import zio.*

trait UserService {
 
  def create(
    firstName: String, 
    lastName: String, 
    address: String, 
    phone: String, 
    email: String 
  ): Task[User]
 
  def delete(id: Uuid): Task[Unit]
 
  def get(id: Uuid): Task[Option[User]]
 
  def getAll: Task[List[User]]
 
  def update(
      id: Uuid,
      firstName: Option[String],
      lastName: Option[String],
      address: Option[String],
      phone: Option[String],
      email: Option[String]
  ): Task[Unit]

}

// object UserService {
//   def get(id: Uuid) =
//     ZIO.serviceWithZIO[UserService](_.get(id))
// }
