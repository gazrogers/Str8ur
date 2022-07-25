package net.garethrogers.str8urexamples.serveronly

import scala.collection.immutable.Queue
import scala.collection.mutable.HashMap
import net.garethrogers.str8ur.Str8urApp
import net.garethrogers.str8ur.HttpRequest
import net.garethrogers.str8ur.HttpResponse

object ServerOnly extends Str8urApp:
  def handleRequest(request: HttpRequest): HttpResponse =
    HttpResponse(200, HashMap.empty[String, Queue[String]], "Hello, world!")
  
  // @main def main(args: String*) =
  def main(args: String*) =
    startServer()
