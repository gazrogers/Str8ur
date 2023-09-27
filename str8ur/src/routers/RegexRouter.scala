package net.garethrogers.str8ur.routers

import net.garethrogers.str8ur.mvc.RegexRouteHandler
import net.garethrogers.str8ur.HttpResponse
import net.garethrogers.str8ur.HttpRequest
import net.garethrogers.str8ur.routers.RouteReturnTypes

trait RegexRouter extends Router:
  def get(route: String)(handler: RequestHandler) = addRoute("GET", route, handler)
  def post(route: String)(handler: RequestHandler) = addRoute("POST", route, handler)
  def head(route: String)(handler: RequestHandler) = addRoute("HEAD", route, handler)
  def put(route: String)(handler: RequestHandler) = addRoute("PUT", route, handler)
  def delete(route: String)(handler: RequestHandler) = addRoute("DELETE", route, handler)
  def connect(route: String)(handler: RequestHandler) = addRoute("CONNECT", route, handler)
  def options(route: String)(handler: RequestHandler) = addRoute("OPTIONS", route, handler)
  def trace(route: String)(handler: RequestHandler) = addRoute("TRACE", route, handler)
  def patch(route: String)(handler: RequestHandler) = addRoute("PATCH", route, handler)

  def addRoute(httpMethod: String, route: String, handler: RequestHandler) =
    val routeIndex = RegexRouteMatcher.addRoute(httpMethod, route)
    routeHandlers.addOne(routeIndex -> wrapHandler(handler))

  /**
   * Allows for a handler to return either a string or an HttpResponse or nothing at all
   *
   * @param handler the handler to wrap
   *
   * @return then wrapped handler
   */
  protected def wrapHandler(passedHandler: RequestHandler): RequestHandler = 
    (request: HttpRequest, urlParams: List[String]) => {
      // Declare an anopnymous class to hold the request and parameters - this feels clunky
      val routeHandler = new RegexRouteHandler(passedHandler)
      routeHandler.request = request.withParams(urlParams)
      routeHandler.normaliseResponse(routeHandler.handle(request, urlParams))
    }