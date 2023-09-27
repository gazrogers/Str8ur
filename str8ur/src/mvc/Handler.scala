package net.garethrogers.str8ur.mvc

import net.garethrogers.str8ur.routers.RouteReturnTypes
import net.garethrogers.str8ur.HttpResponse
import net.garethrogers.str8ur.HttpRequest
import scala.collection.mutable.HashMap
import scala.collection.immutable.Queue

trait Handler:
  var response: HttpResponse
  var request = HttpRequest("", "", Map[String, List[String]](), "", HashMap[String, Queue[String]](), "")

  def normaliseResponse(passedResponse: Any): HttpResponse =
    passedResponse match
      case passedResponse: HttpResponse => passedResponse
      case str: String =>
        response.body = str
        response
      case _ => response
