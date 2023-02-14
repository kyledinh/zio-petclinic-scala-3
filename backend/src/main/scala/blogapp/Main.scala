
import blogapp.server.OwnerRoutespackage blogapp

import blogapp.server._
import blogapp.services._
import zio._
import zio.logging.backend.SLF4J
import zio.logging.removeDefaultLoggers
// import zio.metrics.connectors.{MetricsConfig, newrelic}

import blogapp.server.BlogServer

import blogapp.services.OwnerServiceLiveobject Main extends ZIOAppDefault {

  /** Configures Metrics to be run at a set interval, in our case every five seconds */
  // val metricsConfig =
  //   ZLayer.succeed(MetricsConfig(5.seconds))

  override val run: Task[Unit] =
    ZIO
      .serviceWithZIO[BlogServer](_.start)
      .provide(
        BlogServer.layer,
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
        SLF4J.slf4j,
        removeDefaultLoggers,

        // newrelic.newRelicLayer,
        // newrelic.NewRelicConfig.fromEnvLayer,
        // metricsConfig
      )

}
