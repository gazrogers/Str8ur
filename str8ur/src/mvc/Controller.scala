package net.garethrogers.str8ur.mvc

import net.garethrogers.str8ur.routers.ControllerRouter
import net.garethrogers.str8ur.HttpResponse
import scala.collection.mutable.HashMap
import scala.collection.immutable.Queue

trait Controller:
  var response: HttpResponse = HttpResponse(200, HashMap[String, Queue[String]](), "")
