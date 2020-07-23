package xyz.baddeveloper.lwsl.server

import org.json.JSONObject
import reactor.core.publisher.Mono
import reactor.netty.Connection
import xyz.baddeveloper.lwsl.Events
import xyz.baddeveloper.lwsl.packet.Packet
import xyz.baddeveloper.lwsl.server.events.ServerEvent
import xyz.baddeveloper.lwsl.server.events.ServerPacketEvent
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
                    val packetIn = Packet(JSONObject(connection.inbound().receiveObject().subscribe()))
                    socketServer.eventManager.fireEvent(ServerPacketEvent(Events.PACKET_RECEIVED.toString(), socketServer, this, packetIn))
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