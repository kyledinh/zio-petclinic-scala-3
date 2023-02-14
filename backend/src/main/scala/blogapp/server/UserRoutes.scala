package blogapp.server

import blogapp.models.api.{CreateUser, UpdateUser}
import blogapp.server.ServerUtils.{parseBody, parseUuid}
import blogapp.services.UserService
import zhttp.http.*
import zio.*
import zio.json.*

final case class UserRoutes(service: UserService) {

  val routes: Http[Any, Throwable, Request, Response] = Http.collectZIO[Request] {

    case Method.GET -> !! / "users" =>
      service.getAll.map(users => Response.json(users.toJson))

    case Method.GET -> !! / "user" / id =>
      for {
        id    <- parseUuid(id)
        user <- service.get(id)
      } yield Response.json(user.toJson)

    case req @ Method.POST -> !! / "user" =>
      for {
        UserBody <- parseBody[CreateUser](req)
        user <-
          service.create(
            UserBody.firstName,
            UserBody.lastName,
            UserBody.address,
            UserBody.phone,
            UserBody.email
          )
      } yield Response.json(user.toJson)

    case req @ Method.PATCH -> !! / "user" / id =>
      for {
        userId     <- parseUuid(id)
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

    case Method.DELETE -> !! / "user" / id =>
      for {
        id <- parseUuid(id)
        _  <- service.delete(id)
      } yield Response.ok

  }

}

object UserRoutes {
  val layer: URLayer[UserService, UserRoutes] = ZLayer.fromFunction(UserRoutes.apply _)
}
