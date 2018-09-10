import sbt._

object Dependencies {

  val CatsEffect = Seq(
    "org.typelevel" %% "cats-effect" % "1.0.0"
  )

  val CatsMtl = Seq(
    "org.typelevel" %% "cats-mtl-core" % "0.3.1-SNAPSHOT",
    "com.olegpy"    %% "meow-mtl"      % "0.1.2"
  )

  val Http4s = {
    val Http4sVersion = "0.19.0-M2"
    val CirceVersion  = "0.9.3"

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
}
