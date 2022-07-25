package net.garethrogers.str8urexamples.withrouters

import net.garethrogers.str8ur.Str8urApp
import net.garethrogers.str8ur.routers.ControllerRouter

object WithControllerRouter extends Str8urApp with ControllerRouter:

  @main
  def main(args: String*) =
    startServer()
