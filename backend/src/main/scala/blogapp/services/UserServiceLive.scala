package blogapp.services

import blogapp.QuillContext
import blogapp.models.{User, UserRoles, Uuid}
import zio.*
import io.getquill.*

import javax.sql.DataSource

import blogapp.services.UserService

final case class UserServiceLive(dataSource: DataSource) extends UserService {

  import QuillContext._

  override def create(firstName: String, lastName: String, address: String, phone: String, email: String, role: UserRoles): Task[User] =
    for {
      user <- User.make(firstName, lastName, address, phone, email, role)
      _     <- run(query[User].insertValue(lift(user))).provideEnvironment(ZEnvironment(dataSource))
    } yield user

  /** `delete` uses `filter` to find an Owner in the database whose ID matches
    * the one provided and deletes it.
    *
    * Unit is returned to indicate that we are running this method for its side
    * effects, a deleted Owner gives us no information. This will either fail or
    * succeed.
    */
  override def delete(id: Uuid): Task[Unit] =
    run(query[User].filter(_.id == lift(id)).delete)
      .provideEnvironment(ZEnvironment(dataSource))
      .unit

  /** `get` uses `filter` to find an Owner in the database whose ID matches the
    * one provided and returns it.
    */
  override def get(id: Uuid): Task[Option[User]] =
    run(query[User].filter(_.id == lift(id)))
      .provideEnvironment(ZEnvironment(dataSource))
      .map(_.headOption)

  override def getAll: Task[List[User]] =
    run(query[User])
      .provideEnvironment(ZEnvironment(dataSource))

  /** `update` uses `filter` to find an Owner in the database whose ID matches
    * the one provided and updates it with the provided optional values.
    *
    * Because a user may not provide all optional values, `setOpt` is used to
    * preserve the existing value in the case one is not provided to replace it.
    * Unit is returned to indicate side-effecting code. Note that this
    * `dynamicQuery` is not generated at compile time.
    *
    * For more information on dynamic queries, see:
    * https://getquill.io/#writing-queries-dynamic-queries
    */
  override def update(
      id: Uuid,
      firstName: Option[String],
      lastName: Option[String],
      address: Option[String],
      phone: Option[String],
      email: Option[String],
      role: Option[UserRoles]
  ): Task[Unit] =
    run(
      dynamicQuery[User]
        .filter(_.id == lift(id))
        .update(
          setOpt(_.firstName, firstName),
          setOpt(_.lastName, lastName),
          setOpt(_.address, address),
          setOpt(_.phone, phone),
          setOpt(_.email, email)
        )
    )
      .provideEnvironment(ZEnvironment(dataSource))
      .unit

}

object UserServiceLive {
  val layer: URLayer[DataSource, UserService] = ZLayer.fromFunction(UserServiceLive.apply _)
}
