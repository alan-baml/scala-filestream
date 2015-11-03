
scalaVersion := "2.11.4"


javaHome := Some(file("/efs/dist/oracle/jdk/1.8.0_66-build001/exec"))

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.akka" %% "akka-remote" % "2.4.0"
)

