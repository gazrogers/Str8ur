package net.garethrogers.str8ur

import scala.collection.mutable.HashMap
import scala.collection.immutable.Queue

class HttpResponse(var code: Int, val headers: HashMap[String, Queue[String]], var body: String)
