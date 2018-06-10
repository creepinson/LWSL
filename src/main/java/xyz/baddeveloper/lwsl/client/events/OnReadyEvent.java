package xyz.baddeveloper.lwsl.client.events;

import xyz.baddeveloper.lwsl.server.SocketServer;

public interface OnReadyEvent {

    void onReady(SocketServer socketServer);
}
