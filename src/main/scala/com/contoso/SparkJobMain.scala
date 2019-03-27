package com.contoso

import org.apache.spark.sql.SparkSession

object SparkJobMain {

  def main(args: Array[String]): Unit = {
    //Use existing spark session if it already exists, like in Databricks
    val is_local = SparkSession.getActiveSession.isEmpty
    val spark: SparkSession = SparkSession.getActiveSession.getOrElse({
      SparkSession
        .builder()
        .master("local[*]")
        .appName("mysparkapp")
        .getOrCreate()
    })

    val myDF = spark.range(1000)
    println(myDF.count)

    // If we're running locally we want to shut down spark gracefully
    if(is_local){
      spark.stop()
    }
  }
}
