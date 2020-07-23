package xyz.baddeveloper.lwsl.server.events

import xyz.baddeveloper.lwsl.server.SocketHandler
import xyz.baddeveloper.lwsl.server.SocketServer

open class ServerEvent(open val type: String, open val server: SocketServer, open val client: SocketHandler? = null)