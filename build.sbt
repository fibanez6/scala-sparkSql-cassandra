import Versions.*

import scala.collection.Seq

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.12.11"
ThisBuild / scalacOptions ++= Seq("-target:jvm-1.8")
ThisBuild / licenses := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt") )
ThisBuild / organization := "com.fibanez.spark"
ThisBuild / organizationName := "Fibanez"

lazy val connection = (project in file("connection"))
  .settings(
    libraryDependencies ++= Dependencies.Spark.dependencies
      ++ Dependencies.TestConnection.dependencies
      ++ Seq(
        "log4j"                     % "log4j"                             % Log4j,
        "joda-time"                 % "joda-time"                         % JodaTime,
        "com.github.jnr"            % "jnr-posix"                         % JnrPosix
    )
  )

lazy val job = (project in file("job"))
  .settings(
    libraryDependencies ++= Dependencies.Spark.dependencies
      ++ Dependencies.TestJob.dependencies
  )

lazy val root = (project in file("."))
  .aggregate(connection, job)
  .settings(
    name := "scala-sparkSql-cassandra",
    resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"
  )
