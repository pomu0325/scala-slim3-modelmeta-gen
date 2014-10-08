organization := "scalatohoku"

name := "Slim3ModelTest"

version := "0.1"

scalaVersion := "2.9.2"

autoCompilerPlugins := true

addCompilerPlugin("scalatohoku.slim3" %% "slim3modelmetageneratorplugin" % "0.3.0")

libraryDependencies ++= Seq(
  "org.slim3" % "slim3" % "1.0.16"
)

resolvers ++= Seq(
  "seasar" at "https://www.seasar.org/maven/maven2"
)

