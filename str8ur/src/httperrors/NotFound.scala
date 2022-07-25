package net.garethrogers.str8ur.httperrors

import net.garethrogers.str8ur.HttpResponse
import scala.collection.mutable.HashMap
import scala.collection.immutable.Queue

class NotFound extends HttpResponse(code = 404, headers = HashMap[String, Queue[String]](), body = "File not found")
