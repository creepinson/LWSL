/*
 * Copyright (c) Creepinson
 */

package xyz.baddeveloper.lwsl.server.events;

import dev.throwouterror.eventbus.events.Event;
import xyz.baddeveloper.lwsl.server.SocketServer;

public class ServerReadyEvent extends Event {
    public final SocketServer server;

    public ServerReadyEvent(SocketServer server) {
        this.server = server;
    }
}
