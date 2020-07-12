/*
 * Copyright (c) Creepinson
 */

package xyz.baddeveloper.lwsl.client.events;

import xyz.baddeveloper.lwsl.Packet;
import xyz.baddeveloper.lwsl.client.SocketClient;
import xyz.baddeveloper.lwsl.event.PacketEvent;

/**
 * @author Theo Paris
 */
public class ClientPacketSentEvent extends PacketEvent {
    public final SocketClient client;

    public ClientPacketSentEvent(SocketClient client, Packet packet) {
        super(packet);
        this.client = client;
    }
}
