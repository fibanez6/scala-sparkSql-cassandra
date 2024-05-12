import sbt.{ModuleID, _}

object Dependencies {
  import Versions.*
  
  implicit class Exclude(module: ModuleID) {
    def logbackExclude(): ModuleID = module
      .exclude("ch.qos.logback", "logback-classic")
      .exclude("ch.qos.logback", "logback-core")
      .exclude("org.slf4j", "jcl-over-slf4j")
      .exclude("org.slf4j", "log4j-over-slf4j")
    
    def googleExclude(): ModuleID = module
      .exclude("com.google.protobuf", "protobuf-java")
  }

  object Spark {
    val dependencies = Seq(
      "com.datastax.spark" % "spark-cassandra-connector_2.12" % ApacheSpark,
      "org.apache.spark"  %% "spark-core"                     % ApacheSpark,
      "org.apache.spark"  %% "spark-streaming"                % ApacheSpark,
      "org.apache.spark"  %% "spark-sql"                      % ApacheSpark,
    ).map(_.logbackExclude().googleExclude())
  }

//  object Connection {
//    val dependencies = Seq(
//      "log4j"                     % "log4j"         % Log4j,
//      "joda-time"                 % "joda-time"     % JodaTime,
//      "com.github.jnr"            % "jnr-posix"     % JnrPosix
//    )
//  }
    
  object TestCommon {
    val mockito         = "org.mockito"       % "mockito-all"      % Mockito
    val junit           = "junit"             % "junit"            % JUnit
    val junitInterface  = "com.novocode"      % "junit-interface"  % JUnitInterface
    val scalaTest       = "org.scalatest"    %% "scalatest"        % ScalaTest
  }
  
  object TestConnection {
    val dependencies = Seq(
      TestCommon.scalaTest % "test"
    )
  }

  object TestJob {
    val dependencies = Seq(
      TestCommon.scalaTest % "test"
    )
  }

}