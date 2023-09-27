package net.garethrogers.str8ur.routers

import net.garethrogers.str8ur.mvc.Controller
import net.garethrogers.str8ur.HttpRequest
import net.garethrogers.str8ur.HttpResponse

import scala.jdk.CollectionConverters._
import org.reflections.Reflections
import org.reflections.scanners.Scanners.SubTypes
import org.reflections.util.ConfigurationBuilder
import java.lang.reflect.Method
import java.lang.module.ModuleDescriptor.Requires

trait ControllerRouter extends Router:
  // Set up the Reflections instance
  Reflections(
    ConfigurationBuilder()
    .forPackage(this.getClass.getPackageName)
    .setScanners(SubTypes)
  )
  // Get all the Controllers defined by the user...
  .get(SubTypes.of(classOf[Controller]).asClass())
  // ...and register their actions
  .asScala.map(controller =>
    controller
    .getMethods()
    .filter(actionMethodFilter)
    .foreach(
      method =>
        val routeIndex = RegexRouteMatcher.addRoute(allHttpMethods, getRoutePath(controller, method) + "(.*)")
        routeHandlers.addOne(routeIndex -> makeHandler[Controller](controller, method))
    )
  )

  private def actionMethodFilter(method: Method) =
    (method.getGenericReturnType() == classOf[Unit]
    || method.getGenericReturnType() == classOf[String]
    || method.getGenericReturnType() == classOf[HttpResponse])
    && method.getName().toLowerCase.endsWith("action")

  private def getRoutePath(controller: Class[?], method: Method) = 
    val controllerName = controller.getSimpleName().toLowerCase.replace("controller", "")
    val methodName = method.getName().toLowerCase.replace("action", "")
    (controllerName, methodName) match
      case ("index", "index") => ""
      case (matchedController, "index") => matchedController
      case (matchedController, matchedAction) => matchedController + "/" + matchedAction

  override def handleRequest(request: HttpRequest): HttpResponse =
    RegexRouteMatcher.matches(request.method.toUpperCase, request.url.toLowerCase) match
      case Some(routeIndex, routeArgs) =>
          routeHandlers(routeIndex)(request, routeArgs.flatMap(_.stripPrefix("/").stripSuffix("/").split("/")))
      case None => super.handleRequest(request)
