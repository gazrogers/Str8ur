package net.garethrogers.str8ur.servers

import net.garethrogers.str8ur.Str8urApp

trait Server:
  def start(port: Int, app: Str8urApp): Unit
