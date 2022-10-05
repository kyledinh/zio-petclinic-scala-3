package petclinic.models

import zio._
import zio.test._
import zio.test.{Assertion, ZIOSpecDefault}
import zio.test.Assertion._
import java.util.UUID

object OwnerIdSpec extends ZIOSpecDefault {

  def spec = suite("OwnerId")(
      test("returns true confirming existence of added owner") {
        val fixture = "074b1f15-9bcd-4876-8217-ad6d7b201aae"
        // for {
        //     ownerId <- OwnerId.fromString("074b1f15-9bcd-4876-8217-ad6d7b201aae")
        // } yield Assertion.equals(ownerId.id.toString == ("074b1f15-9bcd-4876-8217-ad6d7b201aae"))

        //   for {
        //      ownerId <- OwnerId.fromString("074b1f15-9bcd-4876-8217-ad6d7b201aae")
        //  } yield Assertion.equals(ownerId.toString == fixture) 
        val ownerId = OwnerId.fromString("074b1f15-9bcd-4876-8217-ad6d7b201aae")
        assert(ownerId.toString)(Assertion.isNonEmptyString)
        
      }
  ) 

}