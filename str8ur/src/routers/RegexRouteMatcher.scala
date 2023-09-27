package net.garethrogers.str8ur.routers

import collection.mutable.ArrayBuffer

object RegexRouteMatcher:
  var regexNeedsRebuild = true
  val routes = ArrayBuffer[String]()
  var routeMatcher = "".r

  /**
   * Adds a route
   *
   * A route is added to the list as a regex in the form (?:METHOD|METHOD...)/ROUTE/?
   * 
   * @param methods A list of all the HTTP methods that should match this route
   * @param route   The regex for the route - must match the whole route and not use anchors
   *
   * @return the index of the newly inserted route - this is what will be returned if the route matches a URL
   */
  def addRoute(methods: List[String], route: String): Int =
    routes.append("(?:" + methods.mkString("|") + ")/" + route.stripPrefix("/").stripSuffix("/"))
    regexNeedsRebuild = true
    routes.size - 1

  /**
   * For convenience, since most often only a single method will be required
   * 
   * @param methods The HTTP method that should match this route
   * @param route   The regex for the route - must match the whole route and not use anchors
   */
  def addRoute(method: String, route: String): Int = addRoute(List(method), route)

  /**
   * Builds the regular expression used to match the route
   *
   * The regex is of the form ^(?:x{routeIndex}x* /ROUTE)$
   * The x{routeIndex}x* is used to identify which route has matched.
   * When we run the match, we filter the nulls - all the capture groups from routes that didn't match - so we need this
   * to know which index to pass back. The matches method adds a string of x characters long enough to cover all routes
   * at the beginning to make this work.
   */
  private def buildRegex =
    if(regexNeedsRebuild)
      routeMatcher =
        ("^(?:"
        + routes.zipWithIndex.map( (value, index) => s"(x{$index})x*/$value" ).mkString("|")
        + ")$").mkString("").r
      regexNeedsRebuild = false

  /**
   * Attemps to match the given method and URL
   * 
   * Adds one x to the start of the match URL for every possible route (see method buildRegex for the reason)
   * then a / then the method and then the URL.
   * 
   * @param method the received HTTP method
   * @param url the received URL
   * 
   * @return Option which contains the route index and the captured arguments if it matches
   */
  def matches(method: String, url: String) =
    buildRegex
    val matchUrl = "x" * (routes.size - 1) + "/" + method.toUpperCase + url
    routeMatcher.findFirstMatchIn(matchUrl).map(_.subgroups.filter(_ != null)) match
      case None => None
      case Some(args) => Some(args.head.size, args.tail)
