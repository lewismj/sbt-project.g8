import sbt.Credentials
import sbtrelease.ReleaseStateTransformations._

lazy val commonScalacOptions = Seq(
  "-feature",
  "-deprecation",
  "-encoding", "utf8",
  "-language:postfixOps",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xfatal-warnings")

lazy val buildSettings = Seq(
  name := "$name$",
  Global / organization := "$organization$",
  Global / scalaVersion := "$scala_version$"
)

lazy val noPublishSettings = Seq(
    publish / skip := true
)

lazy val scoverageSettings = Seq(
  coverageMinimumStmtTotal := 75,
  coverageFailOnMinimum := false,
  coverageExcludedPackages := "instances"
)

lazy val commonSettings = Seq(
  scalacOptions ++= commonScalacOptions,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.6.1"
  )
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  Test / publishArtifact := false,
  pomIncludeRepository := Function.const(false),
  sonatypeProfileName := "com.waioeka",
  publishTo := Some(
    if (isSnapshot.value) Opts.resolver.sonatypeSnapshots
    else Opts.resolver.sonatypeStaging
  ),
  autoAPIMappings := true,
  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/bsd-license.php")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/$gh_username$/$name$"),
      "scm:git:git@github.com:$gh_username$/$name$.git"
   )
 ),
 developers := List(
  Developer(id="$gh_username$", name="$name$", email="@$gh_username$", url=url("https://github.com/$gh_username$"))
 )
)


lazy val $name$Settings = buildSettings ++ commonSettings ++ scoverageSettings

lazy val $name$ = project.in(file("."))
  .settings(moduleName := "root")
  .settings(noPublishSettings:_*)
  .aggregate(docs, tests, core)

lazy val core = project.in(file("core"))
  .settings(moduleName := "$name$-core")
  .settings($name$Settings:_*)
  .settings(publishSettings:_*)

lazy val docsMappingsAPIDir = settingKey[String]("Name of subdirectory in site target directory for api docs.")

lazy val docSettings = Seq(
  autoAPIMappings := true,
  micrositeName := "$name$",
  micrositeDescription := "$microsite_description$",
  micrositeBaseUrl :="/$name$",
  micrositeDocumentationUrl := "/$name$/api",
  micrositeGithubOwner := "$gh_username$",
  micrositeGithubRepo := "$name$",
  micrositeHighlightTheme := "atom-one-light",
  micrositePalette := Map(
    "brand-primary" -> "#5B5988",
    "brand-secondary" -> "#292E53",
    "brand-tertiary" -> "#222749",
    "gray-dark" -> "#49494B",
    "gray" -> "#7B7B7E",
    "gray-light" -> "#E5E5E6",
    "gray-lighter" -> "#F4F3F4",
    "white-color" -> "#FFFFFF"),
  git.remoteRepo := "git@github.com:$gh_username$/$name$.git",
  ghpagesNoJekyll := false,
  ScalaUnidoc /unidoc / unidocProjectFilter := inAnyProject -- inProjects(tests),
  docsMappingsAPIDir := "api",
  addMappingsToSiteDir(ScalaUnidoc / packageDoc / mappings, docsMappingsAPIDir),
  ghpagesNoJekyll := false,
  mdoc / fork := true,
  ScalaUnidoc / unidoc / fork := true,
  makeSite / includeFilter := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md" | "*.svg",
  Jekyll / includeFilter := (makeSite / includeFilter).value,
  mdocIn := (LocalRootProject / baseDirectory).value / "docs" / "src" / "main" / "mdoc",
  mdocExtraArguments := Seq("--no-link-hygiene"),
  ScalaUnidoc / unidoc / scalacOptions ++= Seq(
    "-sourcepath",
    (LocalRootProject / baseDirectory).value.getAbsolutePath
  )
)

lazy val docs = project
    .enablePlugins(MicrositesPlugin)
    .enablePlugins(ScalaUnidocPlugin, GhpagesPlugin)
    .settings(moduleName := "$package$-docs")
    .dependsOn(core)
    .settings(docSettings:_*)
    .settings($name$Settings:_*)
    .settings(noPublishSettings:_*)

lazy val tests = project.in(file("tests"))
  .dependsOn(core)
  .settings(moduleName := "$name$-tests")
  .settings($name$Settings:_*)
  .settings(noPublishSettings:_*)
  .settings(
    coverageEnabled := false,
    Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
    libraryDependencies ++= Seq(
      "org.scalatest"  %% "scalatest" % "3.2.9" % "test",
      "org.scalacheck" %% "scalacheck" % "1.15.4" % "test"
    )
  )

lazy val bench = project.in(file("bench"))
  .dependsOn(core)
  .dependsOn(tests  % "test->test")
  .settings(moduleName := "$package$-bench")
  .settings($name$Settings:_*)
  .settings(noPublishSettings:_*)
  .settings(
    coverageEnabled := false
  ).enablePlugins(JmhPlugin)
