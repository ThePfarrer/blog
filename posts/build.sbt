import com.typesafe.sbt.packager.docker.DockerChmodType
import com.typesafe.sbt.packager.docker.DockerPermissionStrategy

name         := """posts"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  guice,
  ws,
  "com.dripower"           %% "play-circe"         % "2814.2",
  "io.circe"               %% "circe-generic"      % "0.14.1",
  "io.circe"               %% "circe-parser"       % "0.14.1",
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
)

//enablePlugins(JavaAppPackaging)
//enablePlugins(DockerPlugin)
//dockerExposedPorts := Seq(9000) //expose default Play port

dockerChmodType          := DockerChmodType.UserGroupWriteExecute
dockerPermissionStrategy := DockerPermissionStrategy.CopyChown
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
