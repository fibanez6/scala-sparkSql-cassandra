import scala.collection.Seq
import Versions._

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.11"
ThisBuild / scalacOptions ++= Seq("-target:jvm-1.8")
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt") )
ThisBuild / organization := "com.fibanez.spark"
ThisBuild / organizationName := "Fibanez"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala-sparkSql-cassandra",
    libraryDependencies ++= Seq(
      "com.datastax.spark"        % "spark-cassandra-connector_2.12"    % sparkVersion,
      "org.apache.spark"         %% "spark-core"                        % sparkVersion,
      "org.apache.spark"         %% "spark-streaming"                   % sparkVersion,
      "org.apache.spark"         %% "spark-sql"                         % sparkVersion,
      "log4j"                     % "log4j"                             % log4jVersion,
      "joda-time"                 % "joda-time"                         % jodaTimeVersion,
      "com.github.jnr"            % "jnr-posix"                         % jnrPosixVersion,
      "org.scalatest"            %% "scalatest"                         % scalatestVersion     % "test"
    ),
    resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"
  )
