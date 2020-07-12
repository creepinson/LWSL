/*
 * Copyright (c) Creepinson
 */

package xyz.baddeveloper.lwsl.client.events;

import dev.throwouterror.eventbus.events.Event;
import xyz.baddeveloper.lwsl.client.SocketClient;

public class ClientReadyEvent extends Event {
    public final SocketClient client;

    public ClientReadyEvent(SocketClient client) {
        this.client = client;
    }
}
