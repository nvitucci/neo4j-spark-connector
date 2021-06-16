package org.neo4j.spark.stream

import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.connector.read.{InputPartition, PartitionReader, PartitionReaderFactory}
import org.apache.spark.sql.sources.Filter
import org.apache.spark.sql.types.StructType
import org.neo4j.spark.reader.{Neo4jPartition, Neo4jPartitionReader}
import org.neo4j.spark.service.PartitionSkipLimit
import org.neo4j.spark.util.Neo4jOptions

case class Neo4jStreamingPartition(partitionSkipLimit: PartitionSkipLimit, filters: Array[Filter])
  extends InputPartition

class SimpleStreamingPartitionReaderFactory(private val neo4jOptions: Neo4jOptions,
                                            private val schema: StructType,
                                            private val jobId: String,
                                            private val scriptResult: java.util.List[java.util.Map[String, AnyRef]]) extends PartitionReaderFactory {
  override def createReader(partition: InputPartition): PartitionReader[InternalRow] = new Neo4jStreamingPartitionReader(
    neo4jOptions,
    partition.asInstanceOf[Neo4jStreamingPartition].filters,
    schema,
    jobId,
    partition.asInstanceOf[Neo4jStreamingPartition].partitionSkipLimit,
    scriptResult,
    new StructType()
  )
}
