package net.garethrogers.str8urexamples.withrouters

import net.garethrogers.str8ur.mvc.Controller
import net.garethrogers.str8ur.HttpResponse
import net.garethrogers.str8ur.HttpRequest
import scala.collection.mutable.HashMap
import scala.collection.immutable.Queue

class MyController extends Controller:

  /**
   * Methods that return Unit return the response contained n the controller instance
   */
  def unitAction(request: HttpRequest): Unit =
    this.response.body = "My action response"

  /**
   * Methods that return String add the String to the response before returning it
   */
  def stringAction(request: HttpRequest): String =
    "Some string or another"

  /**
   * Methods that return an HttpResponse just return the response
   */
  def httpResponseAction(request: HttpRequest): HttpResponse =
    HttpResponse(415, HashMap[String, Queue[String]](), "A http response action")
