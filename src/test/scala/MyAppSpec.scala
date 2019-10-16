import MyApp.Logger
import zio.console._
import zio.test._
import zio.test.Assertion._
import zio.test.environment._


object MyAppSpec extends DefaultRunnableSpec (
  suite("MyAppSpec") {
    testM("logHello logs to console") {
      val logHello = MyApp.logHello.provideSome[Console] { env =>
        new Logger.ConsoleLogger {
          override val console: Console = env
        }
      }

      for {
        _ <- logHello
        output <- TestConsole.output
      } yield assert(output, equalTo(Vector("[info] hello\n")))
    }
  }
)
