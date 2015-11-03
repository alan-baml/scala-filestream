

resourceDirectory in Compile := baseDirectory.value / "scala/protocol"

unmanagedSourceDirectories in Compile += baseDirectory.value / "scala/protocol"

unmanagedSourceDirectories in Compile += baseDirectory.value / "scala/client"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.0",
  "com.typesafe.akka" %% "akka-remote" % "2.4.0"
)

