name := "spark-intellij"

version := "0.1"

scalaVersion := "2.11.12"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "2.4.0" % "provided",

  // Shim library for Databricks dbutils
  "com.databricks" % "dbutils-api_2.11" % "0.0.3" % "compile"

)

fork in Test := true

assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

run in Compile := Defaults.runTask(fullClasspath in Compile, mainClass in (Compile, run), runner in (Compile, run)).evaluated

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

assemblyShadeRules in assembly := Seq(
  ShadeRule.rename("com.google.common.**" -> "my_conf.@1").inAll
)


scalacOptions in (Compile, doc) += "-groups"