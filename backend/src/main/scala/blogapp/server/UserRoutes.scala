package blogapp.server

import blogapp.models.api.{CreateUser, UpdateUser}
import blogapp.server.ServerUtils.{parseBody, parseUserId}
import blogapp.services.UserService
import zhttp.http._
import zio._
import zio.json._

final case class UserRoutes(service: UserService) {

  val routes: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {

    case Method.GET -> !! / "users" =>
      service.getAll.map(users => Response.json(users.toJson))

    case Method.GET -> !! / "users" / id =>
      for {
        id    <- parseUserId(id)
        user <- service.get(id)
      } yield Response.json(user.toJson)

    case req @ Method.POST -> !! / "users" =>
      for {
        createUser <- parseBody[CreateUser](req)
        user <-
          service.create(
            createUser.firstName,
            createUser.lastName,
            createUser.address,
            createUser.phone,
            createUser.email
          )
      } yield Response.json(user.toJson)

    case req @ Method.PATCH -> !! / "users" / id =>
      for {
        userId     <- parseUserId(id)
        updateUser <- parseBody[UpdateUser](req)
        _ <- service.update(
               userId,
               updateUser.firstName,
               updateUser.lastName,
               updateUser.address,
               updateUser.phone,
               updateUser.email
             )
      } yield Response.ok

    // Deletes a single Owner found by their parsed ID and returns a 200 status code indicating success.
    case Method.DELETE -> !! / "users" / id =>
      for {
        id <- parseUserId(id)
        _  <- service.delete(id)
      } yield Response.ok

  }

}

/** Here in the companion object we define the layer that will be used to
  * provide the routes for the OwnerService API.
  */
object UserRoutes {
  val layer: URLayer[UserService, UserRoutes] = ZLayer.fromFunction(UserRoutes.apply _)
}
