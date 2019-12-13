package xyz.baddeveloper.lwsl.server;

import xyz.baddeveloper.lwsl.server.events.OnConnectEvent;
import xyz.baddeveloper.lwsl.server.events.OnDisconnectEvent;
import xyz.baddeveloper.lwsl.server.events.OnPacketReceivedEvent;
import xyz.baddeveloper.lwsl.server.events.OnPacketSentEvent;
import xyz.baddeveloper.lwsl.server.events.OnReadyEvent;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SocketServer {

    private static final int DEFAULT_PORT = 8080;

    private int port;
    private int maxconnections;
    private int timeout;

    private List<OnConnectEvent> connectEvents = new ArrayList<>();
    private List<OnDisconnectEvent> disconnectEvents = new ArrayList<>();
    private List<OnReadyEvent> readyEvents = new ArrayList<>();
    private List<OnPacketReceivedEvent> packetReceivedEvents = new ArrayList<>();
    private List<OnPacketSentEvent> packetSentEvents = new ArrayList<>();

    private List<SocketHandler> clients = new ArrayList<>();

    private boolean running = false;
    private Controller controller;
    private ServerSocket serverSocket;
    private final SSLContext sslContext;

    public SocketServer() {
        this(DEFAULT_PORT);
    }

    public SocketServer(SSLContext sslContext) {
        this(DEFAULT_PORT, sslContext);
    }

    public SocketServer(int port) {
        this(port, null);
    }

    public SocketServer(int port, SSLContext sslContext) {
        this.port = port;
        this.maxconnections = 1000;
        this.timeout = 0;
        this.sslContext = sslContext;
    }

    public void start() {
        try {
            if(sslContext != null) serverSocket = sslContext.getServerSocketFactory().createServerSocket(port);
            else serverSocket = new ServerSocket(port);

            serverSocket.setSoTimeout(timeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        running = serverSocket != null;
        listen();
    }

    private void listen() {
        if (!running)
            return;
        controller = new Controller(this);

        Executors.newSingleThreadExecutor().execute(() -> {
            while (running) try {
                Socket socket = serverSocket.accept();
                if (maxconnections != 0 && clients.size() >= maxconnections) {
                    socket.close();
                    return;
                }
                connectEvents.forEach(onConnectEvent -> onConnectEvent.onConnect(socket));
                SocketHandler socketHandler = new SocketHandler(this, socket);
                clients.add(socketHandler);
                socketHandler.handle();
            } catch (IOException ignored) {}
        });
        readyEvents.forEach(readyEvent -> readyEvent.onReady(this));
    }

    public void stop() {
        if (serverSocket != null)
            try { serverSocket.close(); } catch (IOException ignored) {}
    }

    public SocketServer addPacketReceivedEvent(OnPacketReceivedEvent event) {
        packetReceivedEvents.add(event);
        return this;
    }

    public SocketServer removePacketReceivedEvent(OnPacketReceivedEvent event) {
        packetReceivedEvents.remove(event);
        return this;
    }

    public SocketServer addPacketSentEvent(OnPacketSentEvent event) {
        packetSentEvents.add(event);
        return this;
    }

    public SocketServer removePacketSentEvent(OnPacketSentEvent event) {
        packetSentEvents.remove(event);
        return this;
    }

    public SocketServer addReadyEvent(OnReadyEvent event) {
        readyEvents.add(event);
        return this;
    }

    public SocketServer removeReadyEvent(OnReadyEvent event) {
        readyEvents.remove(event);
        return this;
    }

    public SocketServer addDisconnectEvent(OnDisconnectEvent event) {
        disconnectEvents.add(event);
        return this;
    }

    public SocketServer removeDisconnectEvent(OnDisconnectEvent event) {
        disconnectEvents.remove(event);
        return this;
    }

    public SocketServer addConnectEvent(OnConnectEvent event) {
        connectEvents.add(event);
        return this;
    }

    public SocketServer removeConnectEvent(OnConnectEvent event) {
        connectEvents.remove(event);
        return this;
    }

    public SocketServer setPort(int port) {
        this.port = port;
        return this;
    }

    public SocketServer setMaxConnections(int maxConnections) {
        this.maxconnections = maxConnections;
        return this;
    }

    public SocketServer setTimeout(int timeout) {
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

    public List<OnPacketReceivedEvent> getPacketReceivedEvents() {
        return packetReceivedEvents;
    }

    public List<OnPacketSentEvent> getPacketSentEvents() {
        return packetSentEvents;
    }

    public List<SocketHandler> getClients() {
        return clients;
    }

    public Controller getController() {
        return controller;
    }
}
