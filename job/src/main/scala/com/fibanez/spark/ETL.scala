package com.fibanez.spark

import com.datastax.spark.connector._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.functions._

object ETL {

  def main(args: Array[String]){

    val spark = SparkSession
      .builder()
//      .master("local[*]") // Provided by CLI
      .config("spark.cassandra.connection.host", "localhost")
      .config("spark.sql.extensions", "com.datastax.spark.connector.CassandraSparkExtensions")
      .config("spark.sql.catalog.history", "com.datastax.spark.connector.datasource.CassandraCatalog")
      .getOrCreate()

    spark.sql("CREATE DATABASE IF NOT EXISTS history.demo WITH DBPROPERTIES (class='SimpleStrategy',replication_factor='2')")
    spark.sql("CREATE TABLE IF NOT EXISTS history.demo.pre (job_title String, employee_id String, employee_name String, first_day String, last_day String) USING cassandra PARTITIONED BY (job_title, employee_id)")
    spark.sql("CREATE TABLE IF NOT EXISTS history.demo.post (job_title String, employee_id String, days_worked int, employee_name String) USING cassandra PARTITIONED BY (job_title, employee_id)")
    
    val projectPath = new java.io.File(".").getCanonicalPath
    val csv_df = spark.read.format("csv").option("header", "true").load(s"${projectPath}/previous_employees_by_title.csv")

//    csv_df.createCassandraTable("demo", "pre", partitionKeyColumns = Some(Seq("job_title")), clusteringKeyColumns = Some(Seq("employee_id")))
    csv_df.write.cassandraFormat("pre", "demo").mode("append").save()

    spark.conf.set("spark.sql.catalog.cassandra", "com.datastax.spark.connector.datasource.CassandraCatalog")

    spark.sql("use cassandra.demo")

    val sqlDF = spark.sql("select job_title, employee_id, employee_name, abs(datediff(last_day, first_day)) as days_worked from pre")

//    sqlDF.createCassandraTable("demo", "post", partitionKeyColumns = Some(Seq("job_title")), clusteringKeyColumns = Some(Seq("employee_id")))
    sqlDF.write.cassandraFormat("post", "demo").mode("append").save()

    spark.stop()
  }
}
