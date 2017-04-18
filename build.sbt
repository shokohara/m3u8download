name := "pagination-google"
organization := "com.github.shokohara"

version := "1.0.4"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % "2.3.2",
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false
