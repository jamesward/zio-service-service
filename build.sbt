scalaVersion := "2.13.1"

val zioVersion = "1.0.0-RC15"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio"          % zioVersion,
  "dev.zio" %% "zio-test"     % zioVersion % "test",
  "dev.zio" %% "zio-test-sbt" % zioVersion % "test",
)

testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
