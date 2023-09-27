package net.garethrogers.str8ur.routers

import net.garethrogers.str8ur.mvc.Handler
import net.garethrogers.str8ur.HttpRequest
import net.garethrogers.str8ur.HttpResponse
import net.garethrogers.str8ur.httperrors.NotFound
import scala.collection.mutable.HashMap
import java.lang.reflect.Method

type RouteReturnTypes = HttpResponse | String | Unit
type RequestHandler = (HttpRequest, List[String]) => HttpResponse

trait Router:  
  val routeHandlers = HashMap[Int, RequestHandler]()
  val allHttpMethods = List("GET", "HEAD", "POST", "PUT", "DELETE", "CONNECT", "OPTIONS", "TRACE", "PATCH")

  /**
   * A method that takes an HTTP request and returns the appropriate handler
   * 
   * This is the one that router subclasses should override
   */
  def handleRequest(request: HttpRequest): HttpResponse = 
    NotFound()

  protected def makeHandler[T <: Handler](controller: Class[?], method: Method): RequestHandler = 
    (request: HttpRequest, urlParams: List[String]) => {
      val obj = controller.getDeclaredConstructor().newInstance().asInstanceOf[T]
      obj.request = request.withParams(urlParams)
      obj.normaliseResponse(method.invoke(obj))
    }
