package xyz.baddeveloper.lwsl.client.events;

import java.net.Socket;

public interface OnConnectEvent {

    void onConnect(Socket socket);
}
