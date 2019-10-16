import java.io.IOException

import MyApp.Logger.NopLogger
import zio._
import zio.console.Console

object MyApp extends App {

  type MyEnv = Logger

  override def run(args: List[String]): ZIO[Console, Nothing, Int] = {

    val zio = if (args.contains("-q")) {
      logHello.provide(NopLogger)
    }
    else {
      logHello.provideSome[Console] { env =>
        new Logger.ConsoleLogger {
          override val console: Console = env
        }
      }
    }

    zio.fold(_ => 1, _ => 0)
  }

  def logHello: ZIO[MyEnv, IOException, Unit] = {
    import MyApp.Logger.logger

    for {
      _ <- logger().info("hello")
    } yield ()
  }


  trait Logger extends Serializable {
    val logger: Logger.Service[Any]
  }

  object Logger extends Serializable {
    trait Service[R] {
      def info(s: String): ZIO[R, Nothing, Unit]
    }

    trait ConsoleLogger extends Logger {
      val console: Console

      final val logger = new Service[Any] {
        override def info(s : String): ZIO[Any, Nothing, Unit] = {
          console.console.putStrLn(s"[info] $s")
        }
      }
    }

    object NopLogger extends Logger {
      final val logger = new Service[Any] {
        override def info(s : String): ZIO[Any, Nothing, Unit] = ZIO.unit
      }
    }

    case class logger() extends Logger.Service[Logger] {
      override def info(s: String): ZIO[Logger, Nothing, Unit] = ZIO.accessM[Logger](_.logger.info(s))
    }

  }

}
