package net.garethrogers.str8ur

import net.garethrogers.str8ur.servers.NettyHttpServer

trait Str8urApp:
  val server = NettyHttpServer()

  def handleRequest(request: HttpRequest): HttpResponse

  def startServer(config: Config = Config()): Unit =
    server.start(config.port, this)
