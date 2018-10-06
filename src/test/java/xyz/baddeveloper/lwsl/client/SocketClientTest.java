package xyz.baddeveloper.lwsl.client;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import xyz.baddeveloper.lwsl.client.exceptions.ConnectException;
import xyz.baddeveloper.lwsl.packet.Packet;
import xyz.baddeveloper.lwsl.server.SocketServer;
import xyz.baddeveloper.lwsl.ssl.SSLContextConfig;
import xyz.baddeveloper.lwsl.ssl.SSLContextUtility;

import javax.net.ssl.SSLContext;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SocketClientTest {

    private SSLContext sslContext;

    @Before
    public void setUp() {
        final SSLContextConfig config = SSLContextConfig.builder()
                .keystoreLocation("keystore.jks")
                .keystorePassword("changeit")
                .truststoreLocation("truststore.jks")
                .truststorePassword("changeit")
                .build();
        sslContext = SSLContextUtility.getInstance().getSslContext(config);
    }


    @Test
    public void testSocketClient() throws Exception {
        final List<Packet> receivedPackets = new ArrayList<>();
        final SocketServer socketServer = new SocketServer(25566)
                .setMaxConnections(0)
                .addPacketReceivedEvent((socket, packet) -> receivedPackets.add(packet));
        socketServer.start();

        assertTrue(socketServer.getClients().isEmpty());
        assertTrue(receivedPackets.isEmpty());

        final SocketClient socketClient = new SocketClient("localhost", 25566);
        socketClient.connect();

        assertTrue(socketClient.isConnected());
        awaitOrFail(socketServer.getClients(), 1, 1000);
        assertTrue(receivedPackets.isEmpty());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Key", "Value");

        socketClient.sendPacket(new Packet(jsonObject));
        awaitOrFail(receivedPackets, 1, 1000);
        assertEquals("Value", receivedPackets.get(0).getObject().get("Key"));

        socketClient.sendPacket(new Packet());
        awaitOrFail(receivedPackets, 2, 1000);

        socketClient.shutdown();
        socketServer.stop();
    }

    @Test
    public void testSecureSocketClient() throws Exception {
        final List<Packet> receivedPackets = new ArrayList<>();
        final SocketServer socketServer = new SocketServer(25568, sslContext)
                .setMaxConnections(0)
                .addPacketReceivedEvent((socket, packet) -> receivedPackets.add(packet));
        socketServer.start();

        assertTrue(socketServer.getClients().isEmpty());
        assertTrue(receivedPackets.isEmpty());

        final SocketClient socketClient = new SocketClient("localhost", 25568, sslContext)
                .addPacketReceivedEvent(((socket, packet) -> receivedPackets.add(packet)));
        socketClient.connect();
        assertTrue(socketClient.isConnected());

        awaitOrFail(socketServer.getClients(), 1, 1000);
        assertTrue(receivedPackets.isEmpty());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Key", "Value");

        socketClient.sendPacket(new Packet(jsonObject));
        awaitOrFail(receivedPackets, 1, 5000);
        assertEquals("Value", receivedPackets.get(0).getObject().get("Key"));

        socketServer.getClients().get(0).sendPacket(new Packet());
        awaitOrFail(receivedPackets, 2, 5000);

        socketClient.shutdown();
        socketServer.stop();
    }

    private void awaitOrFail(List list, int expectedSize, int timeout) throws InterruptedException {
        // Packets can take a few ms to arrive, and so be counted. We wait until we reach the expected size, or hit the timeout
        final long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() < startTime + timeout) && (list.size() != expectedSize)) {
            Thread.sleep(20);
        }
        assertEquals(expectedSize, list.size());
    }

    @Test(expected = ConnectException.class)
    public void testFailedConnection() throws Exception {
        final SocketClient socketClient = new SocketClient("localhost", 25567)
                .addConnectEvent(socket -> fail("Should not have connected"));
        socketClient.connect();
    }

}