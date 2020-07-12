package xyz.baddeveloper.lwsl.client;

import dev.throwouterror.eventbus.SimpleEventManager;
import org.json.JSONObject;
import xyz.baddeveloper.lwsl.Packet;
import xyz.baddeveloper.lwsl.client.events.*;
import xyz.baddeveloper.lwsl.exceptions.ConnectException;

import javax.net.ssl.SSLContext;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;

public class SocketClient {

    private String address;
    private int port;
    private int timeout;
    private boolean keepalive;

    /*
        private List<OnConnectEvent> connectEvents = new ArrayList<>();
        private List<OnDisconnectEvent> disconnectEvents = new ArrayList<>();
        private List<OnPacketReceivedEvent> packetReceivedEvents = new ArrayList<>();
        private List<OnPacketSentEvent> packetSentEvents = new ArrayList<>();
    */
    private final SimpleEventManager eventManager = new SimpleEventManager();
    private boolean connected = false;
    private Socket socket;
    private final SSLContext sslContext;

    private DataInputStream dis;
    private DataOutputStream dos;

    public SocketClient(String address, int port) {
        this(address, port, null);
    }

    public SocketClient(String address, int port, SSLContext sslContext) {
        this.address = address;
        this.port = port;
        this.sslContext = sslContext;
    }

    public void connect() throws ConnectException {
        try {
            if (sslContext != null) {
                socket = sslContext.getSocketFactory().createSocket(address, port);
            } else {
                socket = new Socket(address, port);
            }

            socket.setKeepAlive(keepalive);
            socket.setSoTimeout(timeout);

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

        } catch (Exception e) {
            throw new ConnectException(e);
        }
        connected = socket != null;
        if (!connected) return;
        eventManager.fireEvent(new ClientConnectEvent(this));
        listen();
    }

    public void shutdown() {
        try {
            if (socket != null) socket.close();
            connected = false;
        } catch (IOException ignored) {
        }
    }

    private void listen() {
        Executors.newSingleThreadExecutor().execute(() -> {
            while (!socket.isClosed()) {
                try {
                    Packet in = new Packet(new JSONObject(dis.readUTF()));
                    eventManager.fireEvent(new ClientPacketReceivedEvent(this, in));
                } catch (Exception e) {
                    try {
                        socket.close();
                    } catch (IOException ignored) {
                    }
                    eventManager.fireEvent(new ClientDisconnectEvent(this));
                    break;
                }
            }
        });
        eventManager.fireEvent(new ClientReadyEvent(this));
    }

    public void sendPacket(Packet packet) {
        try {
            packet.setName(packet.getClass().getSimpleName().replace("Packet", ""));
            dos.writeUTF(packet.getObject().toString());
            dos.flush();
            eventManager.fireEvent(new ClientPacketSentEvent(this, packet));
        } catch (IOException e) {
            e.printStackTrace();
            //TODO: Create exception
        }
    }

    public SocketClient setAddress(String address) {
        this.address = address;
        return this;
    }

    public SocketClient setPort(int port) {
        this.port = port;
        return this;
    }

    public SocketClient setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public SocketClient setKeepAlive(boolean keepalive) {
        this.keepalive = keepalive;
        return this;
    }

    public SocketClient addEventListener(Object eventHandlerClass) {
        eventManager.addEventListener(eventHandlerClass);
        return this;
    }

    public boolean isKeepAlive() {
        return keepalive;
    }

    public boolean isConnected() {
        return connected;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return timeout;
    }
}
