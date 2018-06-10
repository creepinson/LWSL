package xyz.baddeveloper.lwsl.server;

import org.json.JSONObject;
import xyz.baddeveloper.lwsl.packet.Packet;

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
        }catch (IOException ignored){}

    }

    void handle(){
        Executors.newSingleThreadExecutor().execute(() -> {
            while(!socket.isClosed()) {
                try {
                    Packet in = new Packet(new JSONObject(dis.readUTF()));
                    socketServer.getPacketReceivedEvents().forEach(onPacketReceivedEvent -> onPacketReceivedEvent.onPacketReceived(this, in));
                } catch (Exception e) {
                    try {socket.close();} catch (IOException ignored) {}
                    socketServer.getDisconnectEvents().forEach(onDisconnectEvent -> onDisconnectEvent.onDisconnect(socket));
                    socketServer.getClients().remove(this);
                    break;
                }
            }
        });
    }

    public void sendPacket(Packet packet){
        try{
            dos.writeUTF(packet.getObject().toString());
            dos.flush();
            socketServer.getPacketSentEvents().forEach(onPacketSentEvent -> onPacketSentEvent.onPacketSent(this, packet));
        }catch (IOException ignored){}
    }
}
