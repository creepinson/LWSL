package xyz.baddeveloper.lwsl.client;

import xyz.baddeveloper.lwsl.client.events.OnConnectEvent;

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

    private List<OnConnectEvent> connectEventList = new ArrayList<>();

    private boolean connected = false;
    private Socket socket;

    private DataInputStream dis;
    private DataOutputStream dos;

    public SocketClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void connect(){
        try {
            socket = new Socket(address, port);

            socket.setKeepAlive(keepalive);
            socket.setSoTimeout(timeout);

            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

        }catch (IOException e){
            e.printStackTrace();
        }
        connected = socket != null;
        if(!connected) return;
        connectEventList.forEach(onConnectEvent -> onConnectEvent.onConnect(socket));
        listen();
    }

    public void shutdown(){
        try{
            if(socket != null) socket.close();
            connected = false;
        }catch (IOException ignored){}
    }

    private void listen(){
        Executors.newSingleThreadExecutor().execute(() -> {
            while(!socket.isClosed()){
                try {

                } catch (Exception e) {
                    shutdown();
                    break;
                }
            }
        });
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
