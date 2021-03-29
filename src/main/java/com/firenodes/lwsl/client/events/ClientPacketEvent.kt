package com.firenodes.lwsl.client.events

import com.firenodes.lwsl.client.SocketClient
import com.firenodes.lwsl.packet.Packet

class ClientPacketEvent(override val type: String, override val client: SocketClient, val packet: Packet) : ClientEvent(type, client)
