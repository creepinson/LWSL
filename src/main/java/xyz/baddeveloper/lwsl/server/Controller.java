package xyz.baddeveloper.lwsl.server;

import xyz.baddeveloper.lwsl.packet.Packet;

import java.io.IOException;
import java.net.Socket;

public class Controller {

    private SocketServer socketServer;

    public Controller(SocketServer socketServer) {
        this.socketServer = socketServer;
    }

    public void sendPacketToAll(Packet packet){
        socketServer.getClients().forEach(socketHandler -> socketHandler.sendPacket(packet));
    }

    public void kickAll() {
        socketServer.getClients().forEach(this::kickClient);
    }

    public SocketHandler getClientByIP(String hostname){
        for(SocketHandler socketHandler : socketServer.getClients()){
            if(socketHandler.getSocket().getInetAddress().getHostName().equals(hostname))
                return socketHandler;
        }
        return null;
    }

    public void kickClient(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void kickClient(SocketHandler client) {
        try {
            client.getSocket().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
