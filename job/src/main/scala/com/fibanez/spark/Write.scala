package com.fibanez.spark

//import com.datastax.spark.connector._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.cassandra._
import org.apache.spark.sql.functions._

object Write {

  def main(args: Array[String]){

    val spark = SparkSession
      .builder()
//      .master("local[*]") // Provided by CLI
      .config("spark.cassandra.connection.host", "localhost")
      .config("spark.sql.warehouse.dir", "")
      .config("spark.sql.extensions", "com.datastax.spark.connector.CassandraSparkExtensions")
      .config("spark.sql.catalog.history", "com.datastax.spark.connector.datasource.CassandraCatalog")
      .getOrCreate()
    
    spark.sql("CREATE DATABASE IF NOT EXISTS history.demo WITH DBPROPERTIES (class='SimpleStrategy',replication_factor='2')")
    spark.sql("CREATE TABLE IF NOT EXISTS history.demo.test (job_title String, employee_id String, days_worked int, employee_name String) USING cassandra PARTITIONED BY (job_title, employee_id)")

    // Read CSV
    val projectPath = new java.io.File(".").getCanonicalPath
    val csv_df = spark.read.format("csv").option("header", "true").load(s"${projectPath}/previous_employees_by_title.csv")
    
    val calcDF = csv_df
      .select("job_title", "employee_id", "employee_name", "first_day", "last_day")
      .withColumn("days_worked", abs(datediff(col("first_day").cast("date"), col("last_day").cast("date"))))

    val finalDF = calcDF.select("job_title", "employee_id", "employee_name", "days_worked")
//    finalDF.createCassandraTable("demo", "test", partitionKeyColumns = Some(Seq("job_title")), clusteringKeyColumns = Some(Seq("employee_id")))
    finalDF.write.cassandraFormat("test", "demo").mode("append").save()
    
    spark.stop()
  }
}