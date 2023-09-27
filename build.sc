import mill._, scalalib._, publish._

object str8ur extends ScalaModule with PublishModule {
  def scalaVersion = "3.3.1"
  def publishVersion = "0.0.1"
  def scalacOptions = Seq("-deprecation")

  def pomSettings = PomSettings(
    description = "Str8ur minimal web application framework",
    organization = "net.garethrogers",
    url = "https://github.com/gazrogers/str8ur",
    licenses = Seq(License.MIT),
    versionControl = VersionControl.github("gazrogers", "str8ur"),
    developers = Seq(
      Developer("gazrogers", "Gareth Rogers", "https://github.com/gazrogers")
    )
  )

  def ivyDeps = Agg(
    ivy"io.netty:netty-all:4.1.93.Final",
    ivy"org.reflections:reflections:0.10.2"
  )

  object test extends ScalaTests with TestModule.ScalaTest {
    def sources = T.sources(millSourcePath)
    def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.2.17",
    )
  }
}
