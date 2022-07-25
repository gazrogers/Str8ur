package net.garethrogers.str8ur.routers

import net.garethrogers.str8ur.mvc.Controller
import net.garethrogers.str8ur.HttpRequest
import net.garethrogers.str8ur.HttpResponse

import collection.JavaConverters.asScalaSetConverter
import org.reflections.Reflections
import org.reflections.scanners.Scanners.SubTypes
import org.reflections.util.ConfigurationBuilder
import scala.collection.mutable
import java.lang.reflect.Method
import java.lang.module.ModuleDescriptor.Requires

trait ControllerRouter extends Router:
  val routes =
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
    .foldLeft(Map[String, RequestHandler]()) {
      (map, method) => map + ( getRoutePath(controller, method) -> makeHandler(controller, method) )
    }
  )
  .foldLeft(Map[String, RequestHandler]()) {_ ++ _}

  private def actionMethodFilter(method: Method) =
    (method.getGenericReturnType() == classOf[Unit]
    || method.getGenericReturnType() == classOf[String]
    || method.getGenericReturnType() == classOf[HttpResponse])
    && method.getName().toLowerCase.endsWith("action")

  private def getRoutePath(controller: Class[?], method: Method) = 
    controller.getSimpleName().replace("Controller", "").toLowerCase
    + "/"
    + method.getName().replace("Action", "").toLowerCase

  private def makeHandler(controller: Class[?], method: Method): RequestHandler = 
    (request: HttpRequest) => {
      val obj = controller.newInstance.asInstanceOf[Controller]
      method.invoke(obj, request) match
        case str: String =>
          obj.response.body = str
          obj.response
        case response: HttpResponse => response
        case _ => obj.response
    }

  override def handler(request: HttpRequest): RequestHandler =
    routes.getOrElse(request.url.toLowerCase.split('/').drop(1).take(2).mkString("/"), super.handler(request))
