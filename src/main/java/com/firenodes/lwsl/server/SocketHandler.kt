package com.firenodes.lwsl.server

import com.firenodes.lwsl.Events
import com.firenodes.lwsl.packet.Packet
import com.firenodes.lwsl.server.events.ServerEvent
import com.firenodes.lwsl.server.events.ServerPacketEvent
import reactor.core.publisher.Mono
import reactor.netty.Connection
import java.io.IOException
import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.Executors

class SocketHandler internal constructor(private val socketServer: SocketServer, val connection: Connection) {
    var id: String = UUID.randomUUID().toString()

    fun handle() {
        Executors.newSingleThreadExecutor().execute {
            while (connection.channel().isOpen) {
                try {
                    Packet.parseInbound(connection).subscribe { packet ->
                        socketServer.eventManager.fireEvent(
                            ServerPacketEvent(
                                Events.PACKET_RECEIVED.toString(),
                                socketServer,
                                this,
                                packet
                            )
                        )
                    }
                } catch (e: Exception) {
                    try {
                        connection.dispose()
                    } catch (ignored: IOException) {
                    }
                    socketServer.eventManager.fireEvent(ServerEvent(Events.DISCONNECT.toString(), socketServer, this))
                    socketServer.clients.remove(this.id)
                    break
                }
            }
        }
    }

    fun sendPacket(packet: Packet) {
        try {
            packet.name = packet.javaClass.simpleName.replace("Packet", "")
            connection.outbound().sendString(Mono.just(packet.jsonObject.toString()))
            socketServer.eventManager.fireEvent(ServerPacketEvent("packetSent", socketServer, this, packet))
        } catch (ignored: IOException) {
        }
    }

    fun getRemoteAddress(): InetSocketAddress {
        return connection.channel().remoteAddress() as InetSocketAddress
    }
}
