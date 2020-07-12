/*
 * Copyright (c) Creepinson
 */

package xyz.baddeveloper.lwsl.server.events;

import dev.throwouterror.eventbus.events.Event;
import xyz.baddeveloper.lwsl.server.SocketHandler;
import xyz.baddeveloper.lwsl.server.SocketServer;

public class ServerDisconnectEvent extends Event {
    public final SocketServer server;
    public final SocketHandler client;

    public ServerDisconnectEvent(SocketServer server, SocketHandler client) {
        this.server = server;
        this.client = client;
    }
}