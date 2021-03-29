package com.firenodes.lwsl.server

import com.firenodes.lwsl.packet.Packet
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.function.Consumer


class Controller(private val socketServer: SocketServer) {
    fun sendPacketToAll(packet: Packet) {
        socketServer.clients.values.forEach(Consumer { socketHandler: SocketHandler? -> socketHandler!!.sendPacket(packet) })
    }

    fun kickAll() {
        socketServer.clients.values.forEach(Consumer { client: SocketHandler? -> this.kickClient(client) })
    }

    fun getClientByIP(hostname: String): SocketHandler? {
        for (socketHandler in socketServer.clients) {
            if ((socketHandler.value?.connection?.channel()?.remoteAddress() as InetSocketAddress).address.hostAddress == hostname) return socketHandler.value
        }
        return null
    }

    fun kickClient(socket: Socket) {
        try {
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun kickClient(client: SocketHandler?) {
        client?.connection?.dispose()
    }

}
