import sbt._

object Dependencies {

  val CatsEffect = Seq(
    "org.typelevel" %% "cats-effect" % "1.0.0"
  )

  val CatsMtl = Seq(
    "org.typelevel" %% "cats-mtl-core" % "0.4.0",
    "com.olegpy"    %% "meow-mtl"      % "0.2.0"
  )

  val Http4s = {
    val Http4sVersion = "0.19.0"
    val CirceVersion  = "0.10.0"

    Seq(
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe"        % Http4sVersion,
      "org.http4s" %% "http4s-dsl"          % Http4sVersion,
      "io.circe"   %% "circe-generic"       % CirceVersion,
      "io.circe"   %% "circe-literal"       % CirceVersion
    )
  }

  val ScalaTest = Seq(
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )

  val Logback = Seq(
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )

  val KafkaMonix = Seq(
    "io.monix" %% "monix-kafka-1x" % "1.0.0-RC1"
  )

  val Fs2OvoTech = Seq(
    "com.ovoenergy" %% "fs2-kafka-client" % "0.2.5"
  )

  val Slick = {
    val SlickVersion = "3.2.3"

    Seq(
      "com.typesafe.slick"    %% "slick"                % SlickVersion,
      "com.typesafe.slick"    %% "slick-hikaricp"       % SlickVersion,
      "com.github.zainab-ali" %% "fs2-reactive-streams" % "0.8.0",
      "com.h2database"        % "h2"                    % "1.4.197"
    )
  }

  val Fs2 = {
    Seq("co.fs2" %% "fs2-core" % "1.0.0")
  }
}
