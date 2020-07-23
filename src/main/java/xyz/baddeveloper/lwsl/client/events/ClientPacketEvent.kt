package xyz.baddeveloper.lwsl.client.events

import xyz.baddeveloper.lwsl.client.SocketClient
import xyz.baddeveloper.lwsl.packet.Packet

class ClientPacketEvent(override val type: String, override val client: SocketClient, val packet: Packet) : ClientEvent(type, client)