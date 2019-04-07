import Dependencies._
import sbt.addCompilerPlugin

ThisBuild / organization := "fp.testfield"
ThisBuild / version      := "1.0.0"
ThisBuild / scalaVersion := "2.12.8"
ThisBuild / scalacOptions ++= Seq(
  "-feature",
  "-language:higherKinds",
  "-Ypartial-unification"
)
ThisBuild / updateOptions := updateOptions.value.withLatestSnapshots(true)

val workshop = project
  .settings(
    libraryDependencies ++= {
      Http4s ++ CatsMtl ++ Logback ++ ScalaTest
    },
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.+")
  )

val kafka = project
  .settings(
    resolvers += "Ovotech" at "https://dl.bintray.com/ovotech/maven",
    libraryDependencies ++= {
      KafkaMonix ++ Logback ++ Fs2OvoTech
    }
  )

val cats = project
  .settings(
    libraryDependencies ++= Cats
  )

val cats_effect = (project in file("cats-effect"))
  .settings(
    libraryDependencies ++= CatsEffect ++ CatsMtl,
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.+")
  )

val cats_mtl = (project in file("cats-mtl"))
  .settings(
    libraryDependencies ++= CatsEffect ++ CatsMtl
  )

val http4s = project
  .settings(
    libraryDependencies ++= {
      CatsEffect ++ Http4s ++ Slick ++ Logback
    }
  )

val fs2 = project
  .settings(
    libraryDependencies ++= {
      Fs2
    },
    addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.+"),
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4")
  )

lazy val scalaz = project
  .settings(
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.+"),
    libraryDependencies ++= {
      ScalaZ ++ ZIO
    }
  )
