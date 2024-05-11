## A Scala 3 project with DataStax Connector for Apache Spark to Apache Cassandra

* From [spark-cassandra-connector](https://github.com/datastax/spark-cassandra-connector/tree/b3.4)

## Quick Links

| What                     | Where                                                                                                                                                                                                                                                                                                               |
|--------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Community                | Chat with us at [Apache Cassandra](https://cassandra.apache.org/_/community.html#discussions)                                                                                                                                                                                                                       |
| Scala Docs               | Most Recent Release (3.4.1): [Connector API docs](https://datastax.github.io/spark-cassandra-connector/ApiDocs/3.4.1/connector/com/datastax/spark/connector/index.html), [Connector Driver docs](https://datastax.github.io/spark-cassandra-connector/ApiDocs/3.4.1/driver/com/datastax/spark/connector/index.html)
| Medium Tutorial Integrate Spark SQL and Cassandra         | [Website](https://jentekllc8888.medium.com/tutorial-integrate-spark-sql-and-cassandra-complete-with-scala-and-python-example-codes-8307fe9c2901)                                                                                                                                                                    |
| DataStax Connector for Apache Spark to Apache Cassandra | [spark-cassandra-connector](https://github.com/datastax/spark-cassandra-connector/tree/b3.4)                                                                                                                                                                                                                        |

## Requirements

* Cassandra Database 4.1.4 or higher
  * Use the [Cassandra docker-compose](https://github.com/fibanez6/docker-compose/tree/main/cassandra)

## Usage

This is a normal sbt project. You can compile code with `sbt compile`, run it with `sbt run`, and `sbt console` will start a Scala 3 REPL.

## Sample of use

The root package of the DataStax Connector for Apache Spark to Apache Cassandra. Offers handy implicit conversions that add Cassandra-specific methods to SparkContext and RDD.

Call cassandraTable method on the SparkContext object to create a CassandraRDD exposing Cassandra tables as Spark RDDs.

Call RDDFunctions saveToCassandra function on any RDD to save distributed collection to a Cassandra table.

Example:
```cassandraql
CREATE KEYSPACE test WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1 };
CREATE TABLE test.words (word text PRIMARY KEY, count int);
INSERT INTO test.words(word, count) VALUES ("and", 50);
```
```scala
import com.datastax.spark.connector._

val sparkMasterHost = "127.0.0.1"
val cassandraHost = "127.0.0.1"
val keyspace = "test"
val table = "words"

// Tell Spark the address of one Cassandra node:
val conf = new SparkConf(true).set("spark.cassandra.connection.host", cassandraHost)

// Connect to the Spark cluster:
val sc = new SparkContext("spark://" + sparkMasterHost + ":7077", "example", conf)

// Read the table and print its contents:
val rdd = sc.cassandraTable(keyspace, table)
rdd.toArray().foreach(println)

// Write two rows to the table:
val col = sc.parallelize(Seq(("of", 1200), ("the", "863")))
col.saveToCassandra(keyspace, table)

sc.stop()
```

