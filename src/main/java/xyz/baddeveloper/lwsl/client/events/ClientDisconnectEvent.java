package xyz.baddeveloper.lwsl.client.events;

import dev.throwouterror.eventbus.events.Event;
import xyz.baddeveloper.lwsl.client.SocketClient;

public class ClientDisconnectEvent extends Event {
    public final SocketClient client;

    public ClientDisconnectEvent(SocketClient client) {
        this.client = client;
    }
}
