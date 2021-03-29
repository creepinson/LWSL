package com.firenodes.lwsl.server.events

import com.firenodes.lwsl.packet.Packet
import com.firenodes.lwsl.server.SocketHandler
import com.firenodes.lwsl.server.SocketServer

class ServerPacketEvent(override val type: String, override val server: SocketServer, override val client: SocketHandler?, val packet: Packet) : ServerEvent(type, server, client)
