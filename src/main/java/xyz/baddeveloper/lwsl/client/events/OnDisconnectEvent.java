package xyz.baddeveloper.lwsl.client.events;

import java.net.Socket;

public interface OnDisconnectEvent {

    void onDisconnect(Socket socket);
}
