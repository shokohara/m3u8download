name := "mudl"

organization := "com.github.shokohara"

version := "1.0.0"

scalaOrganization := "org.scala-lang"

scalaVersion := "2.12.2"

val http4sVersion = "0.17.0-M3"

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

mainClass in assembly := Some("com.github.shokohara.mudl.Main")

// https://github.com/sbt/sbt-assembly/issues/246
assemblyOption in assembly := (assemblyOption in assembly).value.copy(
  prependShellScript = Some(
    Seq("#!/usr/bin/env sh", """exec java -jar -XX:+UseG1GC $MY_JAVA_OPTS "$0" "$@"""" + "\n")
  )
)

assemblyJarName in assembly := name.value
