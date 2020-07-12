/*
 * Copyright (c) Creepinson
 */

package xyz.baddeveloper.lwsl.server.events;

import xyz.baddeveloper.lwsl.Packet;
import xyz.baddeveloper.lwsl.event.PacketEvent;
import xyz.baddeveloper.lwsl.server.SocketHandler;
import xyz.baddeveloper.lwsl.server.SocketServer;

/**
 * @author Theo Paris
 */
public class ServerPacketReceivedEvent extends PacketEvent {
    public final SocketServer server;
    public final SocketHandler client;

    public ServerPacketReceivedEvent(SocketServer server, SocketHandler client, Packet packet) {
        super(packet);
        this.server = server;
        this.client = client;
    }
}
