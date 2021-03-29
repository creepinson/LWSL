package com.firenodes.lwsl.server

import dev.throwouterror.eventbus.TypedEventBus
import io.netty.handler.logging.LogLevel
import io.netty.handler.timeout.ReadTimeoutHandler
import reactor.netty.DisposableServer
import reactor.netty.tcp.SslProvider
import reactor.netty.tcp.TcpServer
import com.firenodes.lwsl.Events
import com.firenodes.lwsl.exceptions.ConnectException
import com.firenodes.lwsl.packet.ClientIdentificationPacket
import com.firenodes.lwsl.server.events.ServerEvent
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

class SocketServer @JvmOverloads constructor(var port: Int = DEFAULT_PORT, private val sslProvider: SslProvider? = null) {
    var maxconnections = DEFAULT_MAX_CONNECTIONS
        private set
    var timeout = 0
        private set
    val eventManager = TypedEventBus<ServerEvent>()
    val clients: MutableMap<String, SocketHandler?> = mutableMapOf()
    var isRunning = false
        private set
    var controller: Controller? = null
        private set
    var serverSocket: TcpServer = TcpServer.create()
        private set
    lateinit var disposableServer: DisposableServer
        private set
    private var clientCreationHandler: ClientCreationHandler = object : ClientCreationHandler {
        override fun createClient(socketHandler: SocketHandler) {
            socketHandler.sendPacket(ClientIdentificationPacket(socketHandler.id))
            clients[socketHandler.id] = socketHandler
        }
    }

    constructor(sslContext: SslProvider?) : this(DEFAULT_PORT, sslContext) {}

    fun start(): SocketServer {
        isRunning = true
        listen()
        return this
    }

    private fun listen() {
        if (!isRunning) return
        controller = Controller(this)
        try {
            serverSocket.doOnConnection { socket ->
                var socketHandler: SocketHandler? = null
                try {
                    socket.addHandler(ReadTimeoutHandler(timeout.toLong(), TimeUnit.SECONDS))
                    socketHandler = SocketHandler(this, socket)
                    if (maxconnections != 0 && clients.size >= maxconnections) {
                        eventManager.fireEvent(ServerEvent(Events.DISCONNECT.toString(), this, socketHandler))
                        socket.dispose()
                    }
                    clientCreationHandler.createClient(socketHandler)
                    eventManager.fireEvent(ServerEvent(Events.CONNECT.toString(), this, socketHandler))
                    socketHandler.handle()
                } catch (ignored: IOException) {
                    eventManager.fireEvent(ServerEvent(Events.DISCONNECT.toString(), this, socketHandler))
                    if (socketHandler != null) {
                        clients.remove(socketHandler.id)
                    }
                }
            }.doOnBound {
                eventManager.fireEvent(ServerEvent(Events.READY.toString(), this))
            }.host("0.0.0.0").port(port)

            if (sslProvider != null) serverSocket.secure(sslProvider)
            disposableServer = serverSocket.bindNow()
        } catch (e: Exception) {
            throw ConnectException(e)
        }
    }

    fun enableDebugLogging(): SocketServer {
        serverSocket.wiretap("LWSL", LogLevel.INFO)
        return this
    }

    fun handleClientCreation(handler: ClientCreationHandler): SocketServer {
        clientCreationHandler = handler
        return this
    }

    fun stop() {
        disposableServer.disposeNow()
    }

    fun createEventBus(): TypedEventBus<ServerEvent> {
        return eventManager
    }

    fun setPort(port: Int): SocketServer {
        this.port = port
        return this
    }

    fun setMaxConnections(maxConnections: Int): SocketServer {
        maxconnections = maxConnections
        return this
    }

    fun setTimeout(timeout: Int): SocketServer {
        this.timeout = timeout
        return this
    }

    companion object {
        const val DEFAULT_PORT = 8080
        const val DEFAULT_MAX_CONNECTIONS = 1000
    }
}
