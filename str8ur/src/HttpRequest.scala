package net.garethrogers.str8ur

import scala.collection.mutable.HashMap
import scala.collection.immutable.Queue

case class HttpRequest(
  val url: String,
  val urlRaw: String,
  val queryParams: Map[String, List[String]],
  val method: String,
  val headers: HashMap[String, Queue[String]],
  val body: String,
  var urlParams: List[String] = List()
):
  def withParams(urlParams: List[String]) =
    this.urlParams = urlParams
    this
