package net.garethrogers.str8ur

import scala.collection.mutable.HashMap
import scala.collection.immutable.Queue

case class HttpRequest(val url: String, val method: String, val headers: HashMap[String, Queue[String]], val body: String)
