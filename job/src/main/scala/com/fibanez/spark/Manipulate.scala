package com.fibanez.spark

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object Manipulate {

  def main(args: Array[String]){

    val spark = SparkSession
      .builder()
      .master("local[*]")
      .config("spark.cassandra.connection.host", "localhost")
      .getOrCreate()

    val projectPath = new java.io.File(".").getCanonicalPath
    val csv_df = spark.read.format("csv").option("header", "true").load(s"${projectPath}/previous_employees_by_title.csv")

    val newDF = csv_df
      .select("job_title", "employee_id", "employee_name", "first_day", "last_day")
      .withColumn("days_worked", abs(datediff(col("first_day").cast("date"), col("last_day").cast("date"))))

    newDF.show()
    spark.stop()
  }
}
