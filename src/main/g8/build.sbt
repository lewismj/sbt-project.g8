lazy val commonScalacOptions = Seq(
  "-feature",
  "-deprecation",
  "-encoding", "utf8",
  "-language:postfixOps",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-unchecked",
  "-Xcheckinit",
  "-Xfuture",
  "-Xlint",
  "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-value-discard")

lazy val buildSettings = Seq(
  name := "$name$",
  organization in Global := "$organization$",
  scalaVersion in Global := "$scala_version$"
)

lazy val noPublishSettings = Seq(
    skip in publish := true
)

lazy val scoverageSettings = Seq(
  coverageMinimum := 75,
  coverageFailOnMinimum := false,
  coverageExcludedPackages := "instances"
)

lazy val commonSettings = Seq(
  scalacOptions ++= commonScalacOptions,
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats" % "0.9.0"
  ),
  fork in test := true
)

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false,
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

lazy val docsAPIDir = settingKey[String]("Name of subdirectory in site target directory for api docs.")

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
  includeFilter in makeSite := "*.html" | "*.css" | "*.png" | "*.jpg" | "*.gif" | "*.js" | "*.swf" | "*.yml" | "*.md",
  ghpagesNoJekyll := false,
  docsAPIDir := "api",
  addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc),docsAPIDir),
  unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject -- inProjects(tests)
)

lazy val docs = project
    .enablePlugins(MicrositesPlugin)
    .settings(moduleName := "$package$-docs")
    .settings(unidocSettings: _*)
    .settings(ghpages.settings)
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
    testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-laws" % "0.9.0",
      "org.scalatest"  %% "scalatest" % "3.0.0" % "test",
      "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
    )
  )

lazy val bench = project.in(file("bench"))
  .dependsOn(core)
  .dependsOn(tests  % "test->test")
  .settings(moduleName := "taniwha-bench")
  .settings(taniwhaSettings:_*)
  .settings(noPublishSettings:_*)
  .settings(
    coverageEnabled := false
  ).enablePlugins(JmhPlugin)
