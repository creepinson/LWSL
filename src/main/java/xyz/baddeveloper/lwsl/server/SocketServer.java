package xyz.baddeveloper.lwsl.server;

public class SocketServer {

    private int port;
    private int maxconnections;

    public SocketServer() {
        this.port = 8080;
        this.maxconnections = 1000;
    }

    public SocketServer(int port) {
        this.port = port;
        this.maxconnections = 1000;
    }

    public void start(){

    }

    public void stop(){

    }

    public SocketServer setPort(int port){
        this.port = port;
        return this;
    }

    public SocketServer setMaxConnections(int maxConnections){
        this.maxconnections = maxConnections;
        return this;
    }
}
