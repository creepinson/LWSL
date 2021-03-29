package com.firenodes.lwsl.server.events

import com.firenodes.lwsl.server.SocketHandler
import com.firenodes.lwsl.server.SocketServer

open class ServerEvent(open val type: String, open val server: SocketServer, open val client: SocketHandler? = null)
