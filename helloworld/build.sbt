organization in ThisBuild := "com.beone.workshop.lagom"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.7"

lazy val helloworldApi = project("helloworld-api")
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lagomJavadslJackson,
      lagomJavadslImmutables
    )
  )

lazy val helloworldImpl = project("helloworld-impl")
  .enablePlugins(LagomJava)
  .settings(
    version := "1.0-SNAPSHOT",
    libraryDependencies ++= Seq(
      lagomJavadslTestKit
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(helloworldApi)

lazy val organizeFrontend = project("helloworld-frontend")
  .enablePlugins(PlayJava, LagomPlay, SbtWeb)
  .settings(
    version := "1.0-SNAPSHOT",
    routesGenerator := InjectedRoutesGenerator,
    includeFilter in (Assets, LessKeys.less) := "*.less",
    excludeFilter in (Assets, LessKeys.less) := "_*.less",
    libraryDependencies ++= Seq(
      lagomJavadslClient,
      "org.webjars" % "bootstrap" % "3.3.5"
    )
  )
  .dependsOn(helloworldApi)

  def project(id: String) = Project(id, base = file(id))
  .settings(eclipseSettings: _*)
  .settings(javacOptions in compile ++= Seq("-encoding", "UTF-8", "-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-Xlint:deprecation"))
  .settings(jacksonParameterNamesJavacSettings: _*) // applying it to every project even if not strictly needed.


// See https://github.com/FasterXML/jackson-module-parameter-names
lazy val jacksonParameterNamesJavacSettings = Seq(
  javacOptions in compile += "-parameters"
)

// Configuration of sbteclipse
// Needed for importing the project into Eclipse
lazy val eclipseSettings = Seq(
  EclipseKeys.projectFlavor := EclipseProjectFlavor.Java,
  EclipseKeys.withBundledScalaContainers := false,
  EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource,
  EclipseKeys.eclipseOutput := Some(".target"),
  EclipseKeys.withSource := true,
  EclipseKeys.withJavadoc := true,
  // avoid some scala specific source directories
  unmanagedSourceDirectories in Compile := Seq((javaSource in Compile).value),
  unmanagedSourceDirectories in Test := Seq((javaSource in Test).value)
)
