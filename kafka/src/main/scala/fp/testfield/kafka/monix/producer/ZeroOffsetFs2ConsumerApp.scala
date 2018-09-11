package fp.testfield.kafka.monix.producer
import cats.effect.{ExitCode, IO, IOApp}
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.duration._

object ZeroOffsetFs2ConsumerApp extends IOApp {

  import com.ovoenergy.fs2.kafka._

  val settings = ConsumerSettings(
    pollTimeout    = 250.milliseconds,
    maxParallelism = 1,
    nativeSettings = Map(
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "false",
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> s"localhost:9092",
      ConsumerConfig.GROUP_ID_CONFIG -> "kafka-test-3",
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest"
    )
  )

  override def run(args: List[String]): IO[ExitCode] = {
    consume[IO](
      TopicSubscription(Set("test.topic")),
      new StringDeserializer,
      new StringDeserializer,
      settings
    ).take(6).map(_.value()).compile.toList.map { result =>
      println(result)
      ExitCode.Success
    }
  }
}
