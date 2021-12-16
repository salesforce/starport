val awsSdkVersion = "1.11.943"
val slickVersion = "3.3.3"
val akkaVersion = "2.6.11"

val scalaTestArtifact      = "org.scalatest"          %% "scalatest"            % "3.2.3" % Test
val slickArtifact          = "com.typesafe.slick"     %% "slick"                % slickVersion
val slickHikaricpArtifact  = "com.typesafe.slick"     %% "slick-hikaricp"       % slickVersion
val scoptArtifact          = "com.github.scopt"       %% "scopt"                % "3.7.1"
val configArtifact         = "com.typesafe"           %  "config"               % "1.4.1"
val nscalaTimeArtifact     = "com.github.nscala-time" %% "nscala-time"          % "2.24.0"
val hyperionArtifact       = "com.krux"               %% "hyperion"             % "7.0.0-RC1"
val slf4jApiArtifact       = "org.slf4j"              %  "slf4j-api"            % "1.7.30"
val logbackClassicArtifact = "ch.qos.logback"         %  "logback-classic"      % "1.2.3"
val awsSdkS3               = "com.amazonaws"          %  "aws-java-sdk-s3"      % awsSdkVersion
val awsSdkSES              = "com.amazonaws"          %  "aws-java-sdk-ses"     % awsSdkVersion
val awsSdkSSM              = "com.amazonaws"          %  "aws-java-sdk-ssm"     % awsSdkVersion
val awsSdkSNS              = "com.amazonaws"          %  "aws-java-sdk-sns"     % awsSdkVersion
val awsSdkCloudWatch       = "com.amazonaws"          %  "aws-java-sdk-cloudwatch"     % awsSdkVersion
val metricsGraphite        = "io.dropwizard.metrics"  %  "metrics-graphite"     % "4.1.17"
val postgreSqlJdbc         = "org.postgresql"         %  "postgresql"           % "42.2.18"
val awsLambdaEvents        = "com.amazonaws"          %  "aws-lambda-java-events" % "3.7.0"
val awsLambdaCore          = "com.amazonaws"          %  "aws-lambda-java-core"   % "1.2.1"
val akkaActorArtifact      = "com.typesafe.akka"      %% "akka-actor-typed" % akkaVersion

lazy val commonSettings = Seq(
  scalacOptions ++= Seq("-deprecation", "-feature", "-Xlint", "-Xfatal-warnings"),
  scalaVersion := "2.12.13",
  libraryDependencies += scalaTestArtifact,
  organization := "com.krux",
  assembly / test := {},  // skip test during assembly
  assembly / assemblyMergeStrategy := {
    // scala 2.12.13 also introduces the nowarn.class in scala-compat
    case PathList(ps @ _*) if Set("nowarn$.class", "nowarn.class").contains(ps.last) =>
      MergeStrategy.first
    case x =>
      val oldStrategy = (assembly / assemblyMergeStrategy).value
      oldStrategy(x)
  },
  publishMavenStyle := true,
  headerLicense := Some(HeaderLicense.Custom(
    """|Copyright (c) 2021, salesforce.com, inc.
       |All rights reserved.
       |SPDX-License-Identifier: BSD-3-Clause
       |For full license text, see the LICENSE file in the repo root or https://opensource.org/licenses/BSD-3-Clause
       |""".stripMargin
  ))
)

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(name := "starport").
  aggregate(core,lambda)

lazy val core = (project in file("starport-core")).
  settings(commonSettings: _*).
  enablePlugins(BuildInfoPlugin).
  settings(
    name := "starport-core",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "com.krux.starport",
    assembly / assemblyJarName := "starport-core.jar",
    libraryDependencies ++= Seq(
      scalaTestArtifact,
      slickArtifact,
      slickHikaricpArtifact,
      scoptArtifact,
      nscalaTimeArtifact,
      configArtifact,
      slf4jApiArtifact,
      logbackClassicArtifact,
      hyperionArtifact,
      awsSdkS3,
      awsSdkSES,
      awsSdkSSM,
      awsSdkSNS,
      awsSdkCloudWatch,
      metricsGraphite,
      postgreSqlJdbc,
      akkaActorArtifact
    ),
    fork := true
  )

lazy val lambda = (project in file("starport-lambda")).
  settings(commonSettings: _*).
  settings(
    name := "starport-lambda",
    assembly / assemblyJarName := "starport-lambda.jar",
    libraryDependencies ++= Seq(
      awsLambdaCore,
      awsLambdaEvents,
    ),
    fork := true
  ).dependsOn(core)
