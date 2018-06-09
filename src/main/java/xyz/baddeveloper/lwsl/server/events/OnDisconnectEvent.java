package xyz.baddeveloper.lwsl.server.events;

import java.net.Socket;

public interface OnDisconnectEvent {

    void onDisconnect(Socket socket);
}
