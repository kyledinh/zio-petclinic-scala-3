package petclinic

import petclinic.server._
import petclinic.services._
import zio._
import zio.logging.backend.SLF4J
import zio.logging.removeDefaultLoggers
import zio.metrics.connectors.{MetricsConfig, newrelic}

/** Main is the entry point for the application.
  *
  * Because it extends ZIOAppDefault, we are required to define a `run` method
  * in which our application logic is housed. `run` is responsible for starting
  * the application and providing it with the necessary dependencies.
  */
object Main extends ZIOAppDefault {

  /** Configures Metrics to be run at a set interval, in our case every five
    * seconds
    */
  // val metricsConfig =
  //   ZLayer.succeed(MetricsConfig(5.seconds))

  /** As mentioned above, `provide` is used to pass along the dependencies
    * required by this ZIO effect.
    */
  override val run: Task[Unit] =
    ZIO
      .serviceWithZIO[ClinicServer](_.start)
      .provide(
        ClinicServer.layer,
        PetRoutes.layer,
        VetRoutes.layer,
        OwnerRoutes.layer,
        VisitRoutes.layer,
        QuillContext.dataSourceLayer,
        OwnerServiceLive.layer,
        PetServiceLive.layer,
        VetServiceLive.layer,
        VisitServiceLive.layer,
        Migrations.layer,
        // SLF4J.slf4j(LogLevel.Info),
        // removeDefaultLoggers,



        // newrelic.newRelicLayer,
        // newrelic.NewRelicConfig.fromEnvLayer,
        // metricsConfig
      )

}
