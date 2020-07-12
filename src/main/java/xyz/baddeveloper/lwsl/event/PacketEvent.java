/*
 * Copyright (c) Creepinson
 */

package xyz.baddeveloper.lwsl.event;

import dev.throwouterror.eventbus.events.Event;
import xyz.baddeveloper.lwsl.Packet;

/**
 * @author Theo Paris
 */
public class PacketEvent extends Event {
    public final Packet packet;

    public PacketEvent(Packet packet) {
        this.packet = packet;
    }
}
