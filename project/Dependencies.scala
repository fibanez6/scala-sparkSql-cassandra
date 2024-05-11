import sbt.*

object Dependencies {
  import Versions.*
  object Spark {
    val dependencies = Seq(
      "com.datastax.spark" % "spark-cassandra-connector_2.12" % ApacheSpark,
      "org.apache.spark"  %% "spark-core"                     % ApacheSpark,
      "org.apache.spark"  %% "spark-streaming"                % ApacheSpark,
      "org.apache.spark"  %% "spark-sql"                      % ApacheSpark,
    )
  }

  object TestCommon {
    val mockito                 = "org.mockito"       % "mockito-all"                   % Mockito
    val junit                   = "junit"             % "junit"                         % JUnit
    val junitInterface          = "com.novocode"      % "junit-interface"               % JUnitInterface
    val scalaTest               = "org.scalatest"    %% "scalatest"                     % ScalaTest
    val driverMapperProcessor   = "com.datastax.oss"  % "java-driver-mapper-processor"  % DataStaxJavaDriver
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