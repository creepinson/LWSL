package xyz.baddeveloper.lwsl.client;

import org.json.JSONObject;
import xyz.baddeveloper.lwsl.client.events.OnConnectEvent;
import xyz.baddeveloper.lwsl.client.events.OnDisconnectEvent;
import xyz.baddeveloper.lwsl.client.events.OnPacketReceivedEvent;
import xyz.baddeveloper.lwsl.client.events.OnPacketSentEvent;
import xyz.baddeveloper.lwsl.exceptions.ConnectException;
import xyz.baddeveloper.lwsl.packet.Packet;

import javax.net.ssl.SSLContext;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class SocketClient {

    private String address;
    private int port;
    private int timeout;
    private boolean keepalive;

    private List<OnConnectEvent> connectEvents = new ArrayList<>();
    private List<OnDisconnectEvent> disconnectEvents = new ArrayList<>();
    private List<OnPacketReceivedEvent> packetReceivedEvents = new ArrayList<>();
    private List<OnPacketSentEvent> packetSentEvents = new ArrayList<>();

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

        } catch (Exception e){
            throw new ConnectException(e);
        }
        connected = socket != null;
        if (!connected) return;
        connectEvents.forEach(onConnectEvent -> onConnectEvent.onConnect(socket));
        listen();
    }

    public void shutdown() {
        try {
            if (socket != null) socket.close();
            connected = false;
        } catch (IOException ignored){}
    }

    private void listen() {
        Executors.newSingleThreadExecutor().execute(() -> {
            while(!socket.isClosed()) {
                try {
                    Packet in = new Packet(new JSONObject(dis.readUTF()));
                    packetReceivedEvents.forEach(onPacketReceivedEvent -> onPacketReceivedEvent.onPacketReceived(this, in));
                } catch (Exception e) {
                    try {
                        socket.close();
                    } catch (IOException ignored) {}
                    disconnectEvents.forEach(onDisconnectEvent -> onDisconnectEvent.onDisconnect(socket));
                    break;
                }
            }
        });
    }

    public void sendPacket(Packet packet) {
        try {
            dos.writeUTF(packet.getObject().toString());
            dos.flush();
            packetSentEvents.forEach(onPacketSentEvent -> onPacketSentEvent.onPacketSent(this, packet));
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

    public SocketClient addConnectEvent(OnConnectEvent event) {
        connectEvents.add(event);
        return this;
    }

    public SocketClient removeConnectEvent(OnConnectEvent event) {
        connectEvents.remove(event);
        return this;
    }

    public SocketClient addDisconnectEvent(OnDisconnectEvent event) {
        disconnectEvents.add(event);
        return this;
    }

    public SocketClient removeDisconnectEvent(OnDisconnectEvent event) {
        disconnectEvents.remove(event);
        return this;
    }

    public SocketClient addPacketReceivedEvent(OnPacketReceivedEvent event) {
        packetReceivedEvents.add(event);
        return this;
    }

    public SocketClient removePacketReceivedEvent(OnPacketReceivedEvent event) {
        packetReceivedEvents.remove(event);
        return this;
    }

    public SocketClient addPacketSentEvent(OnPacketSentEvent event) {
        packetSentEvents.add(event);
        return this;
    }

    public SocketClient removePacketSentEvent(OnPacketSentEvent event) {
        packetSentEvents.remove(event);
        return this;
    }

    public boolean isKeepalive() {
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

    public List<OnConnectEvent> getConnectEvents() {
        return connectEvents;
    }

    public List<OnDisconnectEvent> getDisconnectEvents() {
        return disconnectEvents;
    }

    public List<OnPacketReceivedEvent> getPacketReceivedEvents() {
        return packetReceivedEvents;
    }

    public List<OnPacketSentEvent> getPacketSentEvents() {
        return packetSentEvents;
    }
}
