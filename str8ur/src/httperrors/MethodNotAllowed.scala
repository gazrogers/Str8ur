package net.garethrogers.str8ur.httperrors

import net.garethrogers.str8ur.HttpResponse
import scala.collection.mutable.HashMap
import scala.collection.immutable.Queue

class MethodNotAllowed extends HttpResponse(code = 405, headers = HashMap[String, Queue[String]](), body = "Method Not Allowed")
