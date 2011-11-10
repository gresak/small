name := "kataster"

version := "0.1.0"

organization := "net.gresak"

scalaVersion := "2.9.0-1"

libraryDependencies ++= Seq(
"org.scalaquery" %% "scalaquery" % "0.9.5",
"org.apache.pdfbox" % "pdfbox-app" % "1.6.0",
"mysql" % "mysql-connector-java" % "5.1.18",
"net.liftweb" % "lift-json_2.9.0-1" % "2.4-M3" withSources(),
"net.databinder" %% "dispatch-core" % "0.8.5" withSources(),
"net.databinder" %% "dispatch-oauth" % "0.8.5" withSources(),
"net.databinder" %% "dispatch-lift-json" % "0.8.5" withSources(),
"net.databinder" %% "dispatch-nio" % "0.8.5" withSources(),
"net.databinder" %% "dispatch-http" % "0.8.5" withSources(),
"org.scalatest" %% "scalatest" % "1.6.1" % "test"
)