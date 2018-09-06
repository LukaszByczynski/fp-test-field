import Dependencies._

ThisBuild / organization := "fp.testfield"
ThisBuild / version := "1.0.0"
ThisBuild / scalaVersion := "2.12.6"
ThisBuild / scalacOptions ++= Seq(
  "-feature",
  "-language:higherKinds",
  "-Ypartial-unification"
)

lazy val workshop = project
  .settings(
    libraryDependencies ++= {
      CatsEffectRC2 ++ Http4s ++ CatsMtl ++ Logback ++ ScalaTest
    },
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7")
  )

lazy val benchmark = project
  .enablePlugins(JmhPlugin)
  .settings(
    libraryDependencies ++= {
      CatsEffect ++ CatsMtl ++  ScalaTest
    },
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7")
  )

lazy val root = (project in file("."))
  .aggregate(benchmark, workshop)
