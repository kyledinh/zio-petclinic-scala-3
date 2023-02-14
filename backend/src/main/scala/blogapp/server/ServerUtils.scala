package blogapp.server

import blogapp.models.{Uuid, PetId, VetId, VisitId}
import zhttp.http.Request
import zio.json._
import zio.{Console, IO, ZIO}

object ServerUtils {

  def parseBody[A: JsonDecoder](request: Request): IO[AppError, A] =
    for {
      body   <- request.body.asString.orElseFail(AppError.MissingBodyError)
      parsed <- ZIO.from(body.fromJson[A]).mapError(msg => new AppError.JsonDecodingError(msg))
    } yield parsed

  // def parsePetId(id: String): IO[AppError.InvalidIdError, PetId] =
  //   PetId.fromString(id).orElseFail(AppError.InvalidIdError("Invalid pet id"))

  // def parseVisitId(id: String): IO[AppError.InvalidIdError, VisitId] =
  //   VisitId.fromString(id).orElseFail(AppError.InvalidIdError("Invalid visit id"))

  // def parseVetId(id: String): IO[AppError.InvalidIdError, VetId] =
  //   VetId.fromString(id).orElseFail(AppError.InvalidIdError("Invalid vet id"))

  def parseUserId(id: String): IO[AppError.InvalidIdError, Uuid] = 
    (for {
      _ <- Console.printLine("BEFORE parseUserID !!!!!!")
      uid <- Uuid.fromString(id)
      _ <- Console.printLine("After parseUserID")
    } yield uid
    ).orElseFail(AppError.InvalidIdError("Invalid user id"))
}
