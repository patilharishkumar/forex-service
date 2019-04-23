name := "forex"
version := "1.0.0"

scalaVersion := "2.12.8"
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-Ypartial-unification",
  "-language:experimental.macros",
  "-language:implicitConversions"
)

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

val circeVersion = "0.8.0"
val effVersion = "4.6.1"

libraryDependencies ++= Seq(
  "com.github.pureconfig"      %% "pureconfig"           % "0.7.2",
  "com.softwaremill.quicklens" %% "quicklens"            % "1.4.11",
  "com.typesafe.akka"          %% "akka-actor"           % "2.4.19",
  "com.typesafe.akka"          %% "akka-http"            % "10.0.13",
  "de.heikoseeberger"          %% "akka-http-circe"      % "1.18.1",
  "io.circe"                   %% "circe-core"           % circeVersion,
  "io.circe"                   %% "circe-generic"        % circeVersion,
  "io.circe"                   %% "circe-generic-extras" % circeVersion,
  "io.circe"                   %% "circe-java8"          % circeVersion,
  "io.circe"                   %% "circe-jawn"           % circeVersion,
  "io.circe"                   %% "circe-parser"         % circeVersion,
  "org.atnos"                  %% "eff"                  % effVersion,
  "org.atnos"                  %% "eff-monix"            % effVersion,
  "org.typelevel"              %% "cats-core"            % "0.9.0",
  "org.zalando"                %% "grafter"              % "2.3.0",
  "ch.qos.logback"             % "logback-classic"       % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging"        % "3.9.0",
  "com.beachape"               %% "enumeratum"           % "1.5.13",
  "fr.hmil"                    %% "roshttp"              % "2.1.0",
  compilerPlugin("org.spire-math"  %% "kind-projector" % "0.9.7"),
  compilerPlugin("org.scalamacros" %% "paradise"       % "2.1.1" cross CrossVersion.full),
  "org.scalatest"     %% "scalatest"         % "3.0.5"  % "test",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.3" % "test",
  "org.scalacheck"             %% "scalacheck"           % "1.14.0" % "test",
  "org.scalamock"              %% "scalamock"            % "4.1.0" % "test",
)
