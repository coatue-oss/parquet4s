import bloop.integrations.sbt.BloopDefaults

import com.amazonaws.auth.{AWSCredentialsProviderChain, DefaultAWSCredentialsProviderChain}
import com.amazonaws.auth.profile.ProfileCredentialsProvider

lazy val resolvers =  Seq(
  Opts.resolver.sonatypeReleases,
  Resolver.jcenterRepo
)

lazy val supportedScalaVersions = Seq("2.11.12", "2.12.8")

lazy val commonSettings = Seq(
  Keys.organization := "com.github.mjakubowski84",
  Keys.version := "0.5.0-COATUE-SNAPSHOT",
  Keys.isSnapshot := true,
  Keys.scalaVersion := "2.11.12",
  Keys.scalacOptions ++= Seq("-deprecation", "-target:jvm-1.8"),
  Keys.javacOptions ++= Seq("-source", "1.8", "-target", "1.8","-unchecked",  "-deprecation", "-feature"),
  Keys.resolvers := resolvers
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishTo := Some("Coatue OSS Snapshots" at "s3://coatue-datascience-scratch/repo/snapshots/"),  // TODO: switch to real repo
  s3CredentialsProvider := { (bucket: String) =>
    new AWSCredentialsProviderChain(
      new ProfileCredentialsProvider("default"),
      DefaultAWSCredentialsProviderChain.getInstance()
    )
  }
)

lazy val itSettings = Defaults.itSettings ++ Project.inConfig(IntegrationTest)(Seq(
  Keys.fork := true,
  Keys.parallelExecution := true
)) ++ Project.inConfig(IntegrationTest)(BloopDefaults.configSettings)

lazy val core = (project in file("core"))
  .configs(IntegrationTest)
  .settings(
    Keys.name := "parquet4s-core",
    Keys.crossScalaVersions := supportedScalaVersions
  )
  .settings(commonSettings)
  .settings(itSettings)
  .settings(publishSettings)

lazy val akka = (project in file("akka"))
  .configs(IntegrationTest)
  .settings(
    Keys.name := "parquet4s-akka",
    Keys.crossScalaVersions := supportedScalaVersions
  )
  .settings(commonSettings)
  .settings(itSettings)
  .settings(publishSettings)
  .dependsOn(core % "compile->compile;it->it")

lazy val root = (project in file("."))
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(
    crossScalaVersions := Nil,
    publish / skip := true,
    publishLocal / skip := true
  )
  .aggregate(core, akka)
