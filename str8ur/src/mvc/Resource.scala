package net.garethrogers.str8ur.mvc

import net.garethrogers.str8ur.HttpRequest
import net.garethrogers.str8ur.HttpResponse
import scala.collection.mutable.HashMap
import scala.collection.immutable.Queue

trait Resource extends Handler:
  var response: HttpResponse = HttpResponse.ok
