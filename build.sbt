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

lazy val workshop = project
  .settings(
    libraryDependencies ++= {
      Http4s ++ CatsMtl ++ Logback ++ ScalaTest
    },
    addCompilerPlugin("org.spire-math"          %% "kind-projector" % "0.9.9")
  )

lazy val benchmark = project
  .enablePlugins(JmhPlugin)
  .settings(
    libraryDependencies ++= {
      CatsEffect ++ CatsMtl ++ ScalaTest
    },
    addCompilerPlugin("org.spire-math"          %% "kind-projector"     % "0.9.9"),
    addCompilerPlugin("com.olegpy"              %% "better-monadic-for" % "0.2.4")
  )

lazy val kafka = project
  .settings(
    resolvers += "Ovotech" at "https://dl.bintray.com/ovotech/maven",
    libraryDependencies ++= {
      KafkaMonix ++ Logback ++ Fs2OvoTech
    }
  )

lazy val cats_effect = (project in file("cats-effect"))
  .settings(
    libraryDependencies ++= CatsEffect ++ CatsMtl,
    addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.9"),
  )

lazy val cats_mtl = (project in file("cats-mtl"))
  .settings(
    libraryDependencies ++= CatsEffect ++ CatsMtl
  )

lazy val http4s = project
  .settings(
    libraryDependencies ++= {
      CatsEffect ++ Http4s ++ Slick ++ Logback
    }
  )

lazy val fs2 = project
  .settings(
    libraryDependencies ++= {
      Fs2
    },
    addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.9"),
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4")
  )
