/*
 * Copyright (c) Creepinson
 */

package xyz.baddeveloper.lwsl.server.events;

import xyz.baddeveloper.lwsl.Packet;
import xyz.baddeveloper.lwsl.event.PacketEvent;
import xyz.baddeveloper.lwsl.server.SocketServer;

/**
 * @author Theo Paris
 */
public class ServerPacketSentEvent extends PacketEvent {
    public final SocketServer server;

    public ServerPacketSentEvent(SocketServer server, Packet packet) {
        super(packet);
        this.server = server;
    }
}
