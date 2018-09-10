package fp.testfield.kafka.monix.producer
import monix.reactive.Observable
import org.apache.kafka.clients.producer.ProducerRecord

import scala.concurrent.duration.Duration

object ProducerApp extends App {

  import monix.execution.Scheduler
  import monix.kafka._

  private implicit val scheduler: Scheduler = monix.execution.Scheduler.global

  val producerCfg = KafkaProducerConfig.default.copy(
    bootstrapServers = List("localhost:9092")
  )
  val producer = KafkaProducerSink[String,String](producerCfg, scheduler)

  val result = Observable
    .fromIterable(List("a", "b", "c", "d", "e", "f"))
    .map(v => new ProducerRecord[String, String]("test.topic", v, v))
    .bufferIntrospective(1024)
    .consumeWith(producer)
    .runSyncUnsafe(Duration.Inf)
}
