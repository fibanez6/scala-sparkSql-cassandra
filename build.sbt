import sbtassembly.AssemblyPlugin.autoImport.assembly
import scala.collection.Seq

ThisBuild / version           := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion      := "2.12.11"
ThisBuild / organization      := "com.fibanez.spark"
ThisBuild / organizationName  := "Fibanez"
ThisBuild / scalacOptions     ++= Seq("-target:jvm-1.8")
ThisBuild / licenses          := List("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt") )

lazy val assemblySettings = Seq(
  assembly / test := {},
  assembly / assemblyMergeStrategy := {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case PathList("META-INF", xs @ _*) => MergeStrategy.last
    case PathList("arrow-git.properties") => MergeStrategy.discard
    case PathList("org", "apache", "spark", "unused", "UnusedStubClass.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "client", "ClientBuilder.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "client", "FactoryFinder.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "core", "Response$ResponseBuilder.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "core", "Response$Status$Family.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "core", "Response$Status.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "core", "Response$StatusType.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "core", "Response.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "ext", "FactoryFinder.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "ext", "MessageBodyWriter.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "ext", "RuntimeDelegate.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "sse", "FactoryFinder.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "sse", "SseEventSource$Builder.class") => MergeStrategy.first
    case PathList("javax", "ws", "rs", "sse", "SseEventSource.class") => MergeStrategy.first
    case PathList("module-info.class") => MergeStrategy.first
    case x =>
      val oldStrategy = (assembly / assemblyMergeStrategy).value
      oldStrategy(x)
  }
)

val sparkDependencyProvided = true

lazy val connection = (project in file("connection"))
  .settings(assemblySettings)
  .settings(
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    libraryDependencies ++= Dependencies.Datastax.dependencies
      ++ Dependencies.Spark.dependenciesIncluded
      ++ Dependencies.Common.dependencies
  )

lazy val job = (project in file("job"))
  .settings(assemblySettings)
  .settings(
    javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
    libraryDependencies ++= Dependencies.Datastax.dependencies 
      ++ Dependencies.Spark.dependenciesProvided
      ++ Dependencies.Common.dependencies
  )

lazy val root = (project in file("."))
  .disablePlugins(AssemblyPlugin)
  .aggregate(connection, job)
  .settings(
    name := "scala-sparkSql-cassandra",
    resolvers += "Typesafe Releases" at "https://repo.typesafe.com/typesafe/releases/"
  )
