import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "WHAT"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
		"mysql" % "mysql-connector-java" % "5.1.18",
		"junit" % "junit" % "4.10",
		"org.json" % "json" % "20090211",
		"org.apache.commons" % "commons-lang3" % "3.1"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here     
    )
}
