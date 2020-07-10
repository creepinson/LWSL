package xyz.baddeveloper.lwsl.server.events;

import xyz.baddeveloper.lwsl.server.SocketHandler;

public interface OnConnectEvent {

    void onConnect(SocketHandler socket);
}
