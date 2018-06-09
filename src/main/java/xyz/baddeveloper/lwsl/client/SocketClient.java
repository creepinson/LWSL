package xyz.baddeveloper.lwsl.client;

public class SocketClient {

    private String address;
    private int port;
    private int timeout;
    private boolean keepalive;

    public SocketClient(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public void connect(){

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
