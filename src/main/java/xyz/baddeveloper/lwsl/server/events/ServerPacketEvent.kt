package xyz.baddeveloper.lwsl.server.events

import xyz.baddeveloper.lwsl.packet.Packet
import xyz.baddeveloper.lwsl.server.SocketHandler
import xyz.baddeveloper.lwsl.server.SocketServer

class ServerPacketEvent(override val type: String, override val server: SocketServer, override val client: SocketHandler?, val packet: Packet) : ServerEvent(type, server, client)