package xyz.baddeveloper.lwsl.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketHandler {

    private SocketServer socketServer;
    private Socket socket;

    private DataInputStream dis;
    private DataOutputStream dos;

    public SocketHandler(SocketServer socketServer, Socket socket) {
        this.socketServer = socketServer;
        this.socket = socket;

        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        }catch (IOException ignored){}

    }

    public void handle(){
        if (!socket.isClosed()) {
            new Thread(() -> {
                while (!socket.isClosed() && socket.isConnected()) {

                }
                socketServer.getDisconnectEvents().forEach(onDisconnectEvent -> onDisconnectEvent.onDisconnect(socket));
            }).start();

            // Make disconnect thread
        }
    }
}
