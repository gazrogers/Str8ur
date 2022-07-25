package net.garethrogers.str8ur.routers

import net.garethrogers.str8ur.HttpRequest
import net.garethrogers.str8ur.HttpResponse
import net.garethrogers.str8ur.httperrors.NotFound

trait Router:
  type RequestHandler = HttpRequest => HttpResponse
  
  /**
   * Invokes the handler method
   */
  def handleRequest(request: HttpRequest): HttpResponse = handler(request)(request)

  /**
   * A method that takes an HTTP request and returns the appropriate handler
   * 
   * This is the one that router subclasses should override
   */
  def handler(request: HttpRequest): RequestHandler = _ => NotFound()
