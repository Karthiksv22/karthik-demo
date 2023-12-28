name := """java"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.10"

libraryDependencies += guice

libraryDependencies += "org.mongodb" % "mongodb-driver-sync" % "4.3.0"