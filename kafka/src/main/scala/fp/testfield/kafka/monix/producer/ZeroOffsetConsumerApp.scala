package fp.testfield.kafka.monix.producer
import monix.eval.Task
import monix.execution.Scheduler
import monix.kafka.config.AutoOffsetReset
import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.concurrent.blocking
import scala.concurrent.duration._

object ZeroOffsetConsumerApp extends App {

  import monix.kafka._

  private implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  val consumerCfg = KafkaConsumerConfig.default.copy(
    bootstrapServers = List("localhost:9092"),
    groupId          = "kafka-tests-2",
    autoOffsetReset  = AutoOffsetReset.Earliest
  )

  def createZeroOffsetConsumer[K, V](config: KafkaConsumerConfig, topics: List[String])(
    implicit K: Deserializer[K],
    V: Deserializer[V]
  ): Task[KafkaConsumer[K, V]] = {
    import collection.JavaConverters._

    Task {
      val props = config.toProperties
      blocking {
        val props    = config.toProperties
        val consumer = new KafkaConsumer[K, V](props, K.create(), V.create())
        consumer.subscribe(topics.asJava)
        consumer.poll(0)
        consumer.seekToBeginning(consumer.assignment)
        consumer
      }
    }
  }

  val result = KafkaConsumerObservable[String, String](
    consumerCfg,
    createZeroOffsetConsumer[String, String](consumerCfg, List("test.topic"))
  ).map(_.value())
    .take(6)
    .toListL
    .runSyncUnsafe(10.second)

  println(result)
}
