# Apache Spark Jobs for Cassandra Data Operations

## Quick Links

| What                                     | Where                                                                                     |
|------------------------------------------|-------------------------------------------------------------------------------------------|
| Anant/example-cassandra-spark-job-scala  | [Github](https://github.com/Anant/example-cassandra-spark-job-scala/blob/main/README.md)  |

## Requirements

* Cassandra Database 4.1.x or higher
    * Use the [Cassandra docker-compose](https://github.com/fibanez6/docker-compose/tree/main/cassandra)
* sbt
* Apache Spark [3.0.x](https://spark.apache.org/downloads.html)

## Features
| Dependency          | Artifact                       | Version |
|---------------------|--------------------------------|---------|
| Scala               |                                | 2.12.11 |
| com.datastax.spark  | spark-cassandra-connector_2.12 | 3.5.0   |
| org.apache.spark    | spark-core                     | 3.5.0   |
| org.apache.spark    | spark-streaming                | 3.5.0   |
| org.apache.spark    | spark-sql                      | 3.5.0   |

## Steps

### **1. - Create `demo` keyspace**
```bash
CREATE KEYSPACE demo WITH REPLICATION={'class': 'SimpleStrategy', 'replication_factor': 1};
```

### **2. Read Spark Job**
In this job, we will look at a CSV with 100,000 records and load it into a dataframe. Once read, we will display the first 20 rows.
```bash
./bin/spark-submit --class sparkCassandra.Read \
--master <master-url> \
--files /path/to/example-cassandra-spark-job-scala/previous_employees_by_title.csv \
/path/to/example-cassandra-spark-job-scala/target/scala-2.12/example-cassandra-spark-job-scala-assembly-0.1.0-SNAPSHOT.jar
```

### **3. Manipulate Spark Job**
In this job, we will do the same read; however, we will now take the `first_day` and `last_day` columns and calculate the absolute value difference in days worked. Again, then display the top 20 rows.

```bash
./bin/spark-submit --class sparkCassandra.Manipulate \
--master <master-url> \
--files /path/to/example-cassandra-spark-job-scala/previous_employees_by_title.csv \
/path/to/example-cassandra-spark-job-scala/target/scala-2.12/example-cassandra-spark-job-scala-assembly-0.1.0-SNAPSHOT.jar
```

### **4. Write to Cassandra Spark Job**
In this job, we will do the same thing we did in the manipulate job; however, we will now write the outputted dataframe to Cassandra instead of just displaying it to the console.
```bash
./bin/spark-submit --class sparkCassandra.Write \
--master <master-url> \
--conf spark.cassandra.connection.host=127.0.0.1 \
--conf spark.cassandra.connection.port=9042 \
--conf spark.sql.extensions=com.datastax.spark.connector.CassandraSparkExtensions \
--files /path/to/example-cassandra-spark-job-scala/previous_employees_by_title.csv \
/path/to/example-cassandra-spark-job-scala/target/scala-2.12/example-cassandra-spark-job-scala-assembly-0.1.0-SNAPSHOT.jar
```

### **5. SparkSQL Spark Job**
In this job, we will write the CSV data into one Cassandra table and then pick it up using SparkSQL and transform it at the same time. We will then write the newly transformed data into a new Cassandra table.
```bash
./bin/spark-submit --class sparkCassandra.ETL \
--master <master-url> \
--conf spark.cassandra.connection.host=127.0.0.1 \
--conf spark.cassandra.connection.port=9042 \
--conf spark.sql.extensions=com.datastax.spark.connector.CassandraSparkExtensions \
--files /path/to/example-cassandra-spark-job-scala/previous_employees_by_title.csv \
/path/to/example-cassandra-spark-job-scala/target/scala-2.12/example-cassandra-spark-job-scala-assembly-0.1.0-SNAPSHOT.jar
```