package xyz.baddeveloper.lwsl.server;

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

    public SocketHandler(SocketServer socketServer, Socket socket) {
        this.socketServer = socketServer;
        this.socket = socket;

        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
        }catch (IOException ignored){}

    }

    public void handle(){
        Executors.newSingleThreadExecutor().execute(() -> {
            while(!socket.isClosed()) {
                try {
                    String test = dis.readUTF();
                    System.out.println(test);
                } catch (Exception e) {
                    try {socket.close();} catch (IOException ignored) {}
                    socketServer.getDisconnectEvents().forEach(onDisconnectEvent -> onDisconnectEvent.onDisconnect(socket));
                    break;
                }
            }
        });
    }
}
