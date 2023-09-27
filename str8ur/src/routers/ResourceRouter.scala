package net.garethrogers.str8ur.routers

import net.garethrogers.str8ur.mvc.Resource
import net.garethrogers.str8ur.HttpResponse
import net.garethrogers.str8ur.HttpRequest

import scala.jdk.CollectionConverters._
import scala.collection.mutable.HashMap
import scala.collection.immutable.Queue
import org.reflections.Reflections
import org.reflections.scanners.Scanners.SubTypes
import org.reflections.util.ConfigurationBuilder
import java.lang.reflect.Method

trait ResourceRouter extends Router:
  // Set up the Reflections instance
  Reflections(
    ConfigurationBuilder()
    .forPackage(this.getClass.getPackageName)
    .setScanners(SubTypes)
  )
  // Get all the Controllers defined by the user...
  .get(SubTypes.of(classOf[Resource]).asClass())
  // ...and register their actions
  .asScala.map(resource =>
    val declaredMethods = resource.getMethods().map(_.getName().toUpperCase)
    val (defined, undefined) = allHttpMethods.partition(declaredMethods.contains(_))
    // Individual handler for each defined method
    defined.foreach(
      httpMethod =>
        val routeIndex = RegexRouteMatcher.addRoute(httpMethod, resource.getSimpleName().toLowerCase + "(.*)")
        val method = resource.getMethod(httpMethod.toLowerCase)
        routeHandlers.addOne(routeIndex -> makeHandler[Resource](resource, method))
    )
    // One handler to handle all the undefined methods
    val routeIndex = RegexRouteMatcher.addRoute(undefined, resource.getSimpleName().toLowerCase + "(.*)")
    routeHandlers.addOne(
      routeIndex -> ((request: HttpRequest, urlParams: List[String]) => HttpResponse.methodNotAllowed)
    )
  )

  override def handleRequest(request: HttpRequest): HttpResponse =
    RegexRouteMatcher.matches(request.method.toUpperCase, request.url.toLowerCase) match
      case Some(routeIndex, routeArgs) =>
        routeHandlers(routeIndex)(request, routeArgs.flatMap(_.stripPrefix("/").stripSuffix("/").split("/")))
      case None => super.handleRequest(request)
