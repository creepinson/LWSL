package xyz.baddeveloper.lwsl.server;

import org.json.JSONObject;
import xyz.baddeveloper.lwsl.Packet;
import xyz.baddeveloper.lwsl.server.events.ServerDisconnectEvent;
import xyz.baddeveloper.lwsl.server.events.ServerPacketReceivedEvent;
import xyz.baddeveloper.lwsl.server.events.ServerPacketSentEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;

public class SocketHandler {

    private SocketServer socketServer;
    private Socket socket;

    private DataInputStream dis;
    private DataOutputStream dos;

    SocketHandler(SocketServer socketServer, Socket socket) {
        this.socketServer = socketServer;
        this.socket = socket;

        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ignored) {
        }

    }

    public void handle() {
        Executors.newSingleThreadExecutor().execute(() -> {
            while (!socket.isClosed()) {
                try {
                    Packet packetIn = new Packet(new JSONObject(dis.readUTF()));
                    socketServer.eventManager.fireEvent(new ServerPacketReceivedEvent(socketServer, this, packetIn));
                } catch (Exception e) {
                    try {
                        socket.close();
                    } catch (IOException ignored) {
                    }
                    socketServer.eventManager.fireEvent(new ServerDisconnectEvent(socketServer, this));
                    socketServer.getClients().remove(this);
                    break;
                }
            }
        });
    }

    public void sendPacket(Packet packet) {
        try {
            packet.setName(packet.getClass().getSimpleName().replace("Packet", ""));
            dos.writeUTF(packet.getObject().toString());
            dos.flush();
            socketServer.eventManager.fireEvent(new ServerPacketSentEvent(socketServer, packet));
        } catch (IOException ignored) {
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public DataOutputStream getDos() {
        return dos;
    }
}
