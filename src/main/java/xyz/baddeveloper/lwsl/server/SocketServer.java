package xyz.baddeveloper.lwsl.server;

import xyz.baddeveloper.lwsl.server.events.OnConnectEvent;
import xyz.baddeveloper.lwsl.server.events.OnDisconnectEvent;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketServer {

    private int port;
    private int maxconnections;
    private int timeout;

    private List<OnConnectEvent> connectEvents = new ArrayList<>();
    private List<OnDisconnectEvent> disconnectEvents = new ArrayList<>();

    private boolean running = false;
    private ServerSocket serverSocket;

    public SocketServer() {
        this.port = 8080;
        this.maxconnections = 1000;
        this.timeout = 0;
    }

    public SocketServer(int port) {
        this.port = port;
        this.maxconnections = 1000;
        this.timeout = 0;
    }

    public void start(){
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(timeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = serverSocket != null;
        if(!running) return;
        listen();
    }

    private void listen(){
        new Thread(() -> {
            while(!serverSocket.isClosed() && serverSocket.isBound()) try {
                Socket socket = serverSocket.accept();
                connectEvents.forEach(onConnectEvent -> onConnectEvent.onConnect(socket));
                if (!socket.isClosed()) new Thread(() -> {
                    while (!serverSocket.isClosed())
                        if (socket.isClosed() || !socket.isConnected()){disconnectEvents.forEach(disconnectEvent -> disconnectEvent.onDisconnect(socket)); return;}
                }).start();
            } catch (IOException ignored) {}
        }).start();
    }

    public void stop(){
        if(serverSocket != null && serverSocket.isClosed())
            try { serverSocket.close(); } catch (IOException e) { e.printStackTrace(); }
    }

    public SocketServer addDisconnectEvent(OnDisconnectEvent event){
        disconnectEvents.add(event);
        return this;
    }

    public SocketServer removeDisconnectEvent(OnDisconnectEvent event){
        disconnectEvents.remove(event);
        return this;
    }

    public SocketServer addConnectEvent(OnConnectEvent event){
        connectEvents.add(event);
        return this;
    }

    public SocketServer removeConnectEvent(OnConnectEvent event){
        connectEvents.remove(event);
        return this;
    }

    public SocketServer setPort(int port){
        this.port = port;
        return this;
    }

    public SocketServer setMaxConnections(int maxConnections){
        this.maxconnections = maxConnections;
        return this;
    }

    public SocketServer setTimeout(int timeout){
        this.timeout = timeout;
        return this;
    }

    public boolean isRunning() {
        return running;
    }
}
