package net.garethrogers.str8ur.routers

import org.scalatest.funsuite.AnyFunSuite

class RegexRouteMatcherTests extends AnyFunSuite:
  test("Correct indeces are returned when inserting routes") {
    assert(RegexRouteMatcher.addRoute("GET", "1") == 0)
    assert(RegexRouteMatcher.addRoute("POST", "A") == 1)
    assert(RegexRouteMatcher.addRoute("PUT", "fred") == 2)
    assert(RegexRouteMatcher.addRoute("GET", "x") == 3)
  }

  test("All methods match for a route") {
    RegexRouteMatcher.addRoute(List("GET", "PUT", "POST", "HEAD"), "myroute")
    RegexRouteMatcher.addRoute("POST", "someotherroute")
    assert(RegexRouteMatcher.matches("GET", "/myroute") == Some(4, List()))
    assert(RegexRouteMatcher.matches("PUT", "/myroute") == Some(4, List()))
    assert(RegexRouteMatcher.matches("POST", "/myroute") == Some(4, List()))
    assert(RegexRouteMatcher.matches("HEAD", "/myroute") == Some(4, List()))
    assert(RegexRouteMatcher.matches("OPTIONS", "/myroute") == None)
  }

  test("The correct index is returned for a match") {
    assert(RegexRouteMatcher.addRoute("GET", "route1") == 6)
    assert(RegexRouteMatcher.addRoute("GET", "route2") == 7)
    assert(RegexRouteMatcher.addRoute("GET", "route3") == 8)
    assert(RegexRouteMatcher.addRoute("GET", "route4") == 9)
    assert(RegexRouteMatcher.addRoute("GET", "route5") == 10)
    assert(RegexRouteMatcher.addRoute("GET", "route6") == 11)
    assert(RegexRouteMatcher.matches("GET", "/route1") == Some(6, List()))
    assert(RegexRouteMatcher.matches("GET", "/route2") == Some(7, List()))
    assert(RegexRouteMatcher.matches("GET", "/route3") == Some(8, List()))
    assert(RegexRouteMatcher.matches("GET", "/route4") == Some(9, List()))
    assert(RegexRouteMatcher.matches("GET", "/route5") == Some(10, List()))
    assert(RegexRouteMatcher.matches("GET", "/route6") == Some(11, List()))
  }