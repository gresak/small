name := "kataster"

version := "0.1.0"

organization := "org.scalaquery"

scalaVersion := "2.9.0-1"

libraryDependencies ++= Seq(
"org.scalaquery" %% "scalaquery" % "0.9.5",
"org.apache.pdfbox" % "pdfbox-app" % "1.6.0",
"mysql" % "mysql-connector-java" % "5.1.18",
"org.scalatest" %% "scalatest" % "1.6.1" % "test"
)