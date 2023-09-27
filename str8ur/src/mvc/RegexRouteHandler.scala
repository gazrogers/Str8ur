package net.garethrogers.str8ur.mvc

import net.garethrogers.str8ur.routers.RequestHandler
import net.garethrogers.str8ur.HttpRequest
import net.garethrogers.str8ur.HttpResponse
import scala.collection.mutable.HashMap
import scala.collection.immutable.Queue

class RegexRouteHandler(handlerFunction: RequestHandler) extends Handler:
  var response: HttpResponse = HttpResponse.ok
  def handle(request: HttpRequest, urlParams: List[String]) = handlerFunction(request, urlParams)
