package com.firenodes.lwsl.client

import com.firenodes.lwsl.Events
import com.firenodes.lwsl.packet.Packet
import com.firenodes.lwsl.server.SocketServer
import com.firenodes.lwsl.server.events.ServerPacketEvent
import com.google.gson.JsonObject
import org.junit.Assert
import org.junit.Test
import java.util.*
import javax.net.ssl.SSLContext

class SocketClientTest {
    private var sslContext: SSLContext? = null

    @Test
    @Throws(Exception::class)
    fun testSocketClient() {
        val receivedPackets: MutableList<Packet> = ArrayList()
        val socketServer: SocketServer = SocketServer(25566)
            .setMaxConnections(0).start()
        socketServer.createEventBus().observeEvents().subscribe() {
            when (it.type) {
                Events.PACKET_RECEIVED.toString() -> {
                    receivedPackets.add((it as ServerPacketEvent).packet)
                }
            }
        }

    }

    val socketClient = SocketClient("localhost", 25566)
    var jsonObject = JsonObject()

    @Throws(InterruptedException::class)
    private fun awaitOrFail(list: List<*>, expectedSize: Int, timeout: Int) {
        // Packets can take a few ms to arrive, and so be counted. We wait until we reach the expected size, or hit the timeout
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() < startTime + timeout && list.size != expectedSize) {
            Thread.sleep(20)
        }
        Assert.assertEquals(expectedSize.toLong(), list.size.toLong())
    }
}
