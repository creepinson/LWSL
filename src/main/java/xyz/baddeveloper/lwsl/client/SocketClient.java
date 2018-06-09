package xyz.baddeveloper.lwsl.client;

import xyz.baddeveloper.lwsl.client.events.OnConnectEvent;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SocketClient {

    private String address;
    private int port;
    private int timeout;
    private boolean keepalive;

    private List<OnConnectEvent> connectEventList;

    private boolean connected = false;
    private Socket socket;

    public SocketClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void connect(){
        try {
            socket = new Socket(address, port);

            socket.setKeepAlive(keepalive);
            socket.setSoTimeout(timeout);

            connectEventList = new ArrayList<>();
        }catch (IOException e){
            e.printStackTrace();
        }
        connected = socket != null;
        if(!connected) return;
        connectEventList.forEach(onConnectEvent -> onConnectEvent.onConnect(socket));
        listen();
    }

    private void listen(){
        new Thread(() -> {
            while(!socket.isClosed() && socket.isConnected()){

            }
        }).start();
    }

    private SocketClient setAddress(String address){
        this.address = address;
        return this;
    }

    private SocketClient setPort(int port){
        this.port = port;
        return this;
    }

    private SocketClient setTimeout(int timeout){
        this.timeout = timeout;
        return this;
    }

    private SocketClient setKeepAlive(boolean keepalive){
        this.keepalive = keepalive;
        return this;
    }


}
