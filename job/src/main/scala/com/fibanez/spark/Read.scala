package com.fibanez.spark

import org.apache.spark.sql.SparkSession

object Read {

  def main(args: Array[String]){

    val spark = SparkSession
      .builder()
      .master("local[*]")
      .config("spark.cassandra.connection.host", "localhost")
      .getOrCreate()

    val projectPath = new java.io.File(".").getCanonicalPath
    val csv_df = spark.read.format("csv").option("header", "true").load(s"${projectPath}/previous_employees_by_title.csv")

    csv_df.show()
    spark.stop()
  }
}