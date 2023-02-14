package blogapp.services

import blogapp.models.*
import zio.*
import io.getquill.*

trait UserService {
 
  def create(firstName: String, lastName: String, address: String, phone: String, email: String): Task[User]
 
  def delete(id: Uuid): Task[Unit]
 
  def get(id: Uuid): Task[Option[User]]
 
  def getAll: Task[List[User]]
 
  def update(
      id: Uuid,
      firstName: Option[String] = None,
      lastName: Option[String] = None,
      address: Option[String] = None,
      phone: Option[String] = None,
      email: Option[String] = None,
      role: Option[UserRoles] = None
  ): Task[Unit]

}

object UserService {
  def get(id: Uuid) =
    ZIO.serviceWithZIO[UserService](_.get(id))
}
