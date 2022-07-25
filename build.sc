import mill._
import scalalib._

object str8ur extends ScalaModule {
  def scalaVersion = "3.1.0"
  def ivyDeps = Agg(
    ivy"io.netty:netty-all:4.1.72.Final",
    ivy"org.reflections:reflections:0.10.2"
  )

  object test extends Tests with TestModule.ScalaTest {
    def ivyDeps = Agg(
      ivy"org.scalatest::scalatest:3.2.10",
    )
  }
}

object examples extends ScalaModule {
  def scalaVersion = "3.1.0"
  
  def moduleDeps = Seq(str8ur)
}