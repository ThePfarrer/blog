import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy

name         := """query"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  guice,
  ws,
  "com.dripower"           %% "play-circe"         % "2814.2",
  "io.circe"               %% "circe-generic"      % "0.14.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
)

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

dockerChmodType          := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.CopyChown
dockerExposedPorts       := Seq(9002) //expose default Play port
dockerBaseImage          := "openjdk:11-jre-slim"
dockerRepository         := Some("thepfarrer")
dockerUpdateLatest       := true

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
