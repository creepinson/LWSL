package xyz.baddeveloper.lwsl.client;

import org.json.JSONObject;
import org.junit.Test;
import xyz.baddeveloper.lwsl.client.exceptions.ConnectException;
import xyz.baddeveloper.lwsl.packet.Packet;
import xyz.baddeveloper.lwsl.server.SocketServer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SocketClientTest {

    @Test
    public void testSocketClient() throws Exception {
        System.out.println("--- Begin LWSL Tests ---");
        final List<Packet> receivedPackets = new ArrayList<>();
        final SocketServer socketServer = new SocketServer(25566)
                .setMaxConnections(0)
                .addPacketReceivedEvent((socket, packet) -> receivedPackets.add(packet));
        System.out.println("--- Begin LWSL Test [Socket Server] ---");
        socketServer.start();

        assertTrue(socketServer.getClients().isEmpty());
        assertTrue(receivedPackets.isEmpty());

        System.out.println("--- Begin LWSL Test [Socket Client] ---");
        final SocketClient socketClient = new SocketClient("localhost", 25566);
        socketClient.connect();

        assertEquals(1, socketServer.getClients().size());
        assertTrue(receivedPackets.isEmpty());

        System.out.println("--- Begin LWSL Test [Socket Packets] ---");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Key", "Value");

        socketClient.sendPacket(new Packet(jsonObject));
        awaitPacketOrFail(receivedPackets, 1, 1000);
        assertEquals("Value", receivedPackets.get(0).getObject().get("Key"));

        socketClient.sendPacket(new Packet());
        awaitPacketOrFail(receivedPackets, 2, 1000);
        System.out.println("--- End LWSL Test [Socket Packets] ---");

        socketClient.shutdown();
        System.out.println("--- End LWSL Test [Socket Client] ---");
        socketServer.stop();
        System.out.println("--- End LWSL Test [Socket Server] ---");
        System.out.println("--- End LWSL Tests ---");
    }

    private void awaitPacketOrFail(List<Packet> receivedPackets, int expectedSize, int timeout) throws InterruptedException {
        // Packets can take a few ms to arrive, and so be counted. We wait until we reach the expected size, or hit the timeout
        final long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() < startTime + timeout) && (receivedPackets.size() != expectedSize)) {
            Thread.sleep(50);
        }
        assertEquals(expectedSize, receivedPackets.size());
    }

    @Test(expected = ConnectException.class)
    public void testFailedConnection() throws Exception {
        final SocketClient socketClient = new SocketClient("localhost", 25567)
                .addConnectEvent(socket -> fail("Should not have connected"));
        socketClient.connect();
    }

}