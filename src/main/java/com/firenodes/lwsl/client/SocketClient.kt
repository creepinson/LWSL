package com.firenodes.lwsl.client

import com.firenodes.lwsl.Events
import com.firenodes.lwsl.client.events.ClientEvent
import com.firenodes.lwsl.client.events.ClientPacketEvent
import com.firenodes.lwsl.exceptions.ConnectException
import com.firenodes.lwsl.packet.Packet
import dev.throwouterror.eventbus.TypedEventBus
import io.netty.handler.logging.LogLevel
import reactor.core.publisher.Mono
import reactor.netty.Connection
import reactor.netty.tcp.SslProvider
import reactor.netty.tcp.TcpClient
import java.io.IOException

class SocketClient @JvmOverloads constructor(
    var address: String,
    var port: Int,
    private val sslProvider: SslProvider? = null
) {
    var timeout = 0
        private set
    var isKeepAlive = false
        private set

    private val eventManager = TypedEventBus<ClientEvent>()
    var isConnected = false
        private set
    var client: TcpClient = TcpClient.create().host(address).port(port)
        private set
    lateinit var connection: Connection

    @Throws(ConnectException::class)
    fun connect(): SocketClient {
        listen()
        return this
    }

    fun shutdown() {
        try {
            connection.disposeNow()
            isConnected = false
        } catch (ignored: IOException) {
        }
    }

    private fun listen() {
        client.doOnConnected { it ->
            eventManager.fireEvent(ClientEvent(Events.READY.toString(), this))
            try {
                Packet.parseInbound(it).subscribe {
                    eventManager.fireEvent(
                        ClientPacketEvent(
                            Events.PACKET_RECEIVED.toString(),
                            this,
                            it
                        )
                    )
                }
            } catch (e: Exception) {
                try {
                    connection.disposeNow()
                } catch (ignored: IOException) {
                }
                eventManager.fireEvent(ClientEvent(Events.DISCONNECT.toString(), this))
                return@doOnConnected
            }
        }
        connection = client.connectNow()
    }

    fun sendPacket(packet: Packet) {
        try {
            packet.name = packet.javaClass.simpleName.replace("Packet", "")
            connection.outbound().sendString(Mono.just(packet.jsonObject.toString()))
            eventManager.fireEvent(ClientPacketEvent(Events.PACKET_SENT.toString(), this, packet))
        } catch (e: IOException) {
            e.printStackTrace()
            // TODO: Create exception
        }
    }

    fun enableDebugLogging(): SocketClient {
        client.wiretap("LWSL", LogLevel.INFO)
        return this
    }

    fun setAddress(address: String): SocketClient {
        this.address = address
        return this
    }

    fun setPort(port: Int): SocketClient {
        this.port = port
        return this
    }

    fun setTimeout(timeout: Int): SocketClient {
        this.timeout = timeout
        return this
    }

    fun setKeepAlive(keepAlive: Boolean): SocketClient {
        isKeepAlive = keepAlive
        return this
    }

    fun createEventBus(): TypedEventBus<ClientEvent> {
        return eventManager
    }
}
