package xyz.baddeveloper.lwsl.server.events;

import java.net.Socket;

public interface OnConnectEvent {

    void onConnect(Socket socket);
}
