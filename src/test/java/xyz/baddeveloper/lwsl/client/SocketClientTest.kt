package xyz.baddeveloper.lwsl.client

import org.json.JSONObject
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import xyz.baddeveloper.lwsl.Events
import xyz.baddeveloper.lwsl.packet.Packet
import xyz.baddeveloper.lwsl.server.SocketServer
import xyz.baddeveloper.lwsl.server.events.ServerPacketEvent
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLException

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
    var jsonObject = JSONObject()

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