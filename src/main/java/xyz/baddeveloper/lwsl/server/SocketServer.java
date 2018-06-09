package xyz.baddeveloper.lwsl.server;

import xyz.baddeveloper.lwsl.server.events.OnConnectEvent;
import xyz.baddeveloper.lwsl.server.events.OnDisconnectEvent;
import xyz.baddeveloper.lwsl.server.events.OnReadyEvent;

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
    private List<OnReadyEvent> readyEvents = new ArrayList<>();

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
                new SocketHandler(this, socket).handle();
            } catch (IOException ignored) {}
        }).start();
        readyEvents.forEach(readyEvent -> readyEvent.onReady(this));
    }

    public void stop(){
        if(serverSocket != null && serverSocket.isClosed())
            try { serverSocket.close(); } catch (IOException e) { e.printStackTrace(); }
    }

    public SocketServer addReadyEvent(OnReadyEvent event){
        readyEvents.add(event);
        return this;
    }

    public SocketServer removeReadyEvent(OnReadyEvent event){
        readyEvents.remove(event);
        return this;
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

    public int getPort() {
        return port;
    }

    public int getMaxconnections() {
        return maxconnections;
    }

    public int getTimeout() {
        return timeout;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public List<OnConnectEvent> getConnectEvents() {
        return connectEvents;
    }

    public List<OnDisconnectEvent> getDisconnectEvents() {
        return disconnectEvents;
    }

    public List<OnReadyEvent> getReadyEvents() {
        return readyEvents;
    }
}
