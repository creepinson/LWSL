package xyz.baddeveloper.lwsl.server.events;

import xyz.baddeveloper.lwsl.server.SocketServer;

public interface OnReadyEvent {

    void onReady(SocketServer socketServer);
}
