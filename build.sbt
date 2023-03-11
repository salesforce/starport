val awsSdkVersion = "1.12.425"
val slickVersion = "3.4.1"
// cannot update to 2.7 because of the new akka license
val akkaVersion = "2.6.20"

val scalaTestArtifact      = "org.scalatest"          %% "scalatest"            % "3.2.+" % Test
val slickArtifact          = "com.typesafe.slick"     %% "slick"                % slickVersion
val slickHikaricpArtifact  = "com.typesafe.slick"     %% "slick-hikaricp"       % slickVersion
val scoptArtifact          = "com.github.scopt"       %% "scopt"                % "4.1.0"
val configArtifact         = "com.typesafe"           %  "config"               % "1.4.2"
val hyperionArtifact       = "com.krux"               %% "hyperion"             % "7.0.0-RC8"
val slf4jApiArtifact       = "org.slf4j"              %  "slf4j-api"            % "2.0.6"
// cannot upgrade to 1.4.x because it runs on java 11 only
val logbackClassicArtifact = "ch.qos.logback"         %  "logback-classic"      % "1.3.5"
val awsSdkS3               = "com.amazonaws"          %  "aws-java-sdk-s3"      % awsSdkVersion
val awsSdkSES              = "com.amazonaws"          %  "aws-java-sdk-ses"     % awsSdkVersion
val awsSdkSSM              = "com.amazonaws"          %  "aws-java-sdk-ssm"     % awsSdkVersion
val awsSdkSNS              = "com.amazonaws"          %  "aws-java-sdk-sns"     % awsSdkVersion
val awsSdkCloudWatch       = "com.amazonaws"          %  "aws-java-sdk-cloudwatch"     % awsSdkVersion
val metricsGraphite        = "io.dropwizard.metrics"  %  "metrics-graphite"     % "4.2.17"
val postgreSqlJdbc         = "org.postgresql"         %  "postgresql"           % "42.5.4"
val awsLambdaEvents        = "com.amazonaws"          %  "aws-lambda-java-events" % "3.11.0"
val awsLambdaCore          = "com.amazonaws"          %  "aws-lambda-java-core"   % "1.2.2"
val akkaActorArtifact      = "com.typesafe.akka"      %% "akka-actor-typed" % akkaVersion
val parallelCollection     = "org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4"

lazy val commonSettings = Seq(
  scalacOptions ++= Seq("-deprecation", "-feature", "-Xlint"),
  scalaVersion := "2.13.10",
  libraryDependencies += scalaTestArtifact,
  organization := "com.krux",
  assembly / test := {},  // skip test during assembly
  assembly / assemblyMergeStrategy := {
    // scala 2.12.13 also introduces the nowarn.class in scala-compat
    case PathList(ps @ _*) if Set("nowarn$.class", "nowarn.class", "module-info.class").contains(ps.last) =>
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
  aggregate(core, lambda)

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
      akkaActorArtifact,
      parallelCollection
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
