package xyz.baddeveloper.lwsl.server;

import dev.throwouterror.eventbus.SimpleEventManager;
import xyz.baddeveloper.lwsl.server.events.ServerConnectEvent;
import xyz.baddeveloper.lwsl.server.events.ServerDisconnectEvent;
import xyz.baddeveloper.lwsl.server.events.ServerReadyEvent;

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

    final SimpleEventManager eventManager = new SimpleEventManager();

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
            if (sslContext != null) serverSocket = sslContext.getServerSocketFactory().createServerSocket(port);
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
            SocketHandler socketHandler = null;
            while (running) try {
                Socket socket = serverSocket.accept();
                socketHandler = new SocketHandler(this, socket);
                if (maxconnections != 0 && clients.size() >= maxconnections) {
                    eventManager.fireEvent(new ServerDisconnectEvent(this, socketHandler));
                    socket.close();
                    return;
                }

                clients.add(socketHandler);
                eventManager.fireEvent(new ServerConnectEvent(this, socketHandler));
                socketHandler.handle();
            } catch (IOException ignored) {
                eventManager.fireEvent(new ServerDisconnectEvent(this, socketHandler));
            }
        });
        eventManager.fireEvent(new ServerReadyEvent(this));
    }

    public void stop() {
        if (serverSocket != null)
            try {
                serverSocket.close();
            } catch (IOException ignored) {
            }
    }

    public SocketServer addEventListener(Object eventHandlerClass) {
        eventManager.addEventListener(eventHandlerClass);
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

    public List<SocketHandler> getClients() {
        return clients;
    }

    public Controller getController() {
        return controller;
    }
}
