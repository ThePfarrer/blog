name := """query"""
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
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
