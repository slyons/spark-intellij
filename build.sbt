name := "spark-intellij"
version := "0.1"
organization := "com.contoso"

mainClass in (Compile, run) := Some("com.contoso.SparkJobMain")

This / packageName := "sparkjobmain"

scalaVersion := "2.13.5"
val javaVersion = "1.8"
// This is the same as above, but different because of Docker tagging
val dockerJavaVersion = "8"

Compile / scalacOptions ++= Seq(
  s"-target:$javaVersion",
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xlog-reflective-calls",
  "-Xlint",
  "-Wconf:cat=other-match-analysis:error")
Compile / javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

Test / parallelExecution := false
Test / testOptions += Tests.Argument("-oDF")
Test / logBuffered := false

run / fork := false
Global / cancelable := true

// SPARK VERSION
// Adjust these to match your deployment environment
val sparkVersion = "3.2.0"
val scalatestVersion = "3.1.2"
val deltaVersion = "1.1.0"
val typesafeVersion = "1.4.1"

libraryDependencies ++= Seq(
  "com.typesafe" % "config" % typesafeVersion % "provided",
  "io.delta" %% "delta-core" % deltaVersion % "provided",
  // Shim library for Databricks dbutils
  "com.databricks" % "dbutils-api_2.12" % "0.0.5" % "compile",
  "org.apache.spark" %% "spark-sql" % sparkVersion % "provided",
  "org.scalatest" %% "scalatest" % scalatestVersion % Test,
)

// EXTRA LIBRARIES
// Add your extraneous libraries here
libraryDependencies ++= Seq(

)

Compile / run := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

This / assemblyShadeRules := Seq(
  ShadeRule.rename("com.google.common.**" -> "my_conf.@1").inAll
)

// See here for all of the possible configuration options:
// https://www.scala-sbt.org/sbt-native-packager/formats/docker.html
enablePlugins(JavaAppPackaging, DockerPlugin)
dockerBaseImage := s"docker.io/library/adoptopenjdk:$dockerJavaVersion-jre-hotspot"
dockerExposedPorts := Seq(4040)
dockerUsername := sys.props.get("docker.username")
dockerRepository := sys.props.get("docker.registry")

ThisBuild / dynverSeparator := "-"

