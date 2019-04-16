libraryDependencies ++= {
  val parquetVersion = "1.10.0"
  val sparkVersion = "2.4.0"
  val hadoopVersion = "2.9.2"
  Seq(
    // fix up eviction warnings caused by more granular hadoop dep
    "commons-codec" % "commons-codec" % "1.10",
    "commons-collections" % "commons-collections" % "3.2.2",
    "commons-lang" % "commons-lang" % "2.6",
    "commons-logging" % "commons-logging" % "1.1.3",
    "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.13",
    "org.codehaus.jackson" % "jackson-core-asl" % "1.9.13",
    "org.slf4j" % "slf4j-api" % "1.7.25",
    "org.slf4j" % "slf4j-log4j12" % "1.7.25",
    "org.xerial.snappy" % "snappy-java" % "1.1.2.6",
    "org.apache.parquet" % "parquet-hadoop" % parquetVersion excludeAll (
      ExclusionRule("commons-codec", "commons-codec"),
      ExclusionRule("org.slf4j", "slf4j-api") // fix eviction where parquet-format depends on earlier version
    ),
    // more granular hadoop dep excludes dependencies whose versions will be set by parquet-hadoop
    "org.apache.hadoop" % "hadoop-common" % hadoopVersion excludeAll(
      ExclusionRule("commons-codec", "commons-codec"),
      ExclusionRule("commons-collections", "commons-collections"),
      ExclusionRule("commons-lang", "commons-lang"),
      ExclusionRule("commons-logging", "commons-logging"),
      ExclusionRule("com.google.guava", "guava"),
      ExclusionRule("log4j", "log4j"),
      ExclusionRule("net.java.dev.jets3t", "jets3t"),
      ExclusionRule("org.apache.httpcomponents", "httpclient"),
      ExclusionRule("org.apache.httpcomponents", "httpcore"),
      ExclusionRule("org.codehaus.jackson", "jackson-mapper-asl"),
      ExclusionRule("org.codehaus.jackson", "jackson-core-asl"),
      ExclusionRule("org.slf4j", "slf4j-api"),
      ExclusionRule("org.slf4j", "slf4j-log4j12"),
      ExclusionRule("org.xerial.snappy", "snappy-java")
    ),
    "com.chuusai" %% "shapeless" % "2.3.3",

    // tests
    "org.scalamock" %% "scalamock" % "4.1.0" % "test",
    "org.scalatest" %% "scalatest" % "3.0.5" % "test,it",
    "org.apache.hadoop" % "hadoop-client" % hadoopVersion % "test,it",
    "org.apache.spark" %% "spark-core" % sparkVersion % "it"
      exclude(org = "org.apache.hadoop", name = "hadoop-client"),
    "org.apache.spark" %% "spark-sql" % sparkVersion % "it"
      exclude(org = "org.apache.hadoop", name = "hadoop-client"),
    "ch.qos.logback" % "logback-classic" % "1.2.3" % "test,it"
  )
}
