package xyz.baddeveloper.lwsl.client.events;

import dev.throwouterror.eventbus.events.Event;
import xyz.baddeveloper.lwsl.client.SocketClient;

public class ClientConnectEvent extends Event {
    public final SocketClient client;

    public ClientConnectEvent(SocketClient client) {
        this.client = client;
    }
}
