organization := "scalatohoku.slim3"

name := "Slim3ModelMetaGeneratorPlugin"

version := "0.3.0"

crossScalaVersions := Seq("2.9.2", "2.10.4")

scalaVersion := "2.10.4"

libraryDependencies <+= scalaVersion( "org.scala-lang" % "scala-compiler" % _ )

publishTo := Some(Resolver.file("scalatohoku/slim3",file("/Users/pomu0325/dev/repo"))(Patterns(true, Resolver.mavenStyleBasePattern)))
